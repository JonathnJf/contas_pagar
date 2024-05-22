package com.totvs.contas_pagar.domain.ports;

import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.Conta;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContaRepositoryPort {

    Conta save(ContaEntityDTO conta);

    Conta update(Long id, ContaEntityDTO contaEntityDTO);

    Conta updateSituacaoConta(Long id, SituacaoConta situacaoConta);

    Optional<Conta> findById(Long id);

    ResponseEntity<Object> findTotalValorPagoByPeriodo(LocalDate dataInicial, LocalDate dataFinal);

    List<Conta> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate dataInicial, LocalDate dataFinal, String descricao, Integer pagina, Integer itens);

    ResponseEntity<Object> uploadContas (MultipartFile file) throws IOException;
}
