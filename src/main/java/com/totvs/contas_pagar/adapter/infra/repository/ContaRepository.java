package com.totvs.contas_pagar.adapter.infra.repository;

import com.totvs.contas_pagar.adapter.entities.ContaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ContaRepository extends JpaRepository<ContaEntity, Long> {
    @Query("SELECT SUM(c.valor) FROM ContaEntity c WHERE c.situacao = PAGA AND c.dataPagamento BETWEEN :dataInicial AND :dataFinal")
    BigDecimal findTotalValorPagoByPeriodo(LocalDate dataInicial, LocalDate dataFinal);

    List<ContaEntity> findByDataVencimentoBetweenAndDescricaoContaining(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal, String descricao);
}
