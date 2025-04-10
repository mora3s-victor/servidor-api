package com.seletivo.servidor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeEnderecoDTO {
    private Long unidadeId;
    private String unidadeNome;
    private String unidadeSigla;
    private EnderecoDTO endereco;
    
    public Long getUnidadeId() {
        return unidadeId;
    }
    
    public void setUnidadeId(Long unidadeId) {
        this.unidadeId = unidadeId;
    }
    
    public String getUnidadeNome() {
        return unidadeNome;
    }
    
    public void setUnidadeNome(String unidadeNome) {
        this.unidadeNome = unidadeNome;
    }
    
    public String getUnidadeSigla() {
        return unidadeSigla;
    }
    
    public void setUnidadeSigla(String unidadeSigla) {
        this.unidadeSigla = unidadeSigla;
    }
    
    public EnderecoDTO getEndereco() {
        return endereco;
    }
    
    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }
} 
