package com.projetjsp.demoprojetjsp.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projetjsp.demoprojetjsp.models.Client;
import com.projetjsp.demoprojetjsp.models.Transfert;
import com.projetjsp.demoprojetjsp.models.TransfertDto;
import com.projetjsp.demoprojetjsp.repository.TransfertRepository;
import com.projetjsp.demoprojetjsp.services.ClientsService;
import com.projetjsp.demoprojetjsp.services.TransfertService;
import com.projetjsp.demoprojetjsp.utils.PdfGenerator;

import jakarta.validation.Valid;

@Controller
public class TransfertController {
    @Autowired
    private TransfertService transfertService;

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private TransfertRepository repositories;

    private final PdfGenerator pdfGenerator;

    @Autowired
    private TransfertService repo;

    @Autowired
    public TransfertController(TransfertService transfertService, PdfGenerator pdfGenerator) {
        this.transfertService = transfertService;
        this.pdfGenerator = pdfGenerator;
    }

    // ----------------FORMULAIRE DE TRANSFERT--------------------------//

    @GetMapping("/transferer")
    public String transferer(@RequestParam("numtel") String numTel, Model model) {
        model.addAttribute("numTel", numTel);
        return "transferts/transferer";
    }

    @PostMapping("/transferer")
    public String processTransfer(@RequestParam String idEnv, @RequestParam String numEnvoyeur,
            @RequestParam String numRecepteur, @RequestParam BigDecimal montant,
            @RequestParam String raison, Model model) {
        try {
            transfertService.transferer(idEnv, numEnvoyeur, numRecepteur, montant, raison);
            model.addAttribute("successMessage", "Transsaction réussi, vous allez recevoir un mail!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "transferts/transferer";
    }

    // ----------------------LISTE TRANSFERT---------------------//

    @GetMapping("/transfert")
    public String listTransferts(Model model) {
        List<Transfert> transferts = transfertService.getAllTransferts();
        model.addAttribute("transferts", transferts);

        // Calcul de la somme des frais de transfert
        BigDecimal sommeFrais = transferts.stream()
                .map(Transfert::getFraisDeTransfert)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("sommeFrais", sommeFrais);

        return "transferts/listetransfert";
    }

    @GetMapping("/recherchertransfert")
    public String rechercherTransfert(@RequestParam("query") String query, Model model) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date(formatter.parse(query).getTime());
            List<Transfert> transferts = transfertService.findByDateOnly(date);
            model.addAttribute("transferts", transferts);
        } catch (ParseException e) {
            e.printStackTrace();
            // Gérez l'erreur de manière appropriée, peut-être ajouter un message d'erreur
            // au modèle
        }
        return "transferts/listetransfert";
    }

    // Endpoint pour générer et télécharger le PDF de la liste des transferts
    // @GetMapping("/generer-pdf")
    // @ResponseBody
    // public ResponseEntity<byte[]> generatePdf() {
    // try {
    // byte[] pdfBytes =
    // pdfGenerator.generateTransfertListPdf(transfertService.getAllTransferts());
    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_PDF);
    // headers.setContentDisposition(ContentDisposition.builder("attachment").filename("liste-transferts.pdf").build());
    // return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    // } catch (IOException e) {
    // // Gestion de l'erreur de génération de PDF
    // e.printStackTrace();
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // @GetMapping("/client/{numtel}")
    // public String getTransfertsByClientNumtel(@PathVariable String numtel, Model
    // model) {
    // List<Transfert> transferts =
    // transfertRepository.findByEnvoyeur_Numtel(numtel);
    // model.addAttribute("transferts", transferts);
    // return "transferts/client";
    // }

    @GetMapping("/recherchertransfertByNumenvoyeur")
    public String rechercherTransfertBynumerotelenvoyeur(@RequestParam(name = "numtel") String numtel, Model model) {
        List<Transfert> transferts = transfertService.getTransfertsByClientNumtel(numtel);
        model.addAttribute("transferts", transferts);
        return "transferts/listetransfert"; // Nom de votre vue Thymeleaf
    }

    @GetMapping("/generer-pdf")
    @ResponseBody
    public ResponseEntity<byte[]> generatePdf(@RequestParam String numtel) {
        try {
            List<Transfert> transferts = transfertService.getTransfertsByClientNumtel(numtel);
            byte[] pdfBytes = pdfGenerator.generateTransfertListPdf(transferts, numtel);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "liste-transferts.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Gestion de l'erreur de génération de PDF
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/supprimertransfert")
    public String deleteTransfert(@RequestParam String idEnv, RedirectAttributes redirectAttributes) {
        try {
            Transfert transfert = repositories.findById(idEnv)
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));
            transfertService.deleteTransfert(idEnv);
            redirectAttributes.addFlashAttribute("successdeleteMessage", "transfert supprimé avec succès.");
        } catch (NoSuchElementException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "transfert introuvable.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Une erreur est survenue: impossible de supprimer cet client car il a reference au transfert "
            // + ex.getMessage()
            );
        }

        return "redirect:/transfert";
    }

    // modifiertransfert

    // @GetMapping("/modifiertransfert")
    // public String showEditPage(Model model, @RequestParam String idEnv) {
    // try {
    // Transfert transfert = repo.findById(idEnv).get();
    // model.addAttribute("transfert", transfert);

    // TransfertDto transfertDto = new TransfertDto();
    // transfertDto.setIdEnv(transfert.getIdEnv());
    // transfertDto.setNumEnvoyeur(transfert.getEnvoyeur().getNumtel());
    // transfertDto.setNumRecepteur(transfert.getRecepteur().getNumtel());
    // transfertDto.setMontant(transfert.getMontant());
    // transfertDto.setDate(transfert.getDate()); // Utilise java.sql.Date
    // transfertDto.setRaison(transfert.getRaison());
    // model.addAttribute("transfertDto", transfertDto);

    // } catch (Exception ex) {
    // System.out.println("Exception:" + ex.getMessage());
    // return "redirect:/transferts";
    // }
    // return "transferts/Modifiertransfert";
    // }

    @GetMapping("/modifiertransfert")
    public String showEditPage(Model model, @RequestParam String idEnv) {
        try {
            Transfert transfert = repo.findById(idEnv).orElse(null);
            if (transfert == null) {
                model.addAttribute("errorMessage", "Transfert non trouvé.");
                return "redirect:/transferts";
            }
            TransfertDto transfertDto = new TransfertDto();
            transfertDto.setIdEnv(transfert.getIdEnv());
            transfertDto.setNumEnvoyeur(transfert.getEnvoyeur().getNumtel());
            transfertDto.setNumRecepteur(transfert.getRecepteur().getNumtel());
            transfertDto.setMontant(transfert.getMontant());
            transfertDto.setDate(transfert.getDate()); // Utilise java.sql.Date
            transfertDto.setRaison(transfert.getRaison());
            model.addAttribute("transfertDto", transfertDto);
        } catch (Exception ex) {
            System.out.println("Exception:" + ex.getMessage());
            return "redirect:/transferts";
        }
        return "transferts/Modifiertransfert";
    }

    // @PostMapping("/modifiertransfert")
    // public String updateTransfert(Model model, String idEnv, @Valid
    // @ModelAttribute TransfertDto transfertDto,
    // BindingResult result, RedirectAttributes redirectAttributes) {
    // if (result.hasErrors()) {
    // // model.addAttribute("errorMessage", "Il y a des erreurs dans le
    // formulaire.");
    // return "transferts/Modifiertransfert";
    // }

    // Transfert transfert = transfertService.getTransfertById(idEnv);
    // if (transfert == null) {
    // model.addAttribute("errorMessage", "Transfert non trouvé.");
    // return "transferts/Modifiertransfert";
    // }

    // Client envoyeur =
    // clientsService.getClientByNumtel(transfertDto.getNumEnvoyeur());
    // Client recepteur =
    // clientsService.getClientByNumtel(transfertDto.getNumRecepteur());

    // if (envoyeur == null || recepteur == null) {
    // model.addAttribute("errorMessage", "Envoyeur ou récepteur non trouvé.");
    // return "transferts/Modifiertransfert";
    // }

    // transfert.setMontant(transfertDto.getMontant());
    // transfert.setDate(new java.sql.Date(transfertDto.getDate().getTime())); //
    // Convertir java.util.Date en
    // // java.sql.Date
    // transfert.setFraisDeTransfert(transfertDto.getFraisDeTransfert());
    // transfert.setEnvoyeur(envoyeur);
    // transfert.setRecepteur(recepteur);
    // transfert.setRaison(transfertDto.getRaison());

    // transfertService.updateTransfert(transfert);
    // redirectAttributes.addFlashAttribute("successMessage", "Le transfert a été
    // mis à jour avec succès.");
    // return "redirect:/transfert";
    // }

    @PostMapping("/modifiertransfert")
    public String updateTransfert(Model model, @RequestParam String idEnv,
            @Valid @ModelAttribute TransfertDto transfertDto,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Il y a des erreurs dans le formulaire.");
            return "transferts/Modifiertransfert";
        }

        Transfert transfert = transfertService.getTransfertById(idEnv);
        if (transfert == null) {
            model.addAttribute("errorMessage", "Transfert non trouvé.");
            return "transferts/Modifiertransfert";
        }

        Client envoyeur = clientsService.getClientByNumtel(transfertDto.getNumEnvoyeur());
        Client recepteur = clientsService.getClientByNumtel(transfertDto.getNumRecepteur());

        if (envoyeur == null || recepteur == null) {
            model.addAttribute("errorMessage", "Envoyeur ou récepteur non trouvé.");
            return "transferts/Modifiertransfert";
        }

        transfert.setMontant(transfertDto.getMontant());
        transfert.setDate(new java.sql.Date(transfertDto.getDate().getTime())); // Convertir java.util.Date en
                                                                                // java.sql.Date
        transfert.setFraisDeTransfert(transfertDto.getFraisDeTransfert());
        transfert.setEnvoyeur(envoyeur);
        transfert.setRecepteur(recepteur);
        transfert.setRaison(transfertDto.getRaison());

        transfertService.updateTransfert(transfert);
        redirectAttributes.addFlashAttribute("successMessage", "Le transfert a été mis à jour avec succès.");
        return "redirect:/transferts";
    }

}
