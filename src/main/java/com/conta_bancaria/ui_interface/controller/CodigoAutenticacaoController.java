package com.conta_bancaria.ui_interface.controller;

import com.conta_bancaria.application.dto.codigo_autenticacao.CodigoRequestDTO;
import com.conta_bancaria.application.dto.codigo_autenticacao.CodigoResponseDTO;
import com.conta_bancaria.application.service.CodigoAutenticacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class CodigoAutenticacaoController {
    private final CodigoAutenticacaoService service;

    @PostMapping("/registrar")
    public ResponseEntity<CodigoResponseDTO> registrarCodigo(
            @Valid @RequestBody CodigoRequestDTO dto) {
        return ResponseEntity.ok(service.registrarCodigo(dto));
    }

    @PostMapping("/validar")
    public ResponseEntity<CodigoResponseDTO> validarCodigo(
            @RequestParam String codigo,
            @RequestParam String clienteCpf) {
        return ResponseEntity.ok(service.validarCodigo(codigo, clienteCpf));
    }
}
