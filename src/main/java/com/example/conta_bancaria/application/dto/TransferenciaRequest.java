package com.example.conta_bancaria.application.dto;

public record TransferenciaRequest(
        Long origemClienteId,
        String origemNumero,
        Long destinoClienteId,
        String destinoNumero,
        Double valor
) {}