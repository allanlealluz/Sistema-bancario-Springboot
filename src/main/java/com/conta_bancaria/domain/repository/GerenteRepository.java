package com.conta_bancaria.domain.repository;

import com.conta_bancaria.domain.entity.Cliente;
import com.conta_bancaria.domain.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, String> {
    Optional<Gerente> findByCpfAndAtivoTrue(String cpf);
}
