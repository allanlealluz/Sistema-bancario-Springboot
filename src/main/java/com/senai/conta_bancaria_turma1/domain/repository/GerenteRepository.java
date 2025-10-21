package com.senai.conta_bancaria_turma1.domain.repository;
import java.util.Optional;
import com.senai.conta_bancaria_turma1.domain.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GerenteRepository extends JpaRepository<Gerente, String> {
    Optional<Gerente> findByEmail(String email);
}