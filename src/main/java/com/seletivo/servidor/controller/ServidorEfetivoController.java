package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.ServidorEfetivoDTO;
import com.seletivo.servidor.dto.ServidorEfetivoUnidadeDTO;
import com.seletivo.servidor.service.ServidorEfetivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/servidores-efetivos")
@Tag(name = "Servidor Efetivo", description = "Endpoints para gerenciamento de servidores efetivos")
public class ServidorEfetivoController {

    @Autowired
    private ServidorEfetivoService servidorEfetivoService;

    @GetMapping
    @Operation(
        summary = "Listar todos os servidores efetivos",
        description = "Retorna uma lista paginada de todos os servidores efetivos cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorEfetivoDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar servidor efetivo por ID",
        description = "Retorna os dados de um servidor efetivo específico"
    )
    @ApiResponse(responseCode = "200", description = "Servidor encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<ServidorEfetivoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorEfetivoService.findById(id));
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(
        summary = "Listar servidores por unidade",
        description = "Retorna uma lista paginada de servidores efetivos de uma unidade específica"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorEfetivoUnidadeDTO>> findByUnidade(
            @PathVariable Long unidadeId, Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findByUnidade(unidadeId, pageable));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Buscar servidores por nome",
        description = "Retorna uma lista paginada de servidores efetivos que correspondem ao nome informado"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorEfetivoDTO>> findByNome(
            @RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(servidorEfetivoService.findByNome(nome, pageable));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo servidor efetivo",
        description = "Cadastra um novo servidor efetivo no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Servidor criado com sucesso")
    public ResponseEntity<ServidorEfetivoDTO> create(@RequestBody ServidorEfetivoDTO dto) {
        return ResponseEntity.ok(servidorEfetivoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar servidor efetivo",
        description = "Atualiza os dados de um servidor efetivo existente"
    )
    @ApiResponse(responseCode = "200", description = "Servidor atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<ServidorEfetivoDTO> update(
            @PathVariable Long id, @RequestBody ServidorEfetivoDTO dto) {
        return ResponseEntity.ok(servidorEfetivoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir servidor efetivo",
        description = "Remove um servidor efetivo do sistema"
    )
    @ApiResponse(responseCode = "204", description = "Servidor excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servidorEfetivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/foto")
    @Operation(
        summary = "Upload de foto",
        description = "Faz o upload de uma foto para um servidor efetivo"
    )
    @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<Void> uploadFoto(
            @PathVariable Long id, @RequestParam("foto") MultipartFile foto) throws Exception {
        servidorEfetivoService.uploadFoto(id, foto);
        return ResponseEntity.ok().build();
    }
} 
