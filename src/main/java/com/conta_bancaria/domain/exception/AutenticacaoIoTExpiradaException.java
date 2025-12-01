package com.conta_bancaria.domain.exception;

public class AutenticacaoIoTExpiradaException extends RuntimeException {
    public AutenticacaoIoTExpiradaException(String message) {
        super("Erro ao autenticar: " + message);
    }
}
