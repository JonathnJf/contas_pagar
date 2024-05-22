package com.totvs.contas_pagar.adapter.infra.repository.upload;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContaCsvRepresentation {

    @CsvBindByName(column = "data_vencimento")
    @CsvDate("yyyy-MM-dd")
    private LocalDate dataVencimento;
    @CsvDate("yyyy-MM-dd")
    @CsvBindByName(column = "data_pagamento")
    private LocalDate dataPagamento;
    @CsvBindByName(column = "valor")
    private BigDecimal valor;
    @CsvBindByName(column = "descricao")
    private String descricao;
    @CsvBindByName(column = "situacao")
    private SituacaoConta situacao;

}
