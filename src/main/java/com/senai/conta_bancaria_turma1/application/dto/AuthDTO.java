package com.senai.conta_bancaria_turma1.application.dto;

public class AuthDTO {

    public record LoginRequest(
            String email,
            String senha
    ) {}
    public record TokenResponse(
            String token
    ) {}
}
