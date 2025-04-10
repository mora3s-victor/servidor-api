package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "servidor_temporario")
public class ServidorTemporario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servidor_temporario_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;

    @Column(name = "st_data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "st_data_demissao")
    private LocalDate dataDemissao;
} 
