package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ContaResumoDto;
import com.senai.conta_bancaria.application.dto.TransferenciaDto;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDto;
import com.senai.conta_bancaria.application.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    // CRUD

    // Create: embutido em Cliente

    // Read
    @GetMapping
    public ResponseEntity<List<ContaResumoDto>> listarTodasAsContas() {
        return ResponseEntity
                .ok(service.listarTodasAsContas()); // status code: 200 (encontrado com êxito)
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<ContaResumoDto>> listarContasPorCpf(@PathVariable Long cpf) {
        return ResponseEntity
                .ok(service.listarContasPorCpf(cpf)); // status code: 200 (encontrado com êxito)
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContaResumoDto> buscarConta(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.buscarConta(numero));
    }

    // Update
    @PutMapping("/{numero}")
    public ResponseEntity<ContaResumoDto> atualizarConta(@PathVariable Long numero,
                                                         @Valid @RequestBody ContaAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarConta(numero, dto));
    }

    // Delete
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> apagarConta(@PathVariable Long numero) {
        service.apagarConta(numero);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }

    // Ações específicas

    @PostMapping("/{numero}/sacar")
    public ResponseEntity<ContaResumoDto> sacar(@PathVariable Long numero,
                                                @Valid @RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.sacar(numero, dto));
    }

    @PostMapping("/{numero}/depositar")
    public ResponseEntity<ContaResumoDto> depositar(@PathVariable Long numero,
                                                    @Valid @RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.depositar(numero, dto));
    }

    @PostMapping("/{numero}/transferir")
    public ResponseEntity<ContaResumoDto> transferir(@PathVariable Long numero,
                                                     @Valid @RequestBody TransferenciaDto dto) {
        return ResponseEntity
                .ok(service.transferir(numero, dto));
    }

    @PostMapping("/{numero}/rendimento")
    public ResponseEntity<ContaResumoDto> rendimento(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.rendimento(numero));
    }
}
