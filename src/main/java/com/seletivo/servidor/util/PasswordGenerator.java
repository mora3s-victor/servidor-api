package com.seletivo.servidor.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "password";
        String senhaCriptografada = encoder.encode(senha);
        System.out.println("Senha original: " + senha);
        System.out.println("Senha criptografada: " + senhaCriptografada);
    }
} 
