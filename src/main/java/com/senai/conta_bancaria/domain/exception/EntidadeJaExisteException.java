package com.senai.conta_bancaria.domain.exception;

public class EntidadeJaExisteException extends RuntimeException {
    public EntidadeJaExisteException(String message) {
        super(message);
    }
}
