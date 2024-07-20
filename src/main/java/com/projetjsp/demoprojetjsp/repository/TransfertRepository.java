package com.projetjsp.demoprojetjsp.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projetjsp.demoprojetjsp.models.Transfert;

public interface TransfertRepository extends JpaRepository<Transfert, String> {
    @Query("SELECT t FROM Transfert t WHERE DATE(t.date) = :date")
    List<Transfert> findByDateOnly(@Param("date") Date date);

    List<Transfert> findByEnvoyeur_Numtel(String numtel);

}
