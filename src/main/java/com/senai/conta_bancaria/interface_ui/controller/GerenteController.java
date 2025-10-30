package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.GerenteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDto;
import com.senai.conta_bancaria.application.dto.GerenteResponseDto;
import com.senai.conta_bancaria.application.service.GerenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Gerentes", description = "Gerenciamento de gerentes (apenas ADMIN)")
@RestController
@RequestMapping("/api/gerente")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GerenteController {

    private final GerenteService service;

    @Operation(summary = "Registrar novo gerente (ADMIN apenas)")
    @PostMapping
    public ResponseEntity<GerenteResponseDto> registrarGerente(
            @Valid @RequestBody GerenteRegistroDto dto) {
        return ResponseEntity
                .created(URI.create("/api/gerente"))
                .body(service.registrarGerente(dto));
    }

    @Operation(summary = "Listar todos os gerentes (ADMIN apenas)")
    @GetMapping
    public ResponseEntity<List<GerenteResponseDto>> listarTodosOsGerentes() {
        return ResponseEntity.ok(service.listarTodosOsGerentes());
    }

    @Operation(summary = "Buscar gerente por CPF (ADMIN apenas)")
    @GetMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> buscarGerente(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarGerente(cpf));
    }

    @Operation(summary = "Atualizar gerente (ADMIN apenas)")
    @PutMapping("/{cpf}")
    public ResponseEntity<GerenteResponseDto> atualizarGerente(
            @PathVariable String cpf,
            @Valid @RequestBody GerenteAtualizacaoDto dto) {
        return ResponseEntity.ok(service.atualizarGerente(cpf, dto));
    }

    @Operation(summary = "Deletar gerente (ADMIN apenas)")
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarGerente(@PathVariable String cpf) {
        service.apagarGerente(cpf);
        return ResponseEntity.noContent().build();
    }
}