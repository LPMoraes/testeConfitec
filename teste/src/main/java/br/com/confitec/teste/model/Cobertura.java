package br.com.confitec.teste.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cobertura {
    private Long cobertura;
    private BigDecimal valor;
}
