package com.projetjsp.demoprojetjsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetjsp.demoprojetjsp.models.Taux;

public interface TauxRepository extends JpaRepository<Taux, String> {
    // Optional<Taux> findByPaysSourceAndPaysDestination(String paysSource, String
    // paysDestination);
    // Taux findByPaysEnvoyeurAndPaysRecepteur(String paysEnvoyeur, String
    // paysRecepteur);
    Taux findByIdtaux(String idtaux);

}
