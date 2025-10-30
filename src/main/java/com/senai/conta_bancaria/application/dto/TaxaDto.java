package com.senai.conta_bancaria.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO para criação ou atualização de uma Taxa")
public class TaxaDto {

    @NotBlank
    @Schema(description = "Descrição única da taxa", example = "IOF")
    private String descricao;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Valor percentual (ex: 0.01 para 1%)", example = "0.0038")
    private BigDecimal percentual;

    @NotNull
    @DecimalMin("0.0")
    @Schema(description = "Valor fixo adicional", example = "0.50")
    private BigDecimal valorFixo;
}
