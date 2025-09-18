package com.example.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cpf;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;
}
