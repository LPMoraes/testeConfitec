package br.com.confitec.teste.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpcaoParcelamento {
    private int quantidadeMinimaParcelas;
    private int quantidadeMaximaParcelas;
    private BigDecimal juros;
}
