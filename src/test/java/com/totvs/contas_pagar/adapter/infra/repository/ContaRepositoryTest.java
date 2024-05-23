package com.totvs.contas_pagar.adapter.infra.repository;

import com.totvs.contas_pagar.adapter.entities.ContaEntity;
import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class ContaRepositoryTest {

    private ModelMapper modelMapper;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
    }

    @Test
    @DisplayName("Retornar valor esperado com sucesso")
    void findTotalValorPagoByPeriodo() {
        LocalDate dataVencimento = LocalDate.parse("2024-05-22");
        LocalDate dataPagamento  = LocalDate.parse("2024-01-01");
        BigDecimal valor = BigDecimal.valueOf(1234567.89);
        String descricao = "Conta Teste A";

        ContaEntityDTO contaDTO = new ContaEntityDTO(dataVencimento, dataPagamento, valor, descricao, SituacaoConta.PAGA);
        this.createConta(contaDTO);

        LocalDate dataInicial = LocalDate.parse("2024-01-01");
        LocalDate dataFinal   = LocalDate.parse("2024-03-01");
        BigDecimal valorEsperado = BigDecimal.valueOf(1234567.89);

        Optional<BigDecimal> result = this.contaRepository.findTotalValorPagoByPeriodo(dataInicial, dataFinal);
        System.out.println(result);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(valorEsperado);
    }

    private ContaEntity createConta(ContaEntityDTO contaDTO) {
        ContaEntity novaConta = this.modelMapper.map(contaDTO, ContaEntity.class);
        this.entityManager.persist(novaConta);
        return novaConta;
    }
}
