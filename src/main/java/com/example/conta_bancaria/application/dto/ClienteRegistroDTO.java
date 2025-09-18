package com.example.conta_bancaria.application.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;

import java.util.List;

public record ClienteRegistroDTO(String nome, String cpf, List<ContaDTO> contas) {

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        List<Conta> contasEntity = contas.stream()
                .map(ContaDTO::toEntity)
                .toList();
        contasEntity.forEach(conta -> conta.setCliente(cliente));
        cliente.setContas(contasEntity);
        return cliente;
    }

    public static ClienteRegistroDTO fromEntity(Cliente cliente) {
        List<ContaDTO> contasDTO = cliente.getContas().stream()
                .map(ContaDTO::fromEntity)
                .toList();
        return new ClienteRegistroDTO(cliente.getNome(), cliente.getCpf(), contasDTO);
    }
}


