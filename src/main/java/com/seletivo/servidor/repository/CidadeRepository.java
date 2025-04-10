package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Optional<Cidade> findByNomeAndUf(String nome, String uf);
} 
