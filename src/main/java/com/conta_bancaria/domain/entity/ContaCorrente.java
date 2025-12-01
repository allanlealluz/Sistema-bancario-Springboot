package com.conta_bancaria.domain.entity;

import com.conta_bancaria.domain.exception.SaldoInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CORRENTE")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ContaCorrente extends Conta {

    @Column(precision = 19, scale = 2) // Ajuste na precisão
    private BigDecimal limite;

    @Column(precision = 19, scale = 2) // Ajuste na precisão
    private BigDecimal taxa;

    @Override
    public String getTipo() {
        return "CORRENTE";
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "saque");

        BigDecimal custoSaque = valor.multiply(taxa);
        BigDecimal total = valor.add(custoSaque);

        if (saldo.add(limite).compareTo(total) < 0) {
            throw new SaldoInsuficienteException("saque");
        }

        this.setSaldo(this.getSaldo().subtract(total));
    }
}

