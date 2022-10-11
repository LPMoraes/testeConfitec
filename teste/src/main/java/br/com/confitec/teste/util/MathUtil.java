package br.com.confitec.teste.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    private MathUtil(){

    }

    public static BigDecimal calculcarJuros(BigDecimal coberturaTotal, BigDecimal juros, int qtdParcelas){
        double base = (double) 1 + juros.doubleValue();
        double expoente = qtdParcelas;
        return coberturaTotal.multiply(BigDecimal.valueOf(Math.pow(base, expoente)));
    }

    public static BigDecimal truncar(BigDecimal valor){
        return valor.setScale(2, RoundingMode.DOWN);
    }

    public static BigDecimal arrendondar(BigDecimal valor){
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

}
