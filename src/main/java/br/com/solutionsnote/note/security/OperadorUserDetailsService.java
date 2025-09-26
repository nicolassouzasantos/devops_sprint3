package br.com.solutionsnote.note.security;

import br.com.solutionsnote.note.model.Operador;
import br.com.solutionsnote.note.repository.OperadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperadorUserDetailsService implements UserDetailsService {

    @Autowired
    private OperadorRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Operador op = repo.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(op.getLogin())
                .password(op.getSenha())
                .authorities(List.of(new SimpleGrantedAuthority(op.getPapel())))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!op.isHabilitado())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // força padrão 10, ok
    }
}