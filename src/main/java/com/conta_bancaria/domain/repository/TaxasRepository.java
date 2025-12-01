package com.conta_bancaria.domain.repository;

import com.conta_bancaria.domain.entity.Taxa;
import com.conta_bancaria.domain.enums.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxasRepository extends JpaRepository<Taxa, String> {
    List<Taxa> findByTipoPagamento(TipoPagamento tipoPagamento);
}
