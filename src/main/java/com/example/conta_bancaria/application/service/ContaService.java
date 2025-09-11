package com.example.conta_bancaria.application.service;

import com.example.conta_bancaria.application.dto.ContaDTO;
import com.example.conta_bancaria.domain.entity.*;
import com.example.conta_bancaria.domain.repository.ClienteRepository;
import com.example.conta_bancaria.domain.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    // Criar conta para um cliente
    public ContaDTO criarConta(Long clienteId, String tipo, Double saldoInicial) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Conta conta;
        if ("CORRENTE".equalsIgnoreCase(tipo)) {
            ContaCorrente c = new ContaCorrente();
            c.setLimiteChequeEspecial(500.0);
            conta = c;
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            ContaPoupanca c = new ContaPoupanca();
            c.setTaxaRendimento(0.05);
            conta = c;
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido: " + tipo);
        }

        conta.setSaldo(saldoInicial != null ? saldoInicial : 0.0);
        conta.setNumero("ACC-" + System.currentTimeMillis()); // número simples de conta
        conta.setCliente(cliente);

        Conta salva = contaRepository.save(conta);
        return ContaDTO.fromEntity(salva);
    }

    // Buscar conta por id
    public ContaDTO buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        return ContaDTO.fromEntity(conta);
    }
}
