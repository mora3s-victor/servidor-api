package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "cidade")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid_id")
    private Long id;

    @Column(name = "cid_nome", length = 200)
    private String nome;

    @Column(name = "cid_uf", length = 2)
    private String uf;

    @OneToMany(mappedBy = "cidade")
    private List<Endereco> enderecos;
} 
