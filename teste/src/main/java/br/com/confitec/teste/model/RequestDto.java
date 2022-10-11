package br.com.confitec.teste.model;


import lombok.Data;
import java.util.List;

@Data
public class RequestDto {
    private List<Cobertura> listCobertura;
    private List<OpcaoParcelamento> listOpcaoParcelamento;
}
