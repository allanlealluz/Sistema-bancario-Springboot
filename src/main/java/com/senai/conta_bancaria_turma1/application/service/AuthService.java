package com.senai.conta_bancaria_turma1.application.service;

import com.senai.conta_bancaria_turma1.application.dto.AuthDTO;
import com.senai.conta_bancaria_turma1.domain.entity.Cliente;
import com.senai.conta_bancaria_turma1.domain.entity.Usuario;
import com.senai.conta_bancaria_turma1.domain.repository.ClienteRepository;
import com.senai.conta_bancaria_turma1.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDTO.LoginRequest req) {
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}


