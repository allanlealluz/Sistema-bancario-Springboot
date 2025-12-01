package com.conta_bancaria.ui_interface.controller;

import com.conta_bancaria.application.dto.conta_e_transferencias.PagamentoRequestDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.PagamentoResponseDTO;
import com.conta_bancaria.application.dto.conta_e_transferencias.SaqueDepositoDTO;
import com.conta_bancaria.application.service.PagamentoAppService;
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
@RequestMapping("/api/pagamento")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoAppService service;

    //buscar um pagamento por boleto
    @Operation(
            summary = "Buscar pagamento por boleto",
            description = "Exibe o pagamento pelo nome ou número do boleto especificado",
            parameters = {
                    @Parameter(name = "boleto", description = "pagamento a ser buscado:", example = "Boleto de Luz")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento encontrado com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Pagamento não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Pagamento não existente/inativo.\"")
                            )
                    )
            }
    )
    @GetMapping("/{boleto}")
    public ResponseEntity<PagamentoResponseDTO> verPagamento(@PathVariable String boleto){
        return ResponseEntity.ok(service.verPagamento(boleto));
    }

    //listar todos os pagamentos
    @Operation(
            summary = "Listar todos os pagamentos",
            description = "Retorna todas os pagamentos realizados por um cliente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pagamentos retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentos(){
        return ResponseEntity.ok(service.listarPagamentos());
    }


    //realizar pagamento
    @Operation(
            summary = "Cliente realiza um pagamento",
            description = "Cliente pode realizar um pagamento",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de pagamento", value = """
                                        {
                                           "contaNumero": "12346",
                                           "boleto": "ContaDeLuz",\s
                                           "valorPago": 100.00,
                                           "tipoPagamento": "LUZ",\s
                                           "codigoAutenticacao": "123"
                                         }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Saldo Insuficiente", value = "\"Saldo insuficiente para realizar a operação: PAGAMENTO com o valor de R$100.00\""),
                                            @ExampleObject(name = "Valor negativo", value = "\"Valor do pagamento deve ser positivo.\""),
                                            @ExampleObject(name = "Conta inválida ou inativa", value = "\"Conta inválida ou inativa.\""),
                                            @ExampleObject(name = "Tipo de pagamento inválido ou inexistente", value = "\"Tipo de pagamento inválido/inexistente. Verfique se digitou corretamente: LUZ, AGUA ou BOLETO.\""),
                                    }
                            )
                    ),
            }
    )
    @PostMapping("/cliente")
    public ResponseEntity<PagamentoResponseDTO> realizarPagamento(
            @Valid @RequestBody PagamentoRequestDTO dto) {

        PagamentoResponseDTO pagamento = service.realizarPagamento(dto);

        return ResponseEntity.created(
                URI.create("/api/pagamento/" + pagamento.boleto())
        ).body(pagamento);
    }
}