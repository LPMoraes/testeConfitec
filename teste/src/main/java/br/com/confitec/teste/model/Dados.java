package br.com.confitec.teste.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dados {
    private int quantidadeParcelas;
    private BigDecimal valorPrimeiraParcela;
    private BigDecimal valorDemaisParcelas;
    private BigDecimal valorParcelamentoTotal;
}
