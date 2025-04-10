package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.UnidadeEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeEnderecoRepository extends JpaRepository<UnidadeEndereco, Long> {
} 
