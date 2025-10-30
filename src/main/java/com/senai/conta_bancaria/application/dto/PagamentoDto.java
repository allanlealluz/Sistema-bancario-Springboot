package com.senai.conta_bancaria.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO para iniciar um novo pagamento")
public class PagamentoDto {

    @NotBlank
    @Schema(description = "Código de barras do boleto ou identificador do serviço", example = "84670000000100100099000123456000001234567890")
    private String boleto;

    @NotNull
    @DecimalMin("0.01")
    @Schema(description = "Valor principal a ser pago", example = "100.00")
    private BigDecimal valorPago;
}
