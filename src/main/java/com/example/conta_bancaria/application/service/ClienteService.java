package com.example.conta_bancaria.application.service;

import com.example.conta_bancaria.application.dto.ClienteDTO;
import com.example.conta_bancaria.domain.entity.Cliente;
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
        Cliente cliente = dto.toEntity(List.of());
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
