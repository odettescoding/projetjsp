package com.projetjsp.demoprojetjsp.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetjsp.demoprojetjsp.models.Client;
import com.projetjsp.demoprojetjsp.models.ClientDto;
import com.projetjsp.demoprojetjsp.repository.ClientRepository;
import com.projetjsp.demoprojetjsp.services.ClientsService;

import jakarta.validation.Valid;

@Controller
public class ClientsController {
    @Autowired
    private ClientsService repo;

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private ClientRepository repositories;

    // --------------AFFICHE LA LISTE DES CLIENTS----------------//
    @GetMapping("/clients")
    public String ShowClientList(Model model) {
        List<Client> clients = repo.getAllClients();
        // Ligne de log pour vérifier les données
        System.out.println("Clients: " + clients);
        if (clients != null) {
            clients.forEach(client -> System.out.println(client));
        }
        model.addAttribute("clients", clients);
        return "clients/index";
    }

    // ----------------------------AJOUTER CLIENT------------------------------//
    @GetMapping("/ajouterclient")
    public String showCreatePage(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);
        return "clients/AjouterClient";
    }

    @PostMapping("/ajouterclient")
    public String createClient(
            @Valid @ModelAttribute("clientDto") ClientDto clientDto,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clients/AjouterClient";
        }

        // Vérifier si le numéro de téléphone existe déjà
        Optional<Client> existingClient = repo.findById(clientDto.getNumtel());
        if (existingClient.isPresent()) {
            model.addAttribute("errorMessage", "Client existe déjà avec ce numéro de téléphone.");
            return "clients/AjouterClient";
        }

        Client client = new Client();
        client.setNumtel(clientDto.getNumtel());
        client.setNom(clientDto.getNom());
        client.setSexe(clientDto.getSexe());
        client.setPays(clientDto.getPays());
        client.setSolde(clientDto.getSolde());
        client.setMail(clientDto.getMail());
        repo.saveClient(client);
        redirectAttributes.addFlashAttribute("successMessage", "Client ajouté avec succès.");

        return "redirect:/clients";
    }

    // --------------------------MODIFIER CLIENT----------------------//

    @GetMapping("/modifierclient")
    public String showEditPage(Model model, @RequestParam String numtel) {
        try {
            Client client = repo.findById(numtel).get();
            model.addAttribute("client", client);

            ClientDto clientDto = new ClientDto();
            clientDto.setNumtel(client.getNumtel());
            clientDto.setNom(client.getNom());
            clientDto.setSexe(client.getSexe());
            clientDto.setPays(client.getPays());
            clientDto.setSolde(client.getSolde());
            clientDto.setMail(client.getMail());
            model.addAttribute("clientDto", clientDto);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/clients";
        }
        return "clients/ModifierClient";
    }

    @PostMapping("/modifierclient")
    public String updateClient(Model model,
            @RequestParam String numtel,
            @Valid @ModelAttribute ClientDto clientDto, RedirectAttributes redirectAttributes,
            BindingResult result) {
        try {
            Client client = repo.findById(numtel).get();
            model.addAttribute("client", client);
            if (result.hasErrors()) {
                return "clients/ModifierClient";
            }

            client.setNumtel(clientDto.getNumtel());
            client.setNom(clientDto.getNom());
            client.setSexe(clientDto.getSexe());
            client.setPays(clientDto.getPays());
            client.setSolde(clientDto.getSolde());
            client.setMail(clientDto.getMail());
            repo.saveClient(client);
            redirectAttributes.addFlashAttribute("updateMessage", "Client modifié avec succès.");

        } catch (Exception ex) {
            System.out.println("Exception :" + ex.getMessage());
        }

        return "redirect:/clients";
    }

    @GetMapping("/supprimerclient")
    public String deleteClient(@RequestParam String numtel, RedirectAttributes redirectAttributes) {
        try {
            Client client = repositories.findById(numtel)
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));
            clientsService.deleteClient(numtel);
            redirectAttributes.addFlashAttribute("successdeleteMessage", "Client supprimé avec succès.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Client introuvable.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Une erreur est survenue: impossible de supprimer cet client car il a reference au transfert "
            // + ex.getMessage()
            );
        }

        return "redirect:/clients";
    }

    // -----------------------------------RECHERCHE-------------------------------//

    @GetMapping("/rechercherclient")
    public String searchClient(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Client> searchResults;
        if (query != null && !query.isEmpty()) {
            // Effectuer la recherche en utilisant le service approprié (clientsService)
            searchResults = clientsService.searchClientsByNameOrNumtelOrMailOrSexeOrPays(query);
        } else {
            // Si aucun critère de recherche n'est fourni, afficher tous les clients
            searchResults = clientsService.getAllClients();
        }
        // Passer les résultats de la recherche au modèle pour les afficher dans la vue
        model.addAttribute("clients", searchResults);
        return "clients/index";
    }

    // ---------------------PAGE D'ACCUEIL----------------------//
    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "home";
    }

}
