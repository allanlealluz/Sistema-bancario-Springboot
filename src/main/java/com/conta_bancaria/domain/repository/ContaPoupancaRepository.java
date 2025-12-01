package com.conta_bancaria.domain.repository;

import com.conta_bancaria.domain.entity.ContaPoupanca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaPoupancaRepository extends JpaRepository<ContaPoupanca, String> {
}
