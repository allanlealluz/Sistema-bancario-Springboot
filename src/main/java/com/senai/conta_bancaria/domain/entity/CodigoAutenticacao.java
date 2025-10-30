package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "codigos_autenticacao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAutenticacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean validado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_codigo_cliente"))
    private Cliente cliente;

    // Opcional: vincular ao pagamento que ele autorizou
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", unique = true, foreignKey = @ForeignKey(name = "fk_codigo_pagamento"))
    private Pagamento pagamento;
}
