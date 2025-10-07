package com.senai.conta_bancaria_turma1.exception;

public class TransferirParaMesmaContaException extends RuntimeException {
    public TransferirParaMesmaContaException(String message) {
        super(message);
    }
}
