package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.service.PagamentoAppService;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.application.dto.TaxaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxas")
@RequiredArgsConstructor
@Tag(name = "Taxas", description = "Gerenciamento de taxas financeiras")
@SecurityRequirement(name = "bearerAuth") // Swagger
public class TaxaController {

    private final PagamentoAppService pagamentoAppService;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE','ADMIN')")
    @Operation(summary = "Cadastra uma nova taxa",
            description = "Permitido apenas para usuários com perfil 'GERENTE'.")
    public ResponseEntity<Taxa> criarTaxa(@RequestBody @Valid TaxaDto dto) {
        Taxa taxa = pagamentoAppService.criarTaxa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(taxa);
    }
}
