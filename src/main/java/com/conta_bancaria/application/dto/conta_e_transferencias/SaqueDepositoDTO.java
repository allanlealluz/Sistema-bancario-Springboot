package com.conta_bancaria.application.dto.conta_e_transferencias;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaqueDepositoDTO(
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        @JsonProperty("valor")
        BigDecimal valor
) {
}
