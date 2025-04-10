package com.seletivo.servidor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class LotacaoDTO {
    private Long id;
    
    @NotNull(message = "Pessoa é obrigatória")
    private Long pessoaId;
    
    @NotNull(message = "Unidade é obrigatória")
    private Long unidadeId;
    
    @NotNull(message = "Data de lotação é obrigatória")
    private LocalDate dataLotacao;
    
    private LocalDate dataRemocao;
    private String portaria;
    
    // Campos somente leitura
    private String pessoaNome;
    private String unidadeNome;
} 
