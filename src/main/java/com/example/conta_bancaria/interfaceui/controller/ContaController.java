package com.example.conta_bancaria.interfaceui.controller;

import com.example.conta_bancaria.application.service.ContaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conta")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping("/depositar")
    public void depositar(@RequestBody OperacaoRequest request) {
        contaService.depositar(request.clienteId(), request.numero(), request.valor());
    }

    @PostMapping("/sacar")
    public void sacar(@RequestBody OperacaoRequest request) {
        contaService.sacar(request.clienteId(), request.numero(), request.valor());
    }

    @PostMapping("/transferir")
    public void transferir(@RequestBody TransferenciaRequest request) {
        contaService.transferir(
                request.origemClienteId(),
                request.origemNumero(),
                request.destinoClienteId(),
                request.destinoNumero(),
                request.valor()
        );
    }

    @PostMapping("/render")
    public void render(@RequestBody OperacaoRequest request) {
        contaService.render(request.clienteId(), request.numero());
    }
}
