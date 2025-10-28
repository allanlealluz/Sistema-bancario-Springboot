package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Gerente;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GerenteRepository extends JpaRepository<Gerente, String> {
    Optional<Gerente> findByCpfAndAtivoTrue(@NotNull @Positive String cpf);
    Optional<Gerente> findByEmail(String email);
    List<Gerente> findAllByAtivoTrue();
}