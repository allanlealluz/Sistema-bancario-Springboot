package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dispositivos_iot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIoT {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String codigoSerial;

    @Column(columnDefinition = "TEXT")
    private String chavePublica; // Chave para validar a origem da msg MQTT

    @Column(nullable = false)
    private boolean ativo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_dispositivo_cliente"))
    private Cliente cliente;
}
