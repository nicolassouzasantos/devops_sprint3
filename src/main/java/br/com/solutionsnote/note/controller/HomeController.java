package br.com.solutionsnote.note.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Página inicial
     * - Se o usuário estiver logado, manda para /automoveis-ui
     * - Caso contrário, envia para tela de login
     */
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/automoveis-ui";
        }
        return "redirect:/login";
    }

    /**
     * Página de login personalizada (login.html)
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }
}