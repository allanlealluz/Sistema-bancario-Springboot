package com.conta_bancaria.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("POUPANCA")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ContaPoupanca extends Conta {

    @Column(precision = 5, scale = 2) // Ajuste na precisão
    private BigDecimal rendimento;

    @Override
    public String getTipo() {
        return "POUPANCA";
    }

    public void aplicarRendimento() {
        BigDecimal valorRendimento = getSaldo().multiply(rendimento);
        setSaldo(getSaldo().add(valorRendimento));
    }
}

