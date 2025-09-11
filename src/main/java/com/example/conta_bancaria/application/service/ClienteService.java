package com.example.conta_bancaria.application.service;

import com.example.conta_bancaria.application.dto.ClienteDTO;
import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;
import com.example.conta_bancaria.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Criar cliente
    public ClienteDTO criarCliente(ClienteDTO dto) {
        // Converte cada ContaDTO para Conta e vincula ao cliente
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        List<Conta> contas = dto.contas().stream()
                .map(contaDTO -> {
                    Conta conta = contaDTO.toEntity();
                    conta.setCliente(cliente);
                    return conta;
                })
                .collect(Collectors.toList());

        cliente.setContas(contas);

        Cliente salvo = clienteRepository.save(cliente);
        return ClienteDTO.fromEntity(salvo);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ClienteDTO.fromEntity(cliente);
    }
}
