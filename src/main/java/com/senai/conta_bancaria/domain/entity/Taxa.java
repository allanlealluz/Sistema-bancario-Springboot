package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "taxas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Taxa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String descricao; // Ex: IOF, Tarifa Bancária

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal percentual; // Ex: 0.0100 para 1%

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFixo; // Valor adicional

    // Construtor padrão para garantir valores não nulos
    public Taxa(String descricao, BigDecimal percentual, BigDecimal valorFixo) {
        this.descricao = descricao;
        this.percentual = percentual != null ? percentual : BigDecimal.ZERO;
        this.valorFixo = valorFixo != null ? valorFixo : BigDecimal.ZERO;
    }
}
