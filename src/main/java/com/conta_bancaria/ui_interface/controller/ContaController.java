package com.conta_bancaria.ui_interface.controller;

import com.conta_bancaria.application.dto.conta_e_transferencias.ContaAtualizadaDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.ContaResumoDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.SaqueDepositoDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.TransferenciaDTO;
import com.conta_bancaria.application.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Contas", description = "Gerenciamento de conta dos clientes")
@RestController
@RequestMapping("/conta")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;

    //listar todas as contas
    @Operation(
            summary = "Listar todas as contas",
            description = "Retorna todas as contas cadastradas por um gerente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contas retornadas com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarContas() {
        return ResponseEntity.ok(service.exibirContas());
    }

    //listar conta por número
    @Operation(
            summary = "Buscar conta por número",
            description = "Exibe a conta pelo número especificado",
            parameters = {
                    @Parameter(name = "numero", description = "conta a ser buscada:", example = "90009-X")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada no sistema.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @GetMapping("/{numero}")
    public ResponseEntity<ContaResumoDTO> buscarContaPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(service.exibirContasPorNumero(numero));
    }

    //atualizar conta por número
    @Operation(
            summary = "Atualizar uma conta",
            description = "Atualiza os dados de uma conta existente com novos dados",
            parameters = {
                    @Parameter(name = "numero", description = "Digite o numero da conta para atualizar:", example = "90009-X")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaAtualizadaDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                        {
                                          "saldo":200.0,
                                          "limite":500.0,
                                          "taxa":0.03
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Tipo inválido", value = "\"Tipo de conta: 'BANCARIA' desconhecido. Os únicos válidos são: 'CORRENTE' e 'POUPANCA'.\""),
                                            @ExampleObject(name = "Limite negativo", value = "\"O limite não pode ser negativo\""),
                                            @ExampleObject(name = "Saldo negativo", value = "\"O saldo não pode ser negativo\""),
                                            @ExampleObject(name = "Rendimento negativo", value = "\"O rendimento deve ser positivo\""),
                                            @ExampleObject(name = "Taxa inválida", value = "A taxa deve estar entre 0 e 1 (0% a 100%)")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @PutMapping("/{numero}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(@PathVariable String numero,
                                                         @Valid @RequestBody ContaAtualizadaDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numero, dto));
    }

    //deletar conta
    @Operation(
            summary = "Deletar uma conta",
            description = "Desativa uma conta do sistema a partir do seu numero",
            parameters = {
                    @Parameter(name = "numero", description = "Numero da conta a ser desativada:", example = "90009-X")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta removida com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numero) {
        service.deletarConta(numero);
        return ResponseEntity.noContent().build();
    }

    //saque
    @Operation(
            summary = "Sacar um valor de uma conta",
            description = "Saca um determinado valor que esteja disponível na conta do cliente",
            parameters = {
                    @Parameter(name = "numero", description = "Digite o numero da conta realizar o saque:", example = "90009-X")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de saque", value = """
                                        {
                                          "valor":300.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Saldo Insuficiente", value = "\"Saldo insuficiente para realizar a operação: SAQUE com o valor de R$300,00\""),
                                            @ExampleObject(name = "Valor negativo", value = "\"O valor não pode ser negativo\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numero}/sacar")
    public ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numero,
                                                @Valid @RequestBody SaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.sacar(numero, dto));
    }

    //deposito
    @Operation(
            summary = "Depositar um valor em uma conta",
            description = "Deposita um determinado valor na conta do cliente",
            parameters = {
                    @Parameter(name = "numero", description = "Digite o numero da conta realizar o deposito:", example = "90009-X")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de depósito", value = """
                                        {
                                          "valor":300.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Valor negativo", value = "\"O valor não pode ser negativo\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numero}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(@PathVariable String numero,
                                                    @Valid @RequestBody SaqueDepositoDTO dto) {
        System.out.println("DTO Recebido: " + dto);
        return ResponseEntity.ok(service.depositar(numero, dto));
    }

    //transferencia
    @Operation(
            summary = "Transferir um valor de uma conta para outra",
            description = "Saca um valor de uma conta e deposita na outra, conforme números informados.",
            parameters = {
                    @Parameter(name = "numero", description = "Digite o numero da conta realizar a transferencia:", example = "90009-X")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de transferência", value = """
                                        {
                                          "numero":"98998-0"
                                          "valor":300.0
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Valor negativo", value = "\"O valor não pode ser negativo\""),
                                            @ExampleObject(name = "Transferir para a mesma conta", value = "Não é possível para transferir para a mesma conta.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numero}/transferir")
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numero,
                                                     @Valid @RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.transferir(numero, dto));
    }

    @Operation(
            summary = "Aplicar rendimento para conta do tipo POUPANCA",
            description = "Aplica um valor em % para ser rendido em contas Poupança.",
            parameters = {
                    @Parameter(name = "numero", description = "Digite o numero da conta em que deseja aplicar o rendimento:", example = "90009-X")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de aplicação", value = """
                                        {
                                          "rendimento":0.06
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Rendimento em tipo de conta inválida", value = "\"Rendimento deve ser aplicado somente na conta Poupança.\""),
                                            @ExampleObject(name = "Transferir para a mesma conta", value = "Não é possível para transferir para a mesma conta.")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta '10394-8' não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numero}/rendimento")
    public ResponseEntity<ContaResumoDTO> rendimento(@PathVariable String numero) {
        return ResponseEntity.ok(service.aplicarRendimento(numero));
    }

}
