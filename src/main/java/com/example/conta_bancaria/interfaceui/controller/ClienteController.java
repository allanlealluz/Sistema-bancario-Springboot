package com.example.conta_bancaria.interfaceui.controller;

import com.example.conta_bancaria.application.dto.ClienteDTO;
import com.example.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.example.conta_bancaria.application.service.ClienteService;
import com.example.conta_bancaria.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping;
    public ClienteRegistroDTO registroDTO(@RequestBody(ClienteService dto)){
        this.clienteService.criarCliente();
    }
}

