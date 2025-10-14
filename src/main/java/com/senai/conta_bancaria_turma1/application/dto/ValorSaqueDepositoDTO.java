package com.senai.conta_bancaria_turma1.application.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ValorSaqueDepositoDTO(
        @NotNull(message="o valor é obrigatório")
        BigDecimal valor
) {
}
