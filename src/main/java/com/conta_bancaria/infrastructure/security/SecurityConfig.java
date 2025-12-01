package com.conta_bancaria.infrastructure.security;

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
        http.csrf(
                        AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/swagger-ui/**","/v3/api-docs/**").permitAll()

                        //login
//                        .requestMatchers(HttpMethod.POST, "/login").hasAnyRole("GERENTE","CLIENTE")

                        //cliente
                        .requestMatchers(HttpMethod.POST, "/api/cliente").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/api/cliente/cpf/{cpf}").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/cliente/cpf/{cpf}").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/api/cliente").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/cliente/cpf/{cpf}").hasRole("GERENTE")

                        //conta
                        .requestMatchers(HttpMethod.GET, "/conta").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/conta/{numero}").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.POST, "/conta/{numero}/sacar").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/conta/{numero}/depositar").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/conta/{numero}/transferir").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PUT, "/conta/{numero}").hasAnyRole("GERENTE", "CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/conta/{numero}").hasAnyRole("GERENTE", "CLIENTE")
                        .requestMatchers(HttpMethod.POST, "/conta/{numero}/rendimento").hasRole("GERENTE")

                       // pagamento
                       .requestMatchers(HttpMethod.POST, "/api/pagamento").hasRole("GERENTE")
                       .requestMatchers(HttpMethod.POST, "/api/pagamento/cliente").hasRole("CLIENTE")
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

