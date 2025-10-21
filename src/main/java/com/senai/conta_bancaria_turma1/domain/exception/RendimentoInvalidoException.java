package com.senai.conta_bancaria_turma1.domain.exception;

public class RendimentoInvalidoException extends RuntimeException {
    public RendimentoInvalidoException() {
        super("Rendimento deve ser aplicado somente em conta poupança!");
    }
}
