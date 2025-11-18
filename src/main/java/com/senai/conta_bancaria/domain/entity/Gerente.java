package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gerentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gerente {

    @Id
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.GERENTE;

    @Column(nullable = false)
    private boolean ativo = true;

}