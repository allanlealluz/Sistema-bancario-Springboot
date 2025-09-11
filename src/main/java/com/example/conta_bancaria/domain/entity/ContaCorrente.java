package com.example.conta_bancaria.domain.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ContaCorrente extends Conta {
    private Double limiteChequeEspecial;
    private Double taxa;

    @Override
    public void render() {
        // Conta corrente não rende
    }
}

