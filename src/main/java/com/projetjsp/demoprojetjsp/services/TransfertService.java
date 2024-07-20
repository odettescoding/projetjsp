package com.projetjsp.demoprojetjsp.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetjsp.demoprojetjsp.models.Client;
import com.projetjsp.demoprojetjsp.models.Frais;
import com.projetjsp.demoprojetjsp.models.Taux;
import com.projetjsp.demoprojetjsp.models.Transfert;
import com.projetjsp.demoprojetjsp.repository.ClientRepository;
import com.projetjsp.demoprojetjsp.repository.FraisRepository;
import com.projetjsp.demoprojetjsp.repository.TauxRepository;
import com.projetjsp.demoprojetjsp.repository.TransfertRepository;

@Service
public class TransfertService {

    @Autowired
    private TransfertRepository transfertRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TauxRepository tauxRepository;

    @Autowired
    private FraisRepository fraisRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private TransfertRepository repo;

    // --------------Lister tout les transfert------------------//
    public List<Transfert> getAllTransferts() {
        List<Transfert> transferts = transfertRepository.findAll();
        for (Transfert transfert : transferts) {
            Hibernate.initialize(transfert.getEnvoyeur());
            Hibernate.initialize(transfert.getRecepteur());
        }
        return transferts;
    }

    // ---------------------transferer argent -----------------------//
    @Transactional
    public void transferer(String idEnv, String numEnvoyeur, String numRecepteur, BigDecimal montant, String raison)
            throws Exception {
        Optional<Client> optionalEnvoyeur = clientRepository.findById(numEnvoyeur);
        Optional<Client> optionalRecepteur = clientRepository.findById(numRecepteur);

        if (!optionalEnvoyeur.isPresent()) {
            throw new Exception("Envoyeur non trouvé");
        }
        if (!optionalRecepteur.isPresent()) {
            throw new Exception("Recepteur non trouvé");
        }

        Client envoyeur = optionalEnvoyeur.get();
        Client recepteur = optionalRecepteur.get();

        if (envoyeur.getSolde().compareTo(montant) < 0) {
            throw new Exception("Solde insuffisant");
        }

        // Récupérer le taux de change en fonction des pays de l'envoyeur et du
        // récepteur
        BigDecimal tauxDeChange = getTauxDeChange(envoyeur.getPays(), recepteur.getPays());

        // Calculer le montant avec le taux de change
        BigDecimal montantConverti = montant.multiply(tauxDeChange);

        // Appliquer le frais de transfert (exemple: 2%)

        // Récupérer les frais de transfert à appliquer
        BigDecimal fraisDeTransfert = getFraisDeTransfert(montant);

        // Appliquer les frais de transfert
        BigDecimal montantApresFrais = montant.subtract(fraisDeTransfert);

        BigDecimal totalreduit = montant.add(fraisDeTransfert);

        envoyeur.setSolde(envoyeur.getSolde().subtract(totalreduit));
        // BigDecimal montantRecu = montantApresFrais.multiply(tauxDeChange);
        recepteur.setSolde(recepteur.getSolde().add(montantConverti));

        clientRepository.save(envoyeur);
        clientRepository.save(recepteur);

        Transfert transfert = new Transfert();
        transfert.setIdEnv(idEnv);
        transfert.setEnvoyeur(envoyeur);
        transfert.setRecepteur(recepteur);
        transfert.setMontant(montant);
        transfert.setMontantConverti(montantConverti); // Ajoutez cette propriété à votre classe Transfert
        transfert.setFraisDeTransfert(fraisDeTransfert); // Ajout des frais de transfert
        transfert.setRaison(raison);
        transfert.setDate(new java.util.Date());

        transfertRepository.save(transfert);

        // Envoyer un email de notification au recepteur
        String subject = "Transfer d'argent";

        String text = "Bonjour" + recepteur.getNom() + ",\n\n" +
                "Vous venez de recevoir reçu " + montant + "euro de la part de " + envoyeur.getNom()
                + " dont le motif est: " + raison +
                "aujourd'hui le" + new java.util.Date() + "\n" +
                "A la prochaine,\n";

        emailService.sendSimpleMessage(recepteur.getMail(), subject, text);

        // Envoyer un email de notification à l'envoyeur
        String objet = "Retrait dans votre compte";
        String contenu = "Bonjour" + envoyeur.getNom() + ",\n\n" +
                " Vous avez transféré un somme de " + montant + " à " + recepteur.getNom() +
                " dont le motif est: " + raison +
                "aujourd'hui le" + new java.util.Date() + "\n" +
                "Merci de votre confiance,\n";
        emailService.sendSimpleMessage(envoyeur.getMail(), objet, contenu);

    }

    // ------------Méthode pour récupérer le taux de change entre deux pays-----//
    private BigDecimal getTauxDeChange(String paysEnvoyeur, String paysRecepteur) {
        // Concaténer les pays pour former l'idtaux
        String idTaux = paysEnvoyeur + paysRecepteur;

        // Rechercher le taux de change par idtaux
        Taux taux = tauxRepository.findByIdtaux(idTaux);

        if (taux != null) {
            return taux.getTauxDeChange();
        } else {
            throw new IllegalArgumentException(
                    "Taux de change non trouvé pour " + paysEnvoyeur + " vers " + paysRecepteur);
        }
    }

    // Méthode pour récupérer les frais de transfert en fonction du montant à
    // transférer
    private BigDecimal getFraisDeTransfert(BigDecimal montant) {
        List<Frais> fraisList = fraisRepository.findAll();
        for (Frais frais : fraisList) {
            BigDecimal montant1 = frais.getMontant1();
            BigDecimal montant2 = frais.getMontant2();
            BigDecimal fraisDeTransfert = frais.getFraisdetransfert();
            if (montant.compareTo(montant1) >= 0 && montant.compareTo(montant2) <= 0) {
                return fraisDeTransfert;
            }
        }
        throw new IllegalArgumentException("Frais de transfert non trouvé pour le montant " + montant);
    }


    public List<Transfert> findByDateOnly(Date searchDate) {
        return transfertRepository.findByDateOnly(searchDate);
    }

    public List<Transfert> getTransfertsByClientNumtel(String numtel) {
        return transfertRepository.findByEnvoyeur_Numtel(numtel);
    }

    public Optional<Transfert> findById(String idEnv) {
        return transfertRepository.findById(idEnv);
    }

    // ----------supprimer transfert à partir de son id=numtel------------//
    public void deleteTransfert(String idEnv) throws NoSuchElementException {
        Transfert transfert = repo.findById(idEnv).orElseThrow(() -> new NoSuchElementException("transfert not found"));
        repo.delete(transfert);
    }

    public Transfert saveTransfert(Transfert transfert) {
        return transfertRepository.save(transfert);
    }

    public Transfert getTransfertById(String idEnv) {
        return transfertRepository.findById(idEnv).orElse(null);
    }

    public void updateTransfert(Transfert transfert) {
        transfertRepository.save(transfert);
    }
}