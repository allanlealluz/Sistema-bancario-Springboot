package com.conta_bancaria.domain.repository;

import com.conta_bancaria.domain.entity.Conta;
import com.conta_bancaria.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    Optional<Pagamento> findByBoleto(String boleto);

    Optional<Pagamento> findByConta(Conta conta);
}
