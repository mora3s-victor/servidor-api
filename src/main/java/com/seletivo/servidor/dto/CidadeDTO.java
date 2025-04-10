package com.seletivo.servidor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CidadeDTO {
    private Long id;
    
    @NotBlank(message = "Nome da cidade é obrigatório")
    private String nome;
    
    @NotBlank(message = "UF é obrigatória")
    @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres")
    private String uf;
} 
