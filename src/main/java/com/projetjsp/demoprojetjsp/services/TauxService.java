package com.projetjsp.demoprojetjsp.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetjsp.demoprojetjsp.models.Taux;
import com.projetjsp.demoprojetjsp.repository.TauxRepository;

@Service
public class TauxService {
    @Autowired
    private TauxRepository tauxRepository;
    @Autowired
    private TauxRepository repo;

    // ------------Lister tous les taux disponible entre deux pays-------------//
    public List<Taux> getAllTaux() {
        return tauxRepository.findAll();
    }

    // ---------------rechercher taux à partir de son id=idtaux----------------//
    public Optional<Taux> getTauxById(String idtaux) {
        return tauxRepository.findById(idtaux);
    }

    // ---------------enregistrer taux dans la table taux----------------//
    public Taux saveTaux(Taux taux) {
        return tauxRepository.save(taux);
    }

    public Optional<Taux> findById(String idtaux) {
        return tauxRepository.findById(idtaux);
    }

    // ---------------Supprimer taux----------------//
    public void deleteTaux(String idtaux) throws NoSuchElementException {
        Taux taux = repo.findById(idtaux).orElseThrow(() -> new NoSuchElementException("taux not found"));
        repo.delete(taux);
    }

}
