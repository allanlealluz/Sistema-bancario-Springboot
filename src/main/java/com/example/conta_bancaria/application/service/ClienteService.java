package com.example.conta_bancaria.application.service;

import com.example.conta_bancaria.application.dto.ClienteDTO;
import com.example.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.example.conta_bancaria.application.dto.ClienteResponseDTO;
import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;
import com.example.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    public ClienteResponseDTO registrarCliente(ClienteRegistroDTO dto){
        var cliente = clienteRepository.findByCpfandAtivoTrue(dto.cpf()).orElseGet(
                ()-> clienteRepository.save(dto.toEntity))
        );
    }
}
