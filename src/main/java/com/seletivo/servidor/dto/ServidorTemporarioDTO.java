package com.seletivo.servidor.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
public class ServidorTemporarioDTO {
    private Long id;
    
    // Dados pessoais
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;
    
    @NotBlank(message = "Sexo é obrigatório")
    private String sexo;
    
    private String mae;
    private String pai;
    
    // Dados funcionais
    @NotNull(message = "Data de admissão é obrigatória")
    private LocalDate dataAdmissao;
    
    private LocalDate dataDemissao;
    
    @NotNull(message = "ID da unidade é obrigatório")
    private Long unidadeId;
    
    @NotBlank(message = "Portaria de lotação é obrigatória")
    private String portariaLotacao;
    
    // Endereços
    private List<EnderecoDTO> enderecos;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<FotoPessoaDTO> fotos;
} 
