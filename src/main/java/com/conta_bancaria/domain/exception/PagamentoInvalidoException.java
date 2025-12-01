package com.conta_bancaria.domain.exception;

public class PagamentoInvalidoException extends RuntimeException {
    public PagamentoInvalidoException(String message) {
        super(message);
    }
}
