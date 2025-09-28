package br.com.solutionsnote.note.controller;

import br.com.solutionsnote.note.model.Operador;
import br.com.solutionsnote.note.repository.OperadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // LISTAR TODOS
    @GetMapping
    public List<Operador> listar() {
        return repository.findAll();
    }

    // OBTER POR ID (para você testar no navegador com GET)
    @GetMapping("/{id}")
    public ResponseEntity<Operador> obter(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CRIAR
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Operador salvar(@RequestBody @Valid Operador operador) {
        // hash da senha
        operador.setSenha(passwordEncoder.encode(operador.getSenha()));
        // papel default
        if (operador.getPapel() == null || operador.getPapel().isBlank()) {
            operador.setPapel("ROLE_USER");
        }
        return repository.save(operador);
    }

    // ATUALIZAR
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Operador> atualizar(@PathVariable Long id,
                                              @RequestBody @Valid Operador operadorAtualizado) {
        return repository.findById(id)
                .map(operador -> {
                    if (operadorAtualizado.getNome() != null) {
                        operador.setNome(operadorAtualizado.getNome());
                    }
                    if (operadorAtualizado.getLogin() != null) {
                        operador.setLogin(operadorAtualizado.getLogin());
                    }
                    if (operadorAtualizado.getSenha() != null && !operadorAtualizado.getSenha().isBlank()) {
                        operador.setSenha(passwordEncoder.encode(operadorAtualizado.getSenha()));
                    }
                    if (operadorAtualizado.getPapel() != null) {
                        operador.setPapel(operadorAtualizado.getPapel());
                    }
                    Operador salvo = repository.save(operador);
                    return ResponseEntity.ok(salvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
