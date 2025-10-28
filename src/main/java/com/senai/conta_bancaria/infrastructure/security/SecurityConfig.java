package com.senai.conta_bancaria.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permitir acesso público
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // --- REGRAS DE GERENTE (ADMIN) ---
                        .requestMatchers("/gerentes/**").hasRole("ADMIN")

                        // --- REGRAS DE CLIENTE (ADMIN/GERENTE) ---
                        .requestMatchers("/clientes/**").hasAnyRole("ADMIN", "GERENTE")

                        // --- REGRAS DE CONTA (MAIS ESPECÍFICAS PRIMEIRO) ---

                        // 1. Ações do próprio CLIENTE (sacar, depositar, etc.)
                        .requestMatchers(HttpMethod.POST, "/contas/{numero}/(sacar|depositar|transferir|rendimento)").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/contas/cpf/{cpf}").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/contas/numero/{numero}").hasRole("CLIENTE")

                        // 2. Ações de gerenciamento (ADMIN/GERENTE)
                        // (Listar TUDO, atualizar status, deletar)
                        .requestMatchers(HttpMethod.GET, "/contas").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/contas/{numero}").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/contas/{numero}").hasAnyRole("ADMIN", "GERENTE")

                        // (Se um Admin/Gerente também puder ver contas por CPF/Número)
                        .requestMatchers(HttpMethod.GET, "/contas/cpf/{cpf}").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.GET, "/contas/numero/{numero}").hasAnyRole("ADMIN", "GERENTE")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(usuarioDetailsService);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}