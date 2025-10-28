package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.GerenteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDto;
import com.senai.conta_bancaria.application.dto.GerenteResponseDto;
import com.senai.conta_bancaria.application.service.GerenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gerente")
@RequiredArgsConstructor
public class GerenteController {
    private final GerenteService service;

    // CRUD

    // Create
    @PostMapping
    public ResponseEntity<GerenteResponseDto> registrarGerente(@Valid @RequestBody GerenteRegistroDto dto) {
        return ResponseEntity // retorna o código de status
                .created(URI.create("api/gerente")) // status code: 201 (criado com êxito)
                .body(service.registrarGerente(dto));
    }

    // Read
    @GetMapping
    public ResponseEntity<List<GerenteResponseDto>> listarTodosOsGerentes() {
        return ResponseEntity
                .ok(service.listarTodosOsGerentes()); // status code: 200 (encontrado com êxito)
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> buscarGerente(@PathVariable String cpf) {
        return ResponseEntity
                .ok(service.buscarGerente(cpf));
    }

    // Update
    @PutMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> atualizarGerente(@PathVariable String cpf,
                                                               @Valid @RequestBody GerenteAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarGerente(cpf, dto));
    }

    // Delete
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarGerente(@PathVariable String cpf) {
        service.apagarGerente(cpf);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }
}