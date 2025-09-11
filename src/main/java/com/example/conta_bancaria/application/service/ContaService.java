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

    // Criar conta
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
        conta.setNumero("ACC-" + System.currentTimeMillis());
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

    // Depósito
    public void depositar(Long clienteId, String numero, Double valor) {
        Conta conta = contaRepository.findByClienteIdAndNumero(clienteId, numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        conta.depositar(valor);
        contaRepository.save(conta);
    }

    // Saque
    public void sacar(Long clienteId, String numero, Double valor) {
        Conta conta = contaRepository.findByClienteIdAndNumero(clienteId, numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        conta.sacar(valor);
        contaRepository.save(conta);
    }

    // Transferência
    public void transferir(Long origemClienteId, String origemNumero,
                           Long destinoClienteId, String destinoNumero, Double valor) {
        Conta origem = contaRepository.findByClienteIdAndNumero(origemClienteId, origemNumero)
                .orElseThrow(() -> new RuntimeException("Conta origem não encontrada"));
        Conta destino = contaRepository.findByClienteIdAndNumero(destinoClienteId, destinoNumero)
                .orElseThrow(() -> new RuntimeException("Conta destino não encontrada"));

        origem.transferir(destino, valor);
        contaRepository.save(origem);
        contaRepository.save(destino);
    }

    // Render (apenas poupança)
    public void render(Long clienteId, String numero) {
        Conta conta = contaRepository.findByClienteIdAndNumero(clienteId, numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta instanceof ContaPoupanca cp) {
            cp.render();
            contaRepository.save(cp);
        } else {
            throw new IllegalArgumentException("Somente contas poupança podem render");
        }
    }
}
