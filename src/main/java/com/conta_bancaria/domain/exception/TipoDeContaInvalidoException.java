package com.conta_bancaria.domain.exception;

public class TipoDeContaInvalidoException extends RuntimeException {
    public TipoDeContaInvalidoException(String tipo) {
        super("Tipo de conta: " + tipo + "desconhecido. Os únicos válidos são: 'CORRENTE' e 'POUPANCA'.");
    }
}
