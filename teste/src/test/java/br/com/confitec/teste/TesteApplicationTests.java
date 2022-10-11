package br.com.confitec.teste;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@SpringBootTest
class TesteApplicationTests {

	@Test
	void contextLoads() {
		BigDecimal coberturaTotal = BigDecimal.valueOf(123.12 + 345.45);
		this.opcoesParcelamento(coberturaTotal, 1 , 6 , BigDecimal.ZERO);
		this.opcoesParcelamento(coberturaTotal, 7 , 9 , BigDecimal.valueOf(0.01));
		this.opcoesParcelamento(coberturaTotal, 10, 12, BigDecimal.valueOf(0.03));
	}

	private void opcoesParcelamento(BigDecimal coberturaTotal, int qtdMinParcelas, int qtdMaxParcelas, BigDecimal juros){
		for (int i = qtdMinParcelas; i < qtdMaxParcelas + 1; ++i){
			calculcarParcelasComJuros(coberturaTotal, juros, i);
		}
	}

	private void calculcarParcelasComJuros(BigDecimal coberturaTotal, BigDecimal juros, int qtdParcelas){
		BigDecimal valorTotalAprox  = (qtdParcelas > 6) ? this.arrendondar(this.calculcarJuros(coberturaTotal, juros, qtdParcelas))
				: coberturaTotal;

		BigDecimal valorDemaisParcelas = this.truncar(valorTotalAprox.divide(BigDecimal.valueOf(qtdParcelas), MathContext.DECIMAL128));
		BigDecimal somaTodasAsDemaisParcelas = valorDemaisParcelas.multiply(BigDecimal.valueOf(qtdParcelas));
		BigDecimal diferenca = valorTotalAprox.subtract(somaTodasAsDemaisParcelas);
		BigDecimal valorPrimeiraParcela = valorDemaisParcelas.add(diferenca);

		System.out.println("quantidadeParcelas: " + qtdParcelas);
		System.out.println("valorPrimeiraParcela: " + truncar(valorPrimeiraParcela));
		System.out.println("valorDemaisParcelas: " + valorDemaisParcelas);
		System.out.println("valorParcelamentoTotal: " + valorTotalAprox);
		System.out.println("------------------------------\n");
	}
	
	public BigDecimal calculcarJuros(BigDecimal coberturaTotal, BigDecimal juros, int qtdParcelas){
		double base = (double) 1 + juros.doubleValue();
		double expoente = qtdParcelas;
		return coberturaTotal.multiply(BigDecimal.valueOf(Math.pow(base, expoente)));
	}

	public BigDecimal truncar(BigDecimal valor){
		return valor.setScale(2, RoundingMode.DOWN);
	}

	public BigDecimal arrendondar(BigDecimal valor){
		return valor.setScale(2, RoundingMode.HALF_EVEN);
	}

}
