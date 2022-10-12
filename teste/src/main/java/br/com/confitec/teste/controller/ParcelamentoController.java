package br.com.confitec.teste.controller;

import br.com.confitec.teste.exception.CoberturaException;
import br.com.confitec.teste.exception.RequestDtoNullException;
import br.com.confitec.teste.model.Dados;
import br.com.confitec.teste.model.RequestDto;
import br.com.confitec.teste.service.ParcelamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "confitec/teste")
@RequiredArgsConstructor
public class ParcelamentoController {

    private final ParcelamentoService parcelamentoService;

    @PostMapping(value = "parcelamento")
    public ResponseEntity<List<Dados>> obterParcelamento(@RequestBody(required = false) RequestDto dto){
        try {
            if (Objects.isNull(dto)) {
                throw new RequestDtoNullException("Requisicao invalida", HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(parcelamentoService.recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento()));
        }catch (RequestDtoNullException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getHttpStatus());
        }catch (CoberturaException ex){
            return new ResponseEntity(ex.getMessage(), ex.getHttpStatus());
        }catch (Exception ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
