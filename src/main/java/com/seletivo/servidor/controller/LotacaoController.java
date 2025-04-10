package com.seletivo.servidor.controller;

import com.seletivo.servidor.dto.LotacaoDTO;
import com.seletivo.servidor.dto.LotacaoUpdateDTO;
import com.seletivo.servidor.service.LotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotacoes")
@Tag(name = "Lotação", description = "Endpoints para gerenciamento de lotações de servidores")
public class LotacaoController {

    @Autowired
    private LotacaoService lotacaoService;

    @GetMapping
    @Operation(
        summary = "Listar todas as lotações",
        description = "Retorna uma lista paginada de todas as lotações cadastradas no sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lotações encontradas com sucesso")
    public ResponseEntity<Page<LotacaoDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(lotacaoService.findAll(pageable));
    }

    @PostMapping
    @Operation(
        summary = "Criar nova lotação",
        description = "Cria uma nova lotação para um servidor em uma unidade específica"
    )
    @ApiResponse(responseCode = "200", description = "Lotação criada com sucesso")
    public ResponseEntity<LotacaoDTO> create(@RequestBody LotacaoDTO dto) {
        return ResponseEntity.ok(lotacaoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar lotação",
        description = "Atualiza os dados de uma lotação existente através do seu ID"
    )
    @ApiResponse(responseCode = "200", description = "Lotação atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Lotação não encontrada")
    public ResponseEntity<LotacaoDTO> update(
            @PathVariable Long id, @RequestBody LotacaoUpdateDTO dto) {
        return ResponseEntity.ok(lotacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir lotação",
        description = "Remove uma lotação existente através do seu ID"
    )
    @ApiResponse(responseCode = "204", description = "Lotação excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Lotação não encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lotacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 
