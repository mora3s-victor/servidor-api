package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.CriarUsuarioDTO;
import com.seletivo.servidor.dto.UsuarioDTO;
import com.seletivo.servidor.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(
        summary = "Listar usuários",
        description = "Retorna todos os usuários cadastrados no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping
    @Operation(
        summary = "Criar usuário",
        description = "Cria um novo usuário no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody CriarUsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.criar(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo) {
        usuarioService.alterarStatus(id, ativo);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Long id,
            @RequestParam String novaSenha) {
        usuarioService.alterarSenha(id, novaSenha);
        return ResponseEntity.ok().build();
    }
} 
