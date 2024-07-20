package com.projetjsp.demoprojetjsp.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetjsp.demoprojetjsp.models.Client;
import com.projetjsp.demoprojetjsp.repository.ClientRepository;

@Service
public class ClientsService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientRepository repo;

    // ---------Lister tout les clients--------------//
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // ----------Enregistrer client dans la base de données ----------//
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // -----------rechercher l'id=numtel client--------------//
    public Optional<Client> findById(String numtel) {
        return clientRepository.findById(numtel);
    }

    // ----------supprimer client à partir de son id=numtel------------//
    public void deleteClient(String numtel) throws NoSuchElementException {
        Client client = repo.findById(numtel).orElseThrow(() -> new NoSuchElementException("Client not found"));
        repo.delete(client);
    }

    // --------recherche client à partir de numtel, nom, sexe,pays,mail ------- //
    public List<Client> searchClientsByNameOrNumtelOrMailOrSexeOrPays(String query) {
        return clientRepository
                .findByNomContainingIgnoreCaseOrNumtelContainingIgnoreCaseOrMailContainingIgnoreCaseOrSexeContainingIgnoreCaseOrPaysContainingIgnoreCase(
                        query, query, query, query, query);
    }

    public Client getClientByNumtel(String numtel) {
        return clientRepository.findByNumtel(numtel);
    }

}
