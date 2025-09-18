package com.example.conta_bancaria.application.dto;

import com.example.conta_bancaria.domain.entity.Conta;

import java.math.BigDecimal;

public record ContaResumoDTO (
    String numero,
    String tipo,
    BigDecimal saldo
){
    public Conta toEntity(){
        return null;
    }
}
