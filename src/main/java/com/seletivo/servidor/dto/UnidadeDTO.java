package com.seletivo.servidor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

@Data
public class UnidadeDTO {
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "Sigla é obrigatória")
    private String sigla;
    
    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoDTO endereco;
} 
