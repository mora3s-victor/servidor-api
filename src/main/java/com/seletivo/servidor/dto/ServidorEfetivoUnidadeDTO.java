package com.seletivo.servidor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServidorEfetivoUnidadeDTO {
    private Long id;
    private String nome;
    private Integer idade;
    private String unidadeNome;
    private String fotoUrl;
} 
