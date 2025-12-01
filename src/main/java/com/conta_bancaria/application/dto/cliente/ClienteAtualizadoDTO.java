package com.conta_bancaria.application.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteAtualizadoDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "Por favor, digite um email para o cliente")
        String email,

        @NotBlank(message = "O usuário precisa de uma senha.")
        @Size(min = 4, max = 8, message = "A sua senha deve ter no mínimo 4 digitos.")
        String senha
) {
}
