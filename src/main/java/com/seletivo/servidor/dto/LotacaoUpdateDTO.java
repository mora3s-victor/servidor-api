package com.seletivo.servidor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotacaoUpdateDTO {
    private Long pessoaId;
    private Long unidadeId;
    private LocalDate dataLotacao;
    private LocalDate dataRemocao;
    private String portaria;
} 
