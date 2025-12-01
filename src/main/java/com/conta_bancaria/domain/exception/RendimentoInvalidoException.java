package com.conta_bancaria.domain.exception;

public class RendimentoInvalidoException extends RuntimeException {
    public RendimentoInvalidoException() {
        super("Rendimento deve ser aplicado somente na conta Poupança.");
    }
}
