package com.seletivo.servidor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

@Data
public class EnderecoDTO {
    private Long id;
    
    @NotBlank(message = "Tipo de logradouro é obrigatório")
    private String tipoLogradouro;
    
    @NotBlank(message = "Logradouro é obrigatório")
    private String logradouro;
    
    @NotNull(message = "Número é obrigatório")
    private Integer numero;
    
    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;
    
    @Valid
    @NotNull(message = "Cidade é obrigatória")
    private CidadeDTO cidade;
} 
