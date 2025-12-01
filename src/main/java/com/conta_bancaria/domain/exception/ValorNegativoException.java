package com.conta_bancaria.domain.exception;

public class ValorNegativoException extends RuntimeException {
    public ValorNegativoException(String operacao) {
        super("Não é possível realizar um " + operacao + " com valores negativos.");
    }
}
