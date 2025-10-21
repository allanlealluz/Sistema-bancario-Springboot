package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDto;
import com.senai.conta_bancaria.application.dto.ClienteResponseDto;
import com.senai.conta_bancaria.application.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    // CRUD

    // Create
    @PostMapping
    public ResponseEntity<ClienteResponseDto> registrarCliente(@Valid @RequestBody ClienteRegistroDto dto) {
        return ResponseEntity // retorna o código de status
                .created(URI.create("api/cliente")) // status code: 201 (criado com êxito)
                .body(service.registrarCliente(dto));
    }

    // Read
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodosOsClientes() {
        return ResponseEntity
                .ok(service.listarTodosOsClientes()); // status code: 200 (encontrado com êxito)
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> buscarCliente(@PathVariable Long cpf) {
        return ResponseEntity
                .ok(service.buscarCliente(cpf));
    }

    // Update
    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Long cpf,
                                                               @Valid @RequestBody ClienteAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarCliente(cpf, dto));
    }

    // Delete
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarCliente(@PathVariable Long cpf) {
        service.apagarCliente(cpf);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }
}