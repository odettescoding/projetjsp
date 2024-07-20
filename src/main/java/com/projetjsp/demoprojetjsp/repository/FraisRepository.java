package com.projetjsp.demoprojetjsp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projetjsp.demoprojetjsp.models.Frais;

public interface FraisRepository extends JpaRepository<Frais, String> {
    // // BigDecimal findFraisByMontant(@Param("montant") BigDecimal montant);
    // Frais findFraisByMontant(BigDecimal montant);

    // List<Frais> findAll();

    @Query("SELECT f FROM Frais f WHERE :montant BETWEEN f.montant1 AND f.montant2")
    Optional<Frais> findFraisByMontant(@Param("montant") double montant);
}
