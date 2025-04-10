package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pes_id")
    private Long id;

    @Column(name = "pes_nome", length = 200, nullable = false)
    private String nome;

    @Column(name = "pes_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "pes_sexo", length = 1)
    private String sexo;

    @Column(name = "pes_mae", length = 200)
    private String mae;

    @Column(name = "pes_pai", length = 200)
    private String pai;

    @OneToMany(mappedBy = "pessoa")
    private List<PessoaEndereco> enderecos;

    @OneToMany(mappedBy = "pessoa")
    private List<Lotacao> lotacoes;

    @OneToOne(mappedBy = "pessoa")
    private ServidorEfetivo servidorEfetivo;

    @OneToOne(mappedBy = "pessoa")
    private ServidorTemporario servidorTemporario;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoPessoa> fotos = new ArrayList<>();
} 
