package com.projetjsp.demoprojetjsp.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetjsp.demoprojetjsp.models.Frais;
import com.projetjsp.demoprojetjsp.repository.FraisRepository;

@Service
public class FraisService {
    @Autowired
    private FraisRepository fraisRepository;
    @Autowired
    private FraisRepository repo;

    public List<Frais> getAllFrais() {
        return fraisRepository.findAll();
    }

    // -----------rechercher frais--------------//
    public Optional<Frais> findById(String idfrais) {
        return fraisRepository.findById(idfrais);
    }

    public Frais saveFrais(Frais frais) {
        return fraisRepository.save(frais);
    }

    public void deleteFrais(String idfrais) throws NoSuchElementException {
        Frais frais = repo.findById(idfrais).orElseThrow(() -> new NoSuchElementException("frais not found"));
        repo.delete(frais);
    }
}
