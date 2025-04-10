package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
} 
