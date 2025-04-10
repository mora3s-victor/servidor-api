package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.ServidorTemporario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporario, Long> {
    
    @Query("SELECT st FROM ServidorTemporario st " +
           "JOIN st.pessoa p " +
           "JOIN Lotacao l ON l.pessoa = p " +
           "WHERE l.unidade.id = :unidadeId " +
           "AND l.dataRemocao IS NULL " +
           "ORDER BY p.nome")
    Page<ServidorTemporario> findByUnidadeId(@Param("unidadeId") Long unidadeId, Pageable pageable);

    @Query("SELECT st FROM ServidorTemporario st " +
           "JOIN st.pessoa p " +
           "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
           "ORDER BY p.nome")
    Page<ServidorTemporario> findByPessoaNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
} 
