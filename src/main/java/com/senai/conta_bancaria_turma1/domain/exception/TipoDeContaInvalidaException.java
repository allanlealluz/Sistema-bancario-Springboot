package com.senai.conta_bancaria_turma1.domain.exception;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String tipo) {
        super("Tipo de conta "+ tipo+" inválida. Os tipos válidos são: 'CORRENTE', 'POUPANCA'.");
    }
}
