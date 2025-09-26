package br.com.solutionsnote.note.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Página inicial
     * Sem autenticação: redireciona direto para a UI.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/automoveis-ui";
    }

    /**
     * Rota de /login mantida apenas para compatibilidade.
     * Como não há mais security, redireciona para a UI.
     */
    @GetMapping("/login")
    public String login() {
        return "redirect:/automoveis-ui";
    }
}
