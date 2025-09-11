package com.example.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // ou SINGLE_TABLE
public abstract class Conta {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String numero;
   private Double saldo;

   @ManyToOne
   @JoinColumn(name = "cliente_id")
   private Cliente cliente;
}
