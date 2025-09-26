package br.com.solutionsnote.note;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class Hash {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));
        System.out.println(new BCryptPasswordEncoder().encode("user123"));
    }
}