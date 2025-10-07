package com.senai.conta_bancaria_turma1.exception;

public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException(String message) {
        super("O cliente já possui uma conta desse tipo.");
    }
}
