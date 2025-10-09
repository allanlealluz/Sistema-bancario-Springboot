package com.senai.conta_bancaria_turma1.domain.exception;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String message) {
        super("Tipo de conta invalida. Os tipos válidos são: 'CORRENTE' e 'POUPANCA'.");
    }
}
