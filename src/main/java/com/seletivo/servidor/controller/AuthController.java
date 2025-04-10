package com.seletivo.servidor.controller;

import com.seletivo.servidor.security.auth.AuthenticationRequest;
import com.seletivo.servidor.security.auth.AuthenticationResponse;
import com.seletivo.servidor.security.auth.AuthenticationService;
import com.seletivo.servidor.security.auth.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/login")
    @Operation(
        summary = "Login",
        description = "Autentica um usuário e retorna os tokens de acesso e refresh"
    )
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    @Operation(
        summary = "Renovar token",
        description = "Gera um novo token de acesso usando um token de refresh válido"
    )
    @ApiResponse(responseCode = "200", description = "Token renovado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token de refresh inválido ou expirado")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
} 
