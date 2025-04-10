package com.seletivo.servidor.dto;

import lombok.Data;

@Data
public class EnderecoFuncionalDTO {
    private String nomeServidor;
    private String unidadeNome;
    private String tipoLogradouro;
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String uf;
} 
