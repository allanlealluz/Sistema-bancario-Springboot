package com.example.conta_bancaria.interfaceui.controller;

import com.example.conta_bancaria.application.dto.ClienteDTO;
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

    // Criar um cliente via POST
    @PostMapping
    public ClienteDTO criarCliente(@RequestBody ClienteDTO clienteDTO) {
        return clienteService.criarCliente(clienteDTO);
    }

    // Buscar todos os clientes via GET
    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes();
    }

    // Buscar cliente por id
    @GetMapping("/{id}")
    public ClienteDTO buscarCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }
}

