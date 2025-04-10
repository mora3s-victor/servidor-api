package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_id")
    private Long id;

    @Column(name = "end_tipo_logradouro", length = 50)
    private String tipoLogradouro;

    @Column(name = "end_logradouro", length = 200)
    private String logradouro;

    @Column(name = "end_numero")
    private Integer numero;

    @Column(name = "end_bairro", length = 100)
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "cid_id")
    private Cidade cidade;

    @OneToMany(mappedBy = "endereco")
    private List<PessoaEndereco> pessoasEnderecos;

    @OneToMany(mappedBy = "endereco")
    private List<UnidadeEndereco> unidadesEnderecos;
} 
