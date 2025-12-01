package com.conta_bancaria.application.service;

import com.conta_bancaria.application.dto.cliente.ClienteAtualizadoDTO;
import com.conta_bancaria.application.dto.cliente.ClienteRegistroDTO;
import com.conta_bancaria.application.dto.cliente.ClienteResponseDTO;
import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.exception.ContaMesmoTipoException;
import com.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    private Cliente buscarClientePorCpfEAtivoTrue(String cpf) {
        var cliente = clienteRepository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("cliente")
        );
        return cliente;
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ClienteResponseDTO registrarCliente(ClienteRegistroDTO dto) {
        var cliente = clienteRepository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> clienteRepository.save(dto.toEntity())
        );

        var contas = cliente.getContas();
        var novaConta = dto.contas().toEntity(cliente);

        boolean hasConta = contas.stream().anyMatch(
                conta -> conta.getClass().equals(novaConta.getClass()) && conta.isAtiva()
        );

        if (hasConta)
            throw new ContaMesmoTipoException();

        cliente.getContas().add(novaConta);
        cliente.setSenha(passwordEncoder.encode(dto.senha()));

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public List<ClienteResponseDTO> exibirClientesAtivos() {
        return clienteRepository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ClienteResponseDTO buscarClientePorCpf(String cpf) {
        var cliente = buscarClientePorCpfEAtivoTrue(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize("hasRole('GERENTE')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteAtualizadoDTO dto) {
        var cliente = buscarClientePorCpfEAtivoTrue(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public void deletarCliente(String cpf) {
        var cliente = buscarClientePorCpfEAtivoTrue(cpf);
        cliente.setAtivo(false);
        cliente.getContas().forEach(
                conta -> conta.setAtiva(false)
        );
        clienteRepository.save(cliente);
    }
}
