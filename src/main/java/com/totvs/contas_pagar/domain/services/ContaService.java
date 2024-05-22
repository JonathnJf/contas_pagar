package com.totvs.contas_pagar.domain.services;

import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.Conta;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import com.totvs.contas_pagar.domain.ports.ContaRepositoryPort;
import com.totvs.contas_pagar.domain.ports.ContaServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ContaService implements ContaServicePort {

    private final ContaRepositoryPort contaRepositoryPort;

    public ContaService(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }
    @Override
    public Conta saveConta(ContaEntityDTO conta) {
        return contaRepositoryPort.save(conta);
    }
    @Override
    public List<Conta> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate dataInicial, LocalDate dataFinal,String descricao, Integer pagina, Integer itens) {
        return contaRepositoryPort.findByDataVencimentoBetweenAndDescricaoContaining(dataInicial, dataFinal, descricao, pagina, itens);
    }
    @Override
    public Optional<Conta> findById(Long id) {
        return contaRepositoryPort.findById(id);
    }
    @Override
    public ResponseEntity<Object> findTotalValorPagoByPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return contaRepositoryPort.findTotalValorPagoByPeriodo(dataInicial, dataFinal);
    }
    @Override
    public Conta updateConta(Long id, ContaEntityDTO contaEntityDTO) {
         return contaRepositoryPort.update(id, contaEntityDTO);
    }
    @Override
    public Conta updateSituacaoConta(Long id, SituacaoConta situacaoConta) {
        return contaRepositoryPort.updateSituacaoConta(id, situacaoConta);
    }
    public ResponseEntity<Object> uploadContas (MultipartFile file) throws IOException {
        return contaRepositoryPort.uploadContas(file);
    }

}
