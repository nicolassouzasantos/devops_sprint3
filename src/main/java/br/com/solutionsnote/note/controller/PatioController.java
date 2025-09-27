package br.com.solutionsnote.note.controller;

import br.com.solutionsnote.note.model.Patio;
import br.com.solutionsnote.note.repository.PatioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patios")
public class PatioController {

    @Autowired
    private PatioRepository repository;

    @PostMapping
    public Patio salvar(@RequestBody @Valid Patio patio) {
        return repository.save(patio);
    }

    @GetMapping
    public List<Patio> listar() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patio> atualizar(@PathVariable Long id,
                                           @RequestBody @Valid Patio patioAtualizado) {
        return repository.findById(id)
                .map(patio -> {
                    patio.setNome(patioAtualizado.getNome());
                    patio.setEndereco(patioAtualizado.getEndereco());
                    Patio salvo = repository.save(patio);
                    return ResponseEntity.ok(salvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // <- use Supplier aqui
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(patio -> {
                    repository.delete(patio); // (ou deleteById(id))
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
