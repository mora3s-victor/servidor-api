package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.UnidadeDTO;
import com.seletivo.servidor.service.UnidadeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Endpoints para gerenciamento de unidades")
@SecurityRequirement(name = "bearerAuth")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    @Operation(
        summary = "Listar unidades",
        description = "Retorna todas as unidades cadastradas no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de unidades retornada com sucesso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UnidadeDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(unidadeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar unidade por ID",
        description = "Retorna uma unidade específica com base no ID fornecido"
    )
    @ApiResponse(responseCode = "200", description = "Unidade encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.findById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar unidade",
        description = "Cria uma nova unidade no sistema"
    )
    @ApiResponse(responseCode = "201", description = "Unidade criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeDTO> create(@RequestBody UnidadeDTO dto) {
        UnidadeDTO created = unidadeService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar unidade",
        description = "Atualiza os dados de uma unidade existente"
    )
    @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Unidade não encontrada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UnidadeDTO> update(@PathVariable Long id, @RequestBody UnidadeDTO dto) {
        return ResponseEntity.ok(unidadeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 
