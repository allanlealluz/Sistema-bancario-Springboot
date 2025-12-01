package com.conta_bancaria.domain.repository;

import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.CodigoAutenticacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoAutenticacaoRepository extends JpaRepository<CodigoAutenticacao, String> {
    Optional<CodigoAutenticacao> findTopByClienteOrderByExpiraEmDesc(Cliente cliente);
}
