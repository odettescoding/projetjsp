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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetjsp.demoprojetjsp.models.Taux;
import com.projetjsp.demoprojetjsp.models.TauxDto;
import com.projetjsp.demoprojetjsp.repository.TauxRepository;
import com.projetjsp.demoprojetjsp.services.TauxService;

import jakarta.validation.Valid;

@Controller
public class TauxController {
    @Autowired
    private TauxService repo;
    @Autowired
    private TauxService tauxService;

    @Autowired
    private TauxRepository repositories;

    // ------------------------LISTE TAUX--------------------------//

    @GetMapping("/taux")
    public String showTauxList(Model model) {
        List<Taux> tauxList = repo.getAllTaux();
        model.addAttribute("tauxList", tauxList);
        return "taux/taux";
    }

    // -----------------CHOISIR TAUX SELON LE PAYS SELECTIONNER-----------------//

    @GetMapping("/api/taux/find")
    @ResponseBody
    public Taux getTaux(@RequestParam String pays1, @RequestParam String pays2) {
        String idtaux = pays1 + pays2;
        Optional<Taux> taux = repo.getTauxById(idtaux);
        return taux.orElse(null);

    }

    // --------------------AJOUTER TAUX---------------------//

    @GetMapping("/ajoutertaux")
    public String showcreatetaux(Model model) {
        TauxDto tauxDto = new TauxDto();
        model.addAttribute("tauxDto", tauxDto);
        return "taux/ajoutertaux";
    }

    @PostMapping("/ajoutertaux")
    public String createTaux(@Valid @ModelAttribute("tauxDto") TauxDto tauxDto,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {

            return "taux/AjouterTaux";
        }

        Taux taux = new Taux(tauxDto.getIdtaux(), tauxDto.getMontant1(),
                tauxDto.getMontant2());

        repo.saveTaux(taux);
        redirectAttributes.addFlashAttribute("successMessage", "Taux ajouté avec succès.");

        return "redirect:/taux";
    }

    // ----------------------MODIFIER TAUX----------------------------//
    @GetMapping("/modifiertaux")
    public String showEditPage(Model model, @RequestParam String idtaux) {
        try {
            Taux taux = repo.findById(idtaux).get();
            model.addAttribute("taux", taux);

            TauxDto tauxDto = new TauxDto();
            tauxDto.setIdtaux(taux.getIdtaux());
            tauxDto.setMontant1(taux.getMontant1());
            tauxDto.setMontant2(taux.getMontant2());

            model.addAttribute("tauxDto", tauxDto);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/taux";
        }
        return "taux/ModifierTaux";
    }

    @PostMapping("/modifiertaux")
    public String updateTaux(Model model,
            @RequestParam String idtaux,
            @Valid @ModelAttribute TauxDto tauxDto, RedirectAttributes redirectAttributes,
            BindingResult result) {
        try {
            Taux taux = repo.findById(idtaux).get();
            model.addAttribute("taux", taux);
            if (result.hasErrors()) {
                return "taux/ModifierTaux";
            }

            taux.setIdtaux(tauxDto.getIdtaux());
            taux.setMontant1(tauxDto.getMontant1());
            taux.setMontant2(tauxDto.getMontant2());
            repo.saveTaux(taux);
            redirectAttributes.addFlashAttribute("updateMessage", "taux modifié avec succès.");

        } catch (Exception ex) {
            System.out.println("Exception :" + ex.getMessage());
        }

        return "redirect:/taux";
    }

    // --------------------------SUPPRIMER TAUX------------------------//

    @GetMapping("/supprimertaux")
    public String deleteClient(@RequestParam String idtaux, RedirectAttributes redirectAttributes) {
        try {
            Taux taux = repositories.findById(idtaux)
                    .orElseThrow(() -> new NoSuchElementException("taux not found"));
            tauxService.deleteTaux(idtaux);
            redirectAttributes.addFlashAttribute("successdeleteMessage", "taux supprimé avec succès.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "taux introuvable.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue: " + ex.getMessage());
        }

        return "redirect:/taux";
    }

}
