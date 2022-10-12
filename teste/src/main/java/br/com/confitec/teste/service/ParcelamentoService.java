package br.com.confitec.teste.service;

import br.com.confitec.teste.model.Cobertura;
import br.com.confitec.teste.model.Dados;
import br.com.confitec.teste.model.OpcaoParcelamento;

import java.util.List;

public interface ParcelamentoService {
    List<Dados> recuperarParcelamento(List<Cobertura> coberturas, List<OpcaoParcelamento> opcaoParcelamentos);
}
