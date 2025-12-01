package com.conta_bancaria.application.dto;

import com.conta_bancaria.domain.entity.Gerente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import com.conta_bancaria.domain.enums.Role;

@Builder
public record GerenteDTO(
        String id,

        @NotBlank(message = "É necessário um nome para cadastrar um novo administrador.")
        @Size(min = 3, max = 100)
        String nome,

        @NotBlank(message = "O CPF não pode estar vazio.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "É necessário digitar um endereço de email para cadastrar um novo administrador.")
        String email,

        @NotBlank(message = "O usuário precisa de uma senha.")
        @Size(min = 4, max = 8, message = "A sua senha deve ter no mínimo 4 digitos.")
        String senha,
        Boolean ativo,
        Role role
) {
    public static GerenteDTO fromEntity(Gerente gerente) {
        return GerenteDTO.builder()
                .id(gerente.getId())
                .nome(gerente.getNome())
                .cpf(gerente.getCpf())
                .email(gerente.getEmail())
                .ativo(gerente.isAtivo())
                .role(gerente.getRole())
                .build();
    }

    public Gerente toEntity() {
        return Gerente.builder()
                .id(this.id)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .ativo(this.ativo != null ? this.ativo : true)
                .role(this.role != null ? this.role : Role.GERENTE)
                .build();
    }
}
