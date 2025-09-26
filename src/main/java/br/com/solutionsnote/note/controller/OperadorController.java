package br.com.solutionsnote.note.controller;

import br.com.solutionsnote.note.model.Operador;
import br.com.solutionsnote.note.repository.OperadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operadores")
public class OperadorController {

    @Autowired
    private OperadorRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Operador salvar(@RequestBody @Valid Operador operador) {
        // hash da senha
        operador.setSenha(passwordEncoder.encode(operador.getSenha()));
        // papel default se vier vazio
        if (operador.getPapel() == null || operador.getPapel().isBlank()) {
            operador.setPapel("ROLE_USER");
        }
        return repository.save(operador);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Operador> listar() {
        return repository.findAll();
    }
}
