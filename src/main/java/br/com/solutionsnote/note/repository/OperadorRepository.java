package br.com.solutionsnote.note.repository;

import br.com.solutionsnote.note.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperadorRepository extends JpaRepository<Operador, Long> {
    Optional<Operador> findByLogin(String login);
}