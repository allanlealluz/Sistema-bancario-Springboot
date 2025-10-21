package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDto;
import com.senai.conta_bancaria.application.dto.ClienteResponseDto;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.exception.ContaDeMesmoTipoException;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {
    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public ClienteResponseDto registrarCliente(ClienteRegistroDto dto) {
        Cliente clienteRegistrado = repository // verifica se o cliente já existe
                .findByCpfAndAtivoTrue(dto.cpf())
                .orElseGet( // se não existir, cria um novo
                        () -> repository.save(dto.toEntity())
                );
        List<Conta> contas = clienteRegistrado.getContas();
        Conta novaConta = dto.conta().toEntity(clienteRegistrado);

        boolean temMesmoTipo = contas // verifica se o cliente já tem uma conta do mesmo tipo
                .stream()
                .anyMatch(c -> c.getTipo().equals(novaConta.getTipo()) && c.isAtivo());
        if (temMesmoTipo)
            throw new ContaDeMesmoTipoException(novaConta.getTipo());

        clienteRegistrado.getContas().add(novaConta);
        return ClienteResponseDto.fromEntity(repository.save(clienteRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> listarTodosOsClientes() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(ClienteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDto buscarCliente(Long cpf) {
        return ClienteResponseDto.fromEntity(procurarClienteAtivo(cpf));
    }

    // UPDATE
    public ClienteResponseDto atualizarCliente(Long cpf, ClienteAtualizacaoDto dto) {
        Cliente cliente = procurarClienteAtivo(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        return ClienteResponseDto.fromEntity(repository.save(cliente));
    }

    // DELETE
    public void apagarCliente(Long cpf) {
        Cliente cliente = procurarClienteAtivo(cpf);

        cliente.setAtivo(false);
        cliente.getContas()
                .forEach(c -> c.setAtivo(false));

        repository.save(cliente);
    }

    // Mét0do auxiliar para as requisições
    private Cliente procurarClienteAtivo(Long cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("cliente"));
    }
}