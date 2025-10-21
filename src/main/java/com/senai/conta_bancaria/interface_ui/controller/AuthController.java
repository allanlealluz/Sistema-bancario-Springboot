package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.AuthDto;
import com.senai.conta_bancaria.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.TokenResponse> login(@RequestBody AuthDto.LoginRequest req) {
        String token = auth.login(req);
        return ResponseEntity.ok(new AuthDto.TokenResponse(token));
    }
}