package com.projetjsp.demoprojetjsp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetjsp.demoprojetjsp.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findByNomContainingIgnoreCaseOrNumtelContainingIgnoreCaseOrMailContainingIgnoreCaseOrSexeContainingIgnoreCaseOrPaysContainingIgnoreCase(
            String nom, String numtel, String mail, String sexe, String pays);

    Client findByNumtel(String numtel);
}
