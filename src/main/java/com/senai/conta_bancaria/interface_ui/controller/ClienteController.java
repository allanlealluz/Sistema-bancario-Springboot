package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDto;
import com.senai.conta_bancaria.application.dto.ClienteResponseDto;
import com.senai.conta_bancaria.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes do sistema bancário")
@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService service;

    @Operation(
            summary = "Registrar um novo cliente",
            description = """
                    Adiciona um novo cliente ao sistema bancário. 
                    É necessário estar autenticado com um token válido (Bearer Token).
                    """,
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDto.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                    {
                                      "id": "string",
                                      "nome": "João da Silva",
                                      "cpf": "12345678900",
                                      "email": "joao.silva@email.com",
                                      "senha": "minhasenha123",
                                      "contas": [
                                        {
                                          "numero": 1001,
                                          "tipo": "POUPANCA",
                                          "saldo": 1500.00
                                        }
                                      ]
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou CPF já cadastrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "CPF inválido", value = "\"CPF informado é inválido.\""),
                                            @ExampleObject(name = "Duplicado", value = "\"Cliente com este CPF já existe.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token ausente ou inválido",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Acesso não autorizado. Token inválido ou ausente.\"")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDto> registrarCliente(
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRegistroDto dto) {
        return ResponseEntity
                .created(URI.create("/api/cliente"))
                .body(service.registrarCliente(dto));
    }

    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna todos os clientes cadastrados no sistema. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarTodosOsClientes() {
        return ResponseEntity.ok(service.listarTodosOsClientes());
    }

    @Operation(
            summary = "Buscar cliente por CPF",
            description = "Retorna um cliente a partir do seu CPF. Requer autenticação.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser buscado", example = "12345678900")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> buscarCliente(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarCliente(cpf));
    }

    @Operation(
            summary = "Atualizar informações de um cliente",
            description = "Atualiza os dados cadastrais de um cliente existente. Requer autenticação.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser atualizado", example = "12345678900")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteAtualizacaoDto.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                      "nome": "João Pereira da Silva",
                                      "email": "joao.pereira@email.com",
                                      "telefone": "(11) 98888-7777"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(
            @PathVariable String cpf,
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteAtualizacaoDto dto) {
        return ResponseEntity.ok(service.atualizarCliente(cpf, dto));
    }

    @Operation(
            summary = "Deletar um cliente",
            description = "Remove um cliente do sistema bancário a partir do CPF. Requer autenticação.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser removido", example = "12345678900")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
            }
    )
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> apagarCliente(@PathVariable String cpf) {
        service.apagarCliente(cpf);
        return ResponseEntity.noContent().build();
    }
}