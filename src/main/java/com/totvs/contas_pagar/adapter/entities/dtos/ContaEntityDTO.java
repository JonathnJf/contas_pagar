package com.totvs.contas_pagar.adapter.entities.dtos;

import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaEntityDTO {

    @NotNull
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    @NotNull
    private BigDecimal valor;
    @NotBlank
    private String descricao;
    private SituacaoConta situacao;
}
