package com.conta_bancaria.ui_interface.controller;

import com.conta_bancaria.application.dto.cliente.ClienteAtualizadoDTO;
import com.conta_bancaria.application.dto.cliente.ClienteRegistroDTO;
import com.conta_bancaria.application.dto.cliente.ClienteResponseDTO;
import com.conta_bancaria.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    //registrar
    @Operation(
            summary = "Registrar Cliente e Conta do cliente",
            description = "Cria e registra um cliente já atribuindo uma conta a ele",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                            {
                                              "nome": "Jonas",
                                              "cpf": "12345678910",
                                              "email":"jonas@email.com",
                                              "senha": "1234",
                                              "contas": {
                                                    "numero": "12346",
                                                    "tipo": "CORRENTE",
                                                    "saldo": 700.00
                                              }
                                             }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso!"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Conta do mesmo tipo", value = "Cliente já possui uma conta do tipo CORRENTE"),
                                            @ExampleObject(name = "Senha curta demais", value = "A sua senha deve ter no mínimo 4 digitos."),
                                            @ExampleObject(name = "CPF inválido", value = "O CPF deve contar exatamente 11 dígitos numéricos"),
                                            @ExampleObject(name = "Tipo de conta Inválido", value = "Tipo de conta: 'XXXXX' desconhecido. Os únicos válidos são: 'CORRENTE' e 'POUPANCA'.")
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRegistroDTO dto) {
        ClienteResponseDTO createdCliente = service.registrarCliente(dto);

        return ResponseEntity.created(URI.create("/api/cliente/cpf/" +
                createdCliente.cpf())).body(createdCliente);
    }

    //listar todos os clientes
    @Operation(
            summary = "Listar clientes",
            description = "Lista todos os clientes no banco de dados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com clientes retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> exibirCliente() {
        return ResponseEntity.ok(service.exibirClientesAtivos());
    }

    //buscar por cpf
    @Operation(
            summary = "Listar clientes por CPF",
            description = "Retorna clientes ativos pelo CPF requesitado.",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Cliente que deseja buscar: 123.456.789-10")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF: 123456789   10 não encontrado.")
                            )
                    )
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarClientePorCpf(cpf));
    }

    //atualizar cliente
    @Operation(
            summary = "Atualizar Cliente e Conta do cliente",
            description = "Atualiza e salva as informações de um cliente",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Cliente que deseja buscar: 123.456.789-10")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "nome": "Henrique",
                                          "cpf": "12378945611",
                                          "email":"henrique@email.com",
                                          "senha": "1234"
                                         }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Conta do mesmo tipo", value = "Cliente já possui uma conta do tipo CORRENTE"),
                                            @ExampleObject(name = "Senha curta demais", value = "A sua senha deve ter no mínimo 4 digitos."),
                                            @ExampleObject(name = "CPF inválido", value = "O CPF deve contar exatamente 11 dígitos numéricos"),
                                            @ExampleObject(name = "Tipo de conta Inválido", value = "Tipo de conta: 'XXXXX' desconhecido. Os únicos válidos são: 'CORRENTE' e 'POUPANCA'.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF: 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable String cpf,
                                                               @Valid @RequestBody ClienteAtualizadoDTO dto) {
        ClienteResponseDTO clienteAtualizado = service.atualizarCliente(cpf, dto);
        return ResponseEntity.ok().build();
    }


    //deletar cliente
    @Operation(
            summary = "Deletar um cliente",
            description = "Desativa a conta e o registro de um cliente do sistema",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Cliente que deseja buscar: 12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Cliente com CPF: 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf) {
        service.deletarCliente(cpf);
        return ResponseEntity.ok().build();
    }
}
