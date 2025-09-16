package com.example.conta_bancaria.domain.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ContaPoupanca extends Conta {
    private Double taxaRendimento;

    @Override
    public void render() {
        this.setSaldo(this.getSaldo() * (1 + taxaRendimento / 100));
    }
}
