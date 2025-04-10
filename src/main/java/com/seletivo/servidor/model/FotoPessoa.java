package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "foto_pessoa")
public class FotoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fp_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;

    @Column(name = "fp_data")
    private LocalDate data;

    @Column(name = "fp_bucket")
    private String bucket;

    @Column(name = "fp_hash")
    private String hash;
} 
