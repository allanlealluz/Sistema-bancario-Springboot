package com.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_cpf", columnNames = "cpf")
        })
public class Cliente extends Usuario {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Conta> contas = new ArrayList<>();

}

