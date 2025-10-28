package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.GerenteAtualizacaoDto;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDto;
import com.senai.conta_bancaria.application.dto.GerenteResponseDto;
import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.exception.EntidadeJaExisteException; // Crie esta exceção
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GerenteService {
    private final GerenteRepository repository;
    private final PasswordEncoder passwordEncoder;

    // CREATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto registrarGerente(GerenteRegistroDto dto) {
        // 1. Verifica se o gerente já existe
        repository.findByCpfAndAtivoTrue(dto.cpf()).ifPresent(g -> {
            throw new EntidadeJaExisteException("Gerente com este CPF já cadastrado.");
        });

        // 2. Cria a entidade
        Gerente gerente = dto.toEntity();

        // 3. CRIPTOGRAFA a senha ANTES de salvar
        gerente.setSenha(passwordEncoder.encode(dto.senha()));

        // 4. Salva (apenas um save)
        return GerenteResponseDto.fromEntity(repository.save(gerente));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<GerenteResponseDto> listarTodosOsGerentes() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(GerenteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto buscarGerente(String cpf) {
        return GerenteResponseDto.fromEntity(procurarGerenteAtivo(cpf));
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN')")
    public GerenteResponseDto atualizarGerente(String cpf, GerenteAtualizacaoDto dto) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setNome(dto.nome());
        gerente.setCpf(dto.cpf());
        gerente.setEmail(dto.email());

        // 🔐 CORREÇÃO DE SEGURANÇA:
        // Verifica se uma nova senha foi fornecida antes de criptografar
        if (dto.senha() != null && !dto.senha().isBlank()) {
            gerente.setSenha(passwordEncoder.encode(dto.senha()));
        }
        // Se a senha no DTO for nula ou vazia, a senha antiga é mantida.

        return GerenteResponseDto.fromEntity(repository.save(gerente));
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN')")
    public void apagarGerente(String cpf) {
        Gerente gerente = procurarGerenteAtivo(cpf);

        gerente.setAtivo(false);

        repository.save(gerente);
    }

    // Mét0do auxiliar para as requisições
    private Gerente procurarGerenteAtivo(String cpf) {
        return repository
                .findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("gerente"));
    }
}