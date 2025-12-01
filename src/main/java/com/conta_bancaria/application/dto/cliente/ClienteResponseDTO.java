package com.conta_bancaria.application.dto.cliente;

import com.conta_bancaria.application.dto.conta_e_transferencias.ContaResumoDTO;
import com.conta_bancaria.domain.entity.Cliente;

import java.util.List;

public record ClienteResponseDTO(
        String id,
        String nome,
        String cpf,

        String email,
        List<ContaResumoDTO> contas
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        List<ContaResumoDTO> contas = cliente.getContas().stream().map(ContaResumoDTO::fromEntity).toList();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                contas
        );
    }
}
