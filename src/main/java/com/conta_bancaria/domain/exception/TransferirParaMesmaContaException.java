package com.conta_bancaria.domain.exception;

public class TransferirParaMesmaContaException extends RuntimeException {
    public TransferirParaMesmaContaException() {
        super("Não é possível para transferir para a mesma conta.");
    }
}
