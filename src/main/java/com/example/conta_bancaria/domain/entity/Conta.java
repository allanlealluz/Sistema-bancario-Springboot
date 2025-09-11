package com.example.conta_bancaria.domain.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public abstract class Conta {
   public int numero;
   public float saldo;
}
