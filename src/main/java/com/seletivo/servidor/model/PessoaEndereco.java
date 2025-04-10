package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoa_endereco")
public class PessoaEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pessoa_endereco_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "end_id")
    private Endereco endereco;
} 
