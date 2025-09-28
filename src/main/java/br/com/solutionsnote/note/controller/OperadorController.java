package br.com.solutionsnote.note.controller;

import br.com.solutionsnote.note.model.Operador;
import br.com.solutionsnote.note.repository.OperadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operadores")
public class OperadorController {

    @Autowired
    private OperadorRepository repository;

    @PostMapping
    public Operador salvar(@RequestBody @Valid Operador operador) {
        return repository.save(operador);
    }

    @GetMapping
    public List<Operador> listar() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Operador> atualizar(@PathVariable Long id, @RequestBody @Valid Operador operadorAtualizado) {
        return repository.findById(id)
                .map(operadorExistente -> {
                    operadorExistente.setNome(operadorAtualizado.getNome());
                    operadorExistente.setLogin(operadorAtualizado.getLogin());

                    if (operadorAtualizado.getSenha() != null && !operadorAtualizado.getSenha().isBlank()) {
                        operadorExistente.setSenha(operadorAtualizado.getSenha());
                    }
                    Operador salvo = repository.save(operadorExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(operador -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().build(); // HTTP 204
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
