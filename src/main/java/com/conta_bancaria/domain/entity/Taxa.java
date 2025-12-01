package com.conta_bancaria.domain.entity;

import com.conta_bancaria.domain.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String descricao;

    private BigDecimal percentual;

    private BigDecimal valorFixo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipoPagamento;
}
