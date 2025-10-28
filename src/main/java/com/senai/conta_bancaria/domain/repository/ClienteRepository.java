package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Cliente;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    List<Cliente> findAllByAtivoTrue();

    Optional<Cliente> findByCpfAndAtivoTrue(@NotNull(message = "O CPF não pode ser nulo.") @Positive(message = "O CPF não pode ser negativo.") @Max(value = 99999999999L, message = "O CPF deve ter até 11 digitos.") String cpf);
}
