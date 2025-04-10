package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.ServidorEfetivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivo, Long> {
    
    @Query("SELECT se FROM ServidorEfetivo se " +
           "JOIN se.pessoa p " +
           "JOIN Lotacao l ON l.pessoa = p " +
           "WHERE l.unidade.id = :unidadeId " +
           "AND l.dataRemocao IS NULL")
    Page<ServidorEfetivo> findByUnidadeId(@Param("unidadeId") Long unidadeId, Pageable pageable);

    @Query("SELECT se FROM ServidorEfetivo se " +
           "JOIN se.pessoa p " +
           "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<ServidorEfetivo> findByPessoaNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
} 
