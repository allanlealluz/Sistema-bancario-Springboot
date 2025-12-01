package com.conta_bancaria.application.dto.conta_e_transferencias;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaAtualizadaDTO(
        @NotNull @PositiveOrZero(message = "O saldo não pode ser negativo")
        BigDecimal saldo,

        @NotNull @PositiveOrZero(message = "O limite não pode ser negativo")
        BigDecimal limite,

        @NotNull @DecimalMin(value = "0.00", inclusive = true, message = "O rendimento deve ser positivo")
        BigDecimal rendimento,

        @NotNull @DecimalMin(value = "0.00", message = "A taxa deve ser positiva")
        @DecimalMax(value = "1.00", message = "A taxa deve estar entre 0 e 1 (0% a 100%)")
        BigDecimal taxa
) {
}
