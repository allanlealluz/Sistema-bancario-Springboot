package com.conta_bancaria.application.dto.conta_e_transferencias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaDTO(
        @NotBlank(message = "A conta de destino é obrigatória")
        @Pattern(regexp = "\\d{5,20}", message = "Número da conta destino inválido")
        String contaDestino,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor
) {
}
