package com.totvs.contas_pagar.domain;

import com.totvs.contas_pagar.domain.enums.SituacaoConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Conta {
    private Long id;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private SituacaoConta situacao;


    public Long getId() {
        return id;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public SituacaoConta getSituacao() {
        return situacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setSituacao(SituacaoConta situacao) {
        this.situacao = situacao;
    }

    public Conta() {

    }

    public Conta(Long id, LocalDate dataVencimento, LocalDate dataPagamento, BigDecimal valor, String descricao, SituacaoConta situacao) {
        this.id = id;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.valor = valor;
        this.descricao = descricao;
        this.situacao = situacao;
    }
}
