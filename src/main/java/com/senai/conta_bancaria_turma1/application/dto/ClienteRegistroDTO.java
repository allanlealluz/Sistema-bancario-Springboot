package com.senai.conta_bancaria_turma1.application.dto;

import com.senai.conta_bancaria_turma1.domain.entity.Cliente;
import com.senai.conta_bancaria_turma1.domain.entity.Conta;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;

public record ClienteRegistroDTO(
        @NotNull
        String nome,

        @CPF
        String cpf,
        @NotNull
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .contas(new ArrayList<Conta>())
                .build();
    }
}
