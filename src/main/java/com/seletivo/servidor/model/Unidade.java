package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "unidade")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unid_id")
    private Long id;

    @Column(name = "unid_nome", length = 200)
    private String nome;

    @Column(name = "unid_sigla", length = 20)
    private String sigla;

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lotacao> lotacoes = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UnidadeEndereco> enderecosUnidade = new ArrayList<>();
} 
