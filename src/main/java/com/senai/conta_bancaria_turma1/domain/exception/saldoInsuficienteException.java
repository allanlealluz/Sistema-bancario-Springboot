package com.senai.conta_bancaria_turma1.domain.exception;

public class saldoInsuficienteException extends RuntimeException {
    public saldoInsuficienteException(String message) {
        super(message);
    }
}
