package com.conta_bancaria.application.dto.cliente;

import com.conta_bancaria.application.dto.conta_e_transferencias.ContaResumoDTO;
import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.Conta;
import com.conta_bancaria.domain.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;

public record ClienteRegistroDTO(
        @NotBlank(message = "Digite um nome para o cliente.")
        @Size(min = 3, max = 100)
        String nome,

        @NotBlank(message = "O CPF não pode estar vazio.")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "Por favor, digite um email para o cliente")
        String email,

        @NotBlank(message = "O usuário precisa de uma senha.")
        @Size(min = 4, max = 8, message = "A sua senha deve ter no mínimo 4 digitos.")
        String senha,

        @NotNull(message = "Deve haver pelo menos uma conta.")
        @Valid
        ContaResumoDTO contas
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .contas(new ArrayList<Conta>())
                .role(Role.CLIENTE)
                .build();
    }
}
