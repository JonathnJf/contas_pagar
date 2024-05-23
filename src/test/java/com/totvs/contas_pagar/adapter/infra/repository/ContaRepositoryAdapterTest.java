package com.totvs.contas_pagar.adapter.infra.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.totvs.contas_pagar.adapter.entities.ContaEntity;
import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.Conta;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@DataJpaTest
@ActiveProfiles("test")
class ContaRepositoryAdapterTest {
    @Mock
    private ContaRepository contaRepository;
    private ModelMapper modelMapper = new ModelMapper();

    private ContaRepositoryAdapter contaRepositoryAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        contaRepositoryAdapter = new ContaRepositoryAdapter(contaRepository, modelMapper);
    }

    @Test
    @DisplayName("Deve Salvar uma nova conta")
     void testSaveTest() {
        ContaEntityDTO contaEntityDTO = new ContaEntityDTO();
        ContaEntity savedEntity = new ContaEntity();
        savedEntity.setId(1L);

        when(contaRepository.save(any(ContaEntity.class))).thenReturn(savedEntity);
        Conta savedConta = contaRepositoryAdapter.save(contaEntityDTO);

        assertEquals(savedEntity.getId(), savedConta.getId());
    }

    @Test
    @DisplayName("Deve buscar contas por intervalo de data de vencimento e descrição")
    void findByDataVencimentoBetweenAndDescricaoContainingTest() {
        LocalDate dataInicial = LocalDate.now();
        LocalDate dataFinal = LocalDate.now().plusDays(7);
        String descricao = "Teste";
        List<ContaEntity> entityList = Collections.singletonList(new ContaEntity());

        when(contaRepository.findByDataVencimentoBetweenAndDescricaoContaining(any(), eq(dataInicial), eq(dataFinal), eq(descricao))).thenReturn(entityList);

        List<Conta> contaList = contaRepositoryAdapter.findByDataVencimentoBetweenAndDescricaoContaining(dataInicial, dataFinal, descricao, 0, 10);
        assertEquals(entityList.size(), contaList.size());
    }

    @Test
    @DisplayName("Deve buscar o valor total pago por período")
    void findTotalValorPagoByPeriodoTest() {
        LocalDate dataInicial = LocalDate.now();
        LocalDate dataFinal = LocalDate.now().plusDays(7);
        BigDecimal valorTotal = BigDecimal.TEN;

        when(contaRepository.findTotalValorPagoByPeriodo(dataInicial, dataFinal)).thenReturn(Optional.of(valorTotal));

        ResponseEntity<Object> responseEntity = contaRepositoryAdapter.findTotalValorPagoByPeriodo(dataInicial, dataFinal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Optional<BigDecimal>> responseMap = (Map<String, Optional<BigDecimal>>) responseEntity.getBody();

        assertTrue(responseMap.containsKey("valorTotal"));
        assertEquals(valorTotal, responseMap.get("valorTotal").orElse(null));
    }

    @Test
    @DisplayName("Deve atualizar uma atualizar uma conta")
    void updateTest() {
        Long id = 1L;
        ContaEntityDTO contaEntityDTO = new ContaEntityDTO();
        ContaEntity existingEntity = new ContaEntity();
        existingEntity.setId(id);

        when(contaRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(contaRepository.save(any(ContaEntity.class))).thenReturn(existingEntity);

        Conta updatedConta = contaRepositoryAdapter.update(id, contaEntityDTO);
        assertEquals(existingEntity.getId(), updatedConta.getId());
    }

    @Test
    @DisplayName("Deve atualizar a situação de uma uma conta")
    void updateSituacaoContaTest() {
        Long id = 1L;
        SituacaoConta situacaoConta = SituacaoConta.PAGA;
        ContaEntity existingEntity = new ContaEntity();
        existingEntity.setId(id);

        when(contaRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(contaRepository.save(any(ContaEntity.class))).thenReturn(existingEntity);

        Conta updatedConta = contaRepositoryAdapter.updateSituacaoConta(id, situacaoConta);

        assertEquals(existingEntity.getId(), updatedConta.getId());
        assertEquals(situacaoConta, existingEntity.getSituacao());
    }

    @Test
    @DisplayName("Deve de fazer upload de contas a partir de um arquivo CSV")
    void uploadContas() throws IOException {
        File file = ResourceUtils.getFile("classpath:fileTest/ContasUploadTest.csv");
        FileInputStream input = new FileInputStream(file);

        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/csv", input);

        List<ContaEntity> contas = new ArrayList<>();
        contas.add(new ContaEntity());
        when(contaRepository.saveAll(anyList())).thenReturn(contas);

        ResponseEntity<Object> responseEntity = contaRepositoryAdapter.uploadContas(multipartFile);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Integer> responseMap = (Map<String, Integer>) responseEntity.getBody();
        assertTrue(responseMap.containsKey("Registros incluídos"));
        assertEquals(contas.size(), responseMap.get("Registros incluídos").intValue());
    }
}