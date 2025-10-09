package com.senai.conta_bancaria_turma1.domain.exception;

public class ValoresNegativosException extends RuntimeException {
    public ValoresNegativosException(String message) {
        super("Valor não pode ser negativo");
    }
}
