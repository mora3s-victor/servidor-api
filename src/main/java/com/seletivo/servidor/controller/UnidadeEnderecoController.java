package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.UnidadeEnderecoDTO;
import com.seletivo.servidor.service.UnidadeEnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidade-endereco")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Unidade Endereço", description = "Endpoint para consulta de endereço da unidade por servidor")
public class UnidadeEnderecoController {

    private final UnidadeEnderecoService unidadeEnderecoService;

    @GetMapping("/servidor")
    @Operation(
        summary = "Buscar endereço da unidade por servidor",
        description = "Retorna o endereço da unidade onde o servidor está lotado"
    )
    @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<List<UnidadeEnderecoDTO>> findByServidorNome(
            @RequestParam String nome) {
        List<UnidadeEnderecoDTO> result = unidadeEnderecoService.findByServidorNome(nome);
        return ResponseEntity.ok(result);
    }
} 
