package br.com.confitec.teste.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Data
public class RequestDtoNullException extends RuntimeException{

    private HttpStatus httpStatus;

    public RequestDtoNullException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public static String montarMensagem(boolean flag){
        return "Nenhuma".concat(flag ? "cobertura" : " opção de parcelamento ").concat("foi encontrada");
    }
}
