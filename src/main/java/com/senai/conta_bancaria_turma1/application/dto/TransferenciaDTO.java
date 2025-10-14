package com.senai.conta_bancaria_turma1.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaDTO (
        String contaDestino,
        @NotNull
        @Positive
        BigDecimal valor
){
}
