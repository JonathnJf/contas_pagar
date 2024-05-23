package com.totvs.contas_pagar.adapter.entities;

import com.totvs.contas_pagar.adapter.entities.dtos.ContaEntityDTO;
import com.totvs.contas_pagar.domain.enums.SituacaoConta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="conta")
@Builder
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    @NotNull
    private BigDecimal valor;
    @NotNull
    private String descricao;
    @Enumerated(EnumType.STRING)
    private SituacaoConta situacao;

    public void atualizarSituacao() {
        if (this.dataPagamento != null) {
            this.situacao = SituacaoConta.PAGA;
        } else {
            this.situacao = SituacaoConta.PENDENTE;
        }
    }
    public void atualizarDataPagamento() {
        if(this.situacao == SituacaoConta.PAGA) {
            this.dataPagamento = LocalDate.now();
        }
        else {
            this.dataPagamento = null;
            this.situacao = SituacaoConta.PENDENTE;
        }
    }
}
