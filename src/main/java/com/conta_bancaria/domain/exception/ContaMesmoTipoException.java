package com.conta_bancaria.domain.exception;

public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException() {
        super("Cliente já possui uma conta com este tipo.");
    }
}
