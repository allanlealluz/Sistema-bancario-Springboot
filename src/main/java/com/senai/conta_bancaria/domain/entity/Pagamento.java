package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pagamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private Conta conta;

    @Column(nullable = false)
    private String boleto; // Código do boleto ou identificador do serviço

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago; // Valor principal do boleto/serviço

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalDebitado; // valorPago + taxas

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPagamento status;

    @ManyToMany(fetch = FetchType.EAGER) // EAGER para facilitar o recálculo/auditoria
    @JoinTable(
            name = "pagamento_taxas",
            joinColumns = @JoinColumn(name = "pagamento_id"),
            inverseJoinColumns = @JoinColumn(name = "taxa_id")
    )
    private Set<Taxa> taxas;

    public enum StatusPagamento {
        SUCESSO,
        FALHA,
        SALDO_INSUFICIENTE, // Status registrado se a validação falhar
        PENDENTE_AUTENTICACAO_IOT,
        FALHA_AUTENTICACAO_IOT
    }
}
