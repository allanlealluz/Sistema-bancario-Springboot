package com.example.conta_bancaria.application.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;

import java.util.List;
import java.util.stream.Collectors;

public record ClienteDTO(
        Long id,
        String nome,
        String cpf,
        List<ContaDTO> contas
) {
    public static ClienteDTO fromEntity(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getContas() != null ?
                        cliente.getContas().stream()
                                .map(ContaDTO::fromEntity)
                                .collect(Collectors.toList()) :
                        List.of()
        );
    }

    public Cliente toEntity(List<Conta> contas) {
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setContas(contas);
        if (contas != null) {
            contas.forEach(conta -> conta.setCliente(cliente));
        }
        return cliente;
    }

}
