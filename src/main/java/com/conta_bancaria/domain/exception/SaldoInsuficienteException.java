package com.conta_bancaria.domain.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String operacao) {
        super("Saldo insuficiente para realizar a operação: " + operacao + "com o valor de R$");
    }
}
