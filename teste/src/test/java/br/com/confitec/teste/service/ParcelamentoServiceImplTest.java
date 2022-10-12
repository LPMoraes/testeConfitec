package br.com.confitec.teste.service;

import br.com.confitec.teste.exception.CoberturaException;
import br.com.confitec.teste.exception.RequestDtoNullException;
import br.com.confitec.teste.model.Cobertura;
import br.com.confitec.teste.model.Dados;
import br.com.confitec.teste.model.OpcaoParcelamento;
import br.com.confitec.teste.util.MathUtil;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class ParcelamentoServiceImplTest {
    @InjectMocks
    private ParcelamentoServiceImpl service;
    private EasyRandom easyRandom;
    private List<Cobertura> coberturas;
    private List<OpcaoParcelamento> opcaoParcelamentos;
    private List<Dados> dados;

    @BeforeEach
    public void setUp(){
       easyRandom = new EasyRandom();
       coberturas = easyRandom.objects(Cobertura.class, 2).collect(Collectors.toList());
       opcaoParcelamentos = easyRandom.objects(OpcaoParcelamento.class, 1).collect(Collectors.toList());
        dados = easyRandom.objects(Dados.class, 3).collect(Collectors.toList());
       initCoberturas();
       initOpcoes();
       initDados();
    }

    private void initCoberturas(){
        coberturas.get(0).setCobertura(1L);
        coberturas.get(0).setValor(new BigDecimal(123.12));
        coberturas.get(1).setCobertura(4L);
        coberturas.get(1).setValor(new BigDecimal(345.45));
    }

    private void initOpcoes(){
        opcaoParcelamentos.get(0).setJuros(BigDecimal.valueOf(0.01));
        opcaoParcelamentos.get(0).setQuantidadeMinimaParcelas(7);
        opcaoParcelamentos.get(0).setQuantidadeMaximaParcelas(9);
    }

    private void initDados(){
        dados.get(0).setQuantidadeParcelas(7);
        dados.get(0).setValorPrimeiraParcela(new BigDecimal(71.81));
        dados.get(0).setValorDemaisParcelas(new BigDecimal(71.76));
        dados.get(0).setValorParcelamentoTotal(new BigDecimal(502.37));

        dados.get(1).setQuantidadeParcelas(8);
        dados.get(1).setValorPrimeiraParcela(new BigDecimal(63.45));
        dados.get(1).setValorDemaisParcelas(new BigDecimal(63.42));
        dados.get(1).setValorParcelamentoTotal(new BigDecimal(507.39));

        dados.get(2).setQuantidadeParcelas(9);
        dados.get(2).setValorPrimeiraParcela(new BigDecimal(56.95));
        dados.get(2).setValorDemaisParcelas(new BigDecimal(56.94));
        dados.get(2).setValorParcelamentoTotal(new BigDecimal(512.47));
    }

    @Test
    public void deveRetornaRequestDtoNullException_quandoAsCoberturasEstiverNulla(){
        String msgError = "Nenhuma cobertura foi encontrada";
        RequestDtoNullException thrown = assertThrows(
                RequestDtoNullException.class,
                () -> service.recuperarParcelamento(null, opcaoParcelamentos),
                msgError
        );
        Assertions.assertTrue(thrown.getMessage().contains(msgError));
        Assertions.assertEquals(thrown.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deveRetornaRequestDtoNullException_quandoAsOpcoesParcelamentosEstiverVazia(){
        String msgError = "Nenhuma opcao de parcelamento foi encontrada";
        RequestDtoNullException thrown = assertThrows(
                RequestDtoNullException.class,
                () -> service.recuperarParcelamento(coberturas, new ArrayList<>()),
                msgError
        );
        Assertions.assertTrue(thrown.getMessage().contains(msgError));
        Assertions.assertEquals(thrown.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deveRetornaCoberturaException_quandoValorCoberturaForMenorOuIgualAZero(){
        String msgError = "Apolice do seguro não poder ter valor menor ou igual a zero";
        coberturas
                .stream()
                .forEach(c -> c.setValor(BigDecimal.ZERO));

        CoberturaException thrown = assertThrows(
                CoberturaException.class,
                () -> service.recuperarParcelamento(coberturas, opcaoParcelamentos),
                msgError
        );
        Assertions.assertTrue(thrown.getMessage().contains(msgError));
        Assertions.assertEquals(thrown.getHttpStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deveRecuperarParcelamento(){
        List<Dados> result = service.recuperarParcelamento(coberturas, opcaoParcelamentos);
        Assertions.assertTrue(Objects.nonNull(result));
        Assertions.assertTrue(!result.isEmpty());
        for(int i = 0; i < dados.size() ; ++i){
            Assertions.assertEquals(dados.get(i).getQuantidadeParcelas(), result.get(i).getQuantidadeParcelas());
            Assertions.assertEquals(MathUtil.arrendondar(dados.get(i).getValorPrimeiraParcela()), MathUtil.arrendondar(result.get(i).getValorPrimeiraParcela()));
            Assertions.assertEquals(MathUtil.arrendondar(dados.get(i).getValorDemaisParcelas()), MathUtil.arrendondar(result.get(i).getValorDemaisParcelas()));
            Assertions.assertEquals(MathUtil.arrendondar(dados.get(i).getValorParcelamentoTotal()), MathUtil.arrendondar(result.get(i).getValorParcelamentoTotal()));
        }
    }
}
