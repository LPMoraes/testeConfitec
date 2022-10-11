package br.com.confitec.teste.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CoberturaException extends RuntimeException {
    private HttpStatus httpStatus;

    public CoberturaException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
