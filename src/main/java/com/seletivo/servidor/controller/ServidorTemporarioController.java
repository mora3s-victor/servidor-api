package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.ServidorTemporarioDTO;
import com.seletivo.servidor.dto.ServidorTemporarioUnidadeDTO;
import com.seletivo.servidor.service.ServidorTemporarioService;
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
@RequestMapping("/api/servidores-temporarios")
@Tag(name = "Servidor Temporário", description = "Endpoints para gerenciamento de servidores temporários")
public class ServidorTemporarioController {

    @Autowired
    private ServidorTemporarioService servidorTemporarioService;

    @GetMapping
    @Operation(
        summary = "Listar todos os servidores temporários",
        description = "Retorna uma lista paginada de todos os servidores temporários cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorTemporarioDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(servidorTemporarioService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar servidor temporário por ID",
        description = "Retorna os dados de um servidor temporário específico"
    )
    @ApiResponse(responseCode = "200", description = "Servidor encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<ServidorTemporarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servidorTemporarioService.findById(id));
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(
        summary = "Listar servidores por unidade",
        description = "Retorna uma lista paginada de servidores temporários de uma unidade específica"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorTemporarioUnidadeDTO>> findByUnidade(
            @PathVariable Long unidadeId, Pageable pageable) {
        return ResponseEntity.ok(servidorTemporarioService.findByUnidade(unidadeId, pageable));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Buscar servidores por nome",
        description = "Retorna uma lista paginada de servidores temporários que correspondem ao nome informado"
    )
    @ApiResponse(responseCode = "200", description = "Servidores encontrados com sucesso")
    public ResponseEntity<Page<ServidorTemporarioDTO>> findByNome(
            @RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(servidorTemporarioService.findByNome(nome, pageable));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo servidor temporário",
        description = "Cadastra um novo servidor temporário no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Servidor criado com sucesso")
    public ResponseEntity<ServidorTemporarioDTO> create(@RequestBody ServidorTemporarioDTO dto) {
        return ResponseEntity.ok(servidorTemporarioService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar servidor temporário",
        description = "Atualiza os dados de um servidor temporário existente"
    )
    @ApiResponse(responseCode = "200", description = "Servidor atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<ServidorTemporarioDTO> update(
            @PathVariable Long id, @RequestBody ServidorTemporarioDTO dto) {
        return ResponseEntity.ok(servidorTemporarioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir servidor temporário",
        description = "Remove um servidor temporário do sistema"
    )
    @ApiResponse(responseCode = "204", description = "Servidor excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servidorTemporarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/foto")
    @Operation(
        summary = "Upload de foto",
        description = "Faz o upload de uma foto para um servidor temporário"
    )
    @ApiResponse(responseCode = "200", description = "Foto enviada com sucesso")
    @ApiResponse(responseCode = "404", description = "Servidor não encontrado")
    public ResponseEntity<Void> uploadFoto(
            @PathVariable Long id, @RequestParam("foto") MultipartFile foto) throws Exception {
        servidorTemporarioService.uploadFoto(id, foto);
        return ResponseEntity.ok().build();
    }
} 
