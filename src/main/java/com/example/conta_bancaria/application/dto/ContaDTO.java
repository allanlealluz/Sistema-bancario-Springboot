package com.example.conta_bancaria.application.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;

public record ContaDTO(
        Long id,
        String numero,
        Double saldo,

        Cliente cliente

) {
    public static ContaDTO fromEntity(Conta conta) {
        if (conta == null) return null;
        return new ContaDTO(
                conta.getId(),
                conta.getNumero(),
                conta.getSaldo(),
                new Cliente()
        );
    }
}
