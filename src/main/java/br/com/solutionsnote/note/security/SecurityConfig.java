package br.com.solutionsnote.note.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // permite @PreAuthorize nos controllers
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // se usar H2 em dev
                .headers(h -> h.frameOptions(f -> f.disable()))               // idem H2

                .authorizeHttpRequests(auth -> auth
                        // recursos estáticos e páginas públicas
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/error").permitAll()

                        // regras por perfil (exemplo):
                        .requestMatchers("/operadores/**").hasRole("ADMIN")
                        .requestMatchers("/automoveis/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/patios/**").hasAnyRole("USER","ADMIN")

                        // qualquer outra rota precisa estar autenticada
                        .anyRequest().authenticated()
                )

                // Form login padrão do Spring (pode customizar depois com Thymeleaf)
                .formLogin(form -> form
                        .loginPage("/login")           // opcional: crie /login; se não tiver, use .formLogin(Customizer.withDefaults())
                        .permitAll()
                        .defaultSuccessUrl("/", true)  // redireciona após login
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
