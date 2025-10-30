package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDto;
import com.senai.conta_bancaria.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {
    Optional<Conta> findByNumeroAndAtivoTrue(Long numero);

    List<Conta> findAllByAtivoTrue();

    Optional<Conta> findFirstByClienteIdAndAtivo(String clienteId, boolean ativo);
}
