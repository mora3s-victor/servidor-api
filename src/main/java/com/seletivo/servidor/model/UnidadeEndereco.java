package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unidade_endereco")
public class UnidadeEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unidade_endereco_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unid_id")
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "end_id")
    private Endereco endereco;
} 
