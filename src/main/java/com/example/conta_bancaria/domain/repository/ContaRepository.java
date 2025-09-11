package com.example.conta_bancaria.domain.repository;

import com.example.conta_bancaria.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByClienteIdAndNumero(Long clienteId, String numero);

}
