package com.totvs.contas_pagar.adapter.infra.repository;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.totvs.contas_pagar.adapter.entities.ContaEntity;
import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.adapter.infra.repository.upload.ContaCsvRepresentation;
import com.totvs.contas_pagar.domain.Conta;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import com.totvs.contas_pagar.domain.ports.ContaRepositoryPort;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ContaRepositoryAdapter implements ContaRepositoryPort {
    private final ContaRepository contaRepository;
    private final ModelMapper modelMapper;
    @Override
    public Conta save(ContaEntityDTO conta) {
        ContaEntity entity = modelMapper.map(conta, ContaEntity.class);
        entity.atualizarSituacao();
        ContaEntity save = contaRepository.save(entity);
        return modelMapper.map(save, Conta.class);
    }
    @Override
    public List<Conta> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate dataInicial, LocalDate dataFinal, String descricao, Integer pagina, Integer itens) {
        List<ContaEntity> entities = contaRepository.findByDataVencimentoBetweenAndDescricaoContaining(PageRequest.of(pagina, itens), dataInicial, dataFinal, descricao);
        return entities.stream()
                .map(entity -> modelMapper.map(entity, Conta.class))
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Conta> findById(Long id) {
        Optional<ContaEntity> entity = contaRepository.findById(id);
        return entity.map(e -> modelMapper.map(e, Conta.class));
    }
    @Override
    public ResponseEntity<Object> findTotalValorPagoByPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        Optional<BigDecimal> valorTotal = contaRepository.findTotalValorPagoByPeriodo(dataInicial, dataFinal);
        Map<String, Optional<BigDecimal>> response = new HashMap<>();
        response.put("valorTotal", valorTotal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public Conta update(Long id, ContaEntityDTO conta) {
        ContaEntity existeConta = contaRepository.findById(id).orElseThrow();
        existeConta.setDataVencimento(conta.getDataVencimento());
        existeConta.setDataPagamento(conta.getDataPagamento());
        existeConta.setValor(conta.getValor());
        existeConta.setDescricao(conta.getDescricao());
        existeConta.atualizarSituacao();
        ContaEntity save = contaRepository.save(existeConta);
        return modelMapper.map(save, Conta.class);
    }
    @Override
    public Conta updateSituacaoConta(Long id, SituacaoConta situacaoConta) {
        ContaEntity existeConta = contaRepository.findById(id).orElseThrow();
        existeConta.setSituacao(situacaoConta);
        existeConta.atualizarDataPagamento();
        ContaEntity save = contaRepository.save(existeConta);
        return modelMapper.map(save, Conta.class);
    }
    public ResponseEntity<Object> uploadContas (MultipartFile file) throws IOException {
        Set<ContaEntity> contas = parseCsv(file);
        contaRepository.saveAll(contas);
        Map<String, Integer> response = new HashMap<>();
        response.put("Registros incluídos", contas.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    private Set<ContaEntity> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<ContaCsvRepresentation> csvToBean = new CsvToBeanBuilder<ContaCsvRepresentation>(reader)
                    .withType(ContaCsvRepresentation.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse().stream()
                    .map(csvLine -> {
                        try {
                            return ContaEntity.builder()
                                    .dataVencimento(csvLine.getDataVencimento())
                                    .dataPagamento(csvLine.getDataPagamento())
                                    .descricao(csvLine.getDescricao())
                                    .valor(csvLine.getValor())
                                    .situacao(csvLine.getSituacao())
                                    .build();
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    })
                    .collect(Collectors.toSet());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new IOException("Erro ao executar o parsing CSV no arquivo", e);
        }
    }
}
