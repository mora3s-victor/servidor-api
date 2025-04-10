package com.seletivo.servidor.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FotoPessoaDTO {
    private Long id;
    private LocalDate data;
    private String bucket;
    private String hash;
    private String url;
} 
