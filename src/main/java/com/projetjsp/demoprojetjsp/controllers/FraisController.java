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

import com.projetjsp.demoprojetjsp.models.Frais;
import com.projetjsp.demoprojetjsp.models.FraisDto;
import com.projetjsp.demoprojetjsp.repository.FraisRepository;
import com.projetjsp.demoprojetjsp.services.FraisService;

import jakarta.validation.Valid;

@Controller
public class FraisController {

    @Autowired
    private FraisService repo;

    @Autowired
    private FraisRepository repositories;

    @Autowired
    private FraisService fraisService;

    @GetMapping("/frais")
    public String showFraisList(Model model) {
        List<Frais> fraisList = repo.getAllFrais();
        model.addAttribute("fraisList", fraisList);
        return "frais/frais";
    }

    @GetMapping("/ajouterfrais")
    public String showCreatePage(Model model) {
        FraisDto fraisDto = new FraisDto();
        model.addAttribute("fraisDto", fraisDto);
        return "frais/AjouterFrais";
    }

    @PostMapping("/ajouterfrais")
    public String createFrais(
            @Valid @ModelAttribute("fraistDto") FraisDto fraisDto,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "frais/AjouterFrais";
        }

        // Vérifier si le idtaux existe déjà
        Optional<Frais> existingFrais = repo.findById(fraisDto.getIdfrais());
        if (existingFrais.isPresent()) {
            model.addAttribute("errorMessage", "idtaux existe déjà avec ce numéro .");
            return "frais/AjouterFrais";
        }

        Frais frais = new Frais();
        frais.setIdfrais(fraisDto.getIdfrais());
        frais.setMontant1(fraisDto.getMontant1());
        frais.setMontant2(fraisDto.getMontant2());
        frais.setFraisdetransfert(fraisDto.getFraisdetransfert());
        repo.saveFrais(frais);
        redirectAttributes.addFlashAttribute("successMessage", " nouveau frais ajouté avec succès.");

        return "redirect:/frais";
    }

    @GetMapping("/supprimerfrais")
    public String deleteFrais(@RequestParam String idfrais, RedirectAttributes redirectAttributes) {
        try {
            Frais frais = repositories.findById(idfrais)
                    .orElseThrow(() -> new NoSuchElementException("frais not found"));
            fraisService.deleteFrais(idfrais);
            redirectAttributes.addFlashAttribute("successdeleteMessage", "frais supprimé avec succès.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "frais introuvable.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Une erreur est survenue:  " + ex.getMessage());
        }

        return "redirect:/frais";
    }

    @GetMapping("/modifierfrais")
    public String showEditPage(Model model, @RequestParam String idfrais) {
        try {
            Frais frais = repo.findById(idfrais).get();
            model.addAttribute("frais", frais);

            FraisDto fraisDto = new FraisDto();
            fraisDto.setIdfrais(frais.getIdfrais());
            fraisDto.setMontant1(frais.getMontant1());
            fraisDto.setMontant2(frais.getMontant2());
            fraisDto.setFraisdetransfert(frais.getFraisdetransfert());
            model.addAttribute("fraisDto", fraisDto);

        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/frais";
        }
        return "frais/Modifierfrais";
    }

    @PostMapping("/modifierfrais")
    public String updateFrais(Model model,
            @RequestParam String idfrais,
            @Valid @ModelAttribute FraisDto fraisDto, RedirectAttributes redirectAttributes,
            BindingResult result) {
        try {
            Frais frais = repo.findById(idfrais).get();
            model.addAttribute("frais", frais);
            if (result.hasErrors()) {
                return "frais/Modifierfrais";
            }

            frais.setIdfrais(fraisDto.getIdfrais());
            frais.setMontant1(fraisDto.getMontant1());
            frais.setMontant2(fraisDto.getMontant2());
            frais.setFraisdetransfert(fraisDto.getFraisdetransfert());
            repo.saveFrais(frais);
            redirectAttributes.addFlashAttribute("updateMessage", "frais modifié avec succès.");

        } catch (Exception ex) {
            System.out.println("Exception :" + ex.getMessage());
        }

        return "redirect:/frais";
    }

}
