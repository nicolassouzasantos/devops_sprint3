package br.com.solutionsnote.note.controller;

import br.com.solutionsnote.note.model.Operador;
import br.com.solutionsnote.note.repository.OperadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operadores")
public class OperadorController {

    @Autowired
    private OperadorRepository repository;

    @PostMapping
    public Operador salvar(@RequestBody @Valid Operador operador) {
        // Sem PasswordEncoder: salva a senha como veio (apenas para smoke test)
        // Se quiser, depois trocamos por hash (BCrypt) usando uma lib sem Spring Security.

        // Papel "sem Security" é só informativo; deixe algo neutro
        if (operador.getPapel() == null || operador.getPapel().isBlank()) {
            operador.setPapel("USER");
        }
        return repository.save(operador);
    }

    @GetMapping
    public List<Operador> listar() {
        return repository.findAll();
    }
}
