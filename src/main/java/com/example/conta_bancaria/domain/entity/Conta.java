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
   public void depositar(double valor) {
      if (valor <= 0) throw new IllegalArgumentException("Valor inválido");
      saldo += valor;
   }

   public void sacar(double valor) {
      if (valor <= 0 || valor > saldo) throw new IllegalArgumentException("Saldo insuficiente");
      saldo -= valor;
   }

   public void transferir(Conta destino, double valor) {
      sacar(valor);
      destino.depositar(valor);
   }

   public abstract void render();
}
