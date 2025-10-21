package com.senai.conta_bancaria_turma1.infrastructure.config;

import com.senai.conta_bancaria_turma1.domain.entity.Gerente;
import com.senai.conta_bancaria_turma1.domain.enums.Role;
import com.senai.conta_bancaria_turma1.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GerenteBootstrap implements CommandLineRunner {

    private final GerenteRepository gerenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        gerenteRepository.findByEmail(adminEmail).ifPresentOrElse(
                gerente -> {
                    if (!gerente.isAtivo()) {
                        gerente.setAtivo(true);
                        gerenteRepository.save(gerente);
                    }
                },
                () -> {
                    Gerente admin = Gerente.builder()
                            .nome("Administrador Provisório")
                            .email(adminEmail)
                            .cpf("000.000.000-00")
                            .senha(passwordEncoder.encode(adminSenha))
                            .role(Role.GERENTE)
                            .build();
                    gerenteRepository.save(admin);
                    System.out.println("⚡ Usuário admin provisório criado: " + adminEmail);
                }
        );
    }
}