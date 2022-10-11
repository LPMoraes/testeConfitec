package br.com.confitec.teste.service;

import br.com.confitec.teste.exception.CoberturaException;
import br.com.confitec.teste.exception.RequestDtoNullException;
import br.com.confitec.teste.model.Cobertura;
import br.com.confitec.teste.model.Dados;
import br.com.confitec.teste.model.OpcaoParcelamento;
import br.com.confitec.teste.util.MathUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ParcelamentoServiceImpl implements ParcelamentoService{

    @Override
    public List<Dados> recuperarParcelamento(List<Cobertura> coberturas, List<OpcaoParcelamento> opcaoParcelamentos) {

        if(Objects.isNull(coberturas) || Objects.isNull(opcaoParcelamentos)){
            throw new RequestDtoNullException(RequestDtoNullException.montarMensagem(Objects.isNull(coberturas)), HttpStatus.BAD_REQUEST);
        }

        if(coberturas.isEmpty() || opcaoParcelamentos.isEmpty()){
            throw new RequestDtoNullException(RequestDtoNullException.montarMensagem(coberturas.isEmpty()), HttpStatus.BAD_REQUEST);
        }

        BigDecimal valorCobertura = coberturas
                .stream()
                .map(Cobertura::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        if(valorCobertura.compareTo(BigDecimal.ZERO) < 0 || valorCobertura.equals(BigDecimal.ZERO)){
            throw new CoberturaException("Apolice do seguro não poder ter valor menor ou igual a zero", HttpStatus.BAD_REQUEST);
        }

        return opcaoParcelamentos
                .stream()
                .map(opcao -> recuperar(valorCobertura, opcao))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Dados> recuperar(BigDecimal valorCobertura, OpcaoParcelamento opcao){
        List<Dados> dados = new ArrayList<>();
        for (int i = opcao.getQuantidadeMinimaParcelas(); i < opcao.getQuantidadeMaximaParcelas() + 1; ++i){
           dados.add(calculcarParcelas(valorCobertura, opcao.getJuros(), i));
        }
        return dados;
    }

    private Dados calculcarParcelas(BigDecimal coberturaTotal, BigDecimal juros, int qtdParcelas){
        BigDecimal valorParcelamentoTotal  = (qtdParcelas > 6) ? MathUtil.arrendondar(MathUtil.calculcarJuros(coberturaTotal, juros, qtdParcelas))
                : coberturaTotal;

        BigDecimal valorDemaisParcelas = MathUtil.truncar(valorParcelamentoTotal.divide(BigDecimal.valueOf(qtdParcelas), MathContext.DECIMAL128));
        BigDecimal somaTodasAsDemaisParcelas = valorDemaisParcelas.multiply(BigDecimal.valueOf(qtdParcelas));
        BigDecimal diferenca = valorParcelamentoTotal.subtract(somaTodasAsDemaisParcelas);
        BigDecimal valorPrimeiraParcela = valorDemaisParcelas.add(diferenca);

        return Dados.builder()
                .quantidadeParcelas(qtdParcelas)
                .valorPrimeiraParcela(valorPrimeiraParcela)
                .valorDemaisParcelas(valorDemaisParcelas)
                .valorParcelamentoTotal(valorParcelamentoTotal)
                .build();
    }
}
