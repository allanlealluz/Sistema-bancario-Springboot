package com.example.conta_bancaria.application.dto;

public record OperacaoRequest(
        Long clienteId,
        String numero,
        Double valor
) {}
