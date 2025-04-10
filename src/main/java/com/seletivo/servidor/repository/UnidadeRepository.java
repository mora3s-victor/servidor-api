package com.seletivo.servidor.repository;

import com.seletivo.servidor.model.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    
    @Query("SELECT DISTINCT u FROM Unidade u " +
           "LEFT JOIN FETCH u.enderecosUnidade ue " +
           "LEFT JOIN FETCH ue.endereco e " +
           "LEFT JOIN FETCH e.cidade c " +
           "WHERE u.id = :id")
    Optional<Unidade> findByIdWithEnderecos(@Param("id") Long id);

    @Query(value = "SELECT DISTINCT u FROM Unidade u " +
           "LEFT JOIN FETCH u.enderecosUnidade ue " +
           "LEFT JOIN FETCH ue.endereco e " +
           "LEFT JOIN FETCH e.cidade c",
           countQuery = "SELECT COUNT(DISTINCT u) FROM Unidade u")
    Page<Unidade> findAllWithEnderecos(Pageable pageable);
} 
