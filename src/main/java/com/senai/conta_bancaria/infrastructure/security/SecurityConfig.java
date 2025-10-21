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
        http.csrf(// todo
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Permitir acesso público a endpoints de autenticação e documentação
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Gerentes
                        .requestMatchers(HttpMethod.POST, "/gerentes/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/gerentes/**").hasAnyRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/gerentes/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/gerentes/**").hasRole("GERENTE")

                        // Clientes
                        .requestMatchers(HttpMethod.GET, "/clientes").hasAnyRole("GERENTE")
                        .requestMatchers(HttpMethod.POST, "/clientes").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("GERENTE")

                        // Contas bancárias
                        .requestMatchers(HttpMethod.GET, "/contas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/contas").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/contas/**").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/contas/**").hasRole("GERENTE")

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