package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ContaResumoDto;
import com.senai.conta_bancaria.application.dto.TransferenciaDto;
import com.senai.conta_bancaria.application.dto.ValorSaqueDepositoDto;
import com.senai.conta_bancaria.application.service.ContaService;
// Importações do Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
// Fim das importações do Swagger
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Contas", description = "Gerenciamento de contas bancárias e operações financeiras")
@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Exige autenticação em todos os endpoints desta controller
public class ContaController {
    private final ContaService service;

    // CRUD
    // Create: embutido em Cliente

    // Read
    @Operation(
            summary = "Listar todas as contas",
            description = "Retorna todas as contas cadastradas no sistema. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDto>> listarTodasAsContas() {
        return ResponseEntity
                .ok(service.listarTodasAsContas());
    }

    @Operation(
            summary = "Listar contas por CPF do cliente",
            description = "Retorna todas as contas associadas a um CPF específico. Requer autenticação.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente", example = "12345678900")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contas encontradas com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<ContaResumoDto>> listarContasPorCpf(@PathVariable String cpf) {
        return ResponseEntity
                .ok(service.listarContasPorCpf(cpf));
    }

    @Operation(
            summary = "Buscar conta por número",
            description = "Retorna uma conta bancária específica pelo seu número. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta", example = "1001")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @GetMapping("/numero/{numero}")
    public ResponseEntity<ContaResumoDto> buscarConta(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.buscarConta(numero));
    }

    // Update
    @Operation(
            summary = "Atualizar status da conta (Ativo/Inativo)",
            description = "Atualiza o status de uma conta. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta a ser atualizada", example = "1001")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaAtualizacaoDto.class),
                            examples = @ExampleObject(name = "Exemplo para inativar", value = "{\"ativo\": false}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PutMapping("/{numero}")
    public ResponseEntity<ContaResumoDto> atualizarConta(@PathVariable Long numero,
                                                         @Valid @org.springframework.web.bind.annotation.RequestBody ContaAtualizacaoDto dto) {
        return ResponseEntity
                .ok(service.atualizarConta(numero, dto));
    }

    // Delete
    @Operation(
            summary = "Deletar uma conta",
            description = "Remove uma conta do sistema bancário a partir do número. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta a ser removida", example = "1001")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta removida com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> apagarConta(@PathVariable Long numero) {
        service.apagarConta(numero);
        return ResponseEntity
                .noContent() // status code: 204 (encontrado, sem conteúdo)
                .build();
    }

    // Ações específicas

    @Operation(
            summary = "Realizar um saque",
            description = "Debita um valor do saldo de uma conta específica. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta para o saque", example = "1001")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDto.class),
                            examples = @ExampleObject(value = "{\"valor\": 150.00}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou valor inválido (ex: negativo)"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PostMapping("/{numero}/sacar")
    public ResponseEntity<ContaResumoDto> sacar(@PathVariable Long numero,
                                                @Valid @org.springframework.web.bind.annotation.RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.sacar(numero, dto));
    }

    @Operation(
            summary = "Realizar um depósito",
            description = "Credita um valor ao saldo de uma conta específica. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta para o depósito", example = "1001")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDto.class),
                            examples = @ExampleObject(value = "{\"valor\": 500.00}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Valor inválido (ex: negativo)"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PostMapping("/{numero}/depositar")
    public ResponseEntity<ContaResumoDto> depositar(@PathVariable Long numero,
                                                    @Valid @org.springframework.web.bind.annotation.RequestBody ValorSaqueDepositoDto dto) {
        return ResponseEntity
                .ok(service.depositar(numero, dto));
    }

    @Operation(
            summary = "Realizar uma transferência",
            description = "Transfere um valor da conta de origem (no path) para uma conta de destino (no body). Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta de ORIGEM", example = "1001")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransferenciaDto.class),
                            examples = @ExampleObject(value = "{\"contaDestino\": 1002, \"valor\": 200.00}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente, valor inválido ou transferência para a mesma conta"),
                    @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PostMapping("/{numero}/transferir")
    public ResponseEntity<ContaResumoDto> transferir(@PathVariable Long numero,
                                                     @Valid @org.springframework.web.bind.annotation.RequestBody TransferenciaDto dto) {
        return ResponseEntity
                .ok(service.transferir(numero, dto));
    }

    @Operation(
            summary = "Aplicar rendimento (Apenas Poupança)",
            description = "Calcula e aplica o rendimento de uma conta poupança. Falhará se a conta não for do tipo Poupança. Requer autenticação.",
            parameters = {
                    @Parameter(name = "numero", description = "Número da conta POUPANÇA", example = "1002")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rendimento aplicado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "A conta não é do tipo Poupança"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PostMapping("/{numero}/rendimento")
    public ResponseEntity<ContaResumoDto> rendimento(@PathVariable Long numero) {
        return ResponseEntity
                .ok(service.rendimento(numero));
    }
}