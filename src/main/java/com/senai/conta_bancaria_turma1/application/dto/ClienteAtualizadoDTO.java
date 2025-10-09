package com.senai.conta_bancaria_turma1.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteAtualizadoDTO(
        String nome,
        @NotBlank(message="obrigatorio cpf")
        @CPF
        String cpf
) {

}
