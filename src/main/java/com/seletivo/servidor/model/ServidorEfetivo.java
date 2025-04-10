package com.seletivo.servidor.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "servidor_efetivo")
public class ServidorEfetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servidor_efetivo_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pes_id")
    private Pessoa pessoa;

    @Column(name = "se_matricula", length = 20)
    private String matricula;
} 
