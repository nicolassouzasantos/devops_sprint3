package br.com.solutionsnote.note.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "OPERADOR")
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Column(unique = true)
    private String login;

    @NotBlank
    @JsonIgnore // evita expor a senha no GET
    private String senha;

    @Column(name = "PAPEL", nullable = false)
    private String papel = "ROLE_USER"; // ROLE_USER ou ROLE_ADMIN

    @Column(name = "HABILITADO", nullable = false)
    private boolean habilitado = true;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
}
