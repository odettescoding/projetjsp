package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "envoyers", uniqueConstraints = { @UniqueConstraint(columnNames = "idEnv") })
public class Transfert {
    @Id
    @Column(name = "idEnv")
    private String idEnv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numEnvoyeur", referencedColumnName = "numTel")
    private Client envoyeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numRecepteur", referencedColumnName = "numTel")
    private Client recepteur;

    // @Column(name = "montant")
    // private int montant;
    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "montant_converti")
    private BigDecimal montantConverti; // Ajouter cette ligne pour le montant converti
    @Column(name = "fraisdetransfert")
    private BigDecimal fraisDeTransfert;

    public BigDecimal getFraisDeTransfert() {
        return fraisDeTransfert;
    }

    public void setFraisDeTransfert(BigDecimal fraisDeTransfert) {
        this.fraisDeTransfert = fraisDeTransfert;
    }

    public BigDecimal getMontantConverti() {
        return montantConverti;
    }

    public void setMontantConverti(BigDecimal montantConverti) {
        this.montantConverti = montantConverti;
    }

    @Column(name = "date")
    private Date date;

    @Column(name = "raison")
    private String raison;

    // Getters et setters
    public String getIdEnv() {
        return idEnv;
    }

    public void setIdEnv(String idEnv) {
        this.idEnv = idEnv;
    }

    public Client getEnvoyeur() {
        return envoyeur;
    }

    public void setEnvoyeur(Client envoyeur) {
        this.envoyeur = envoyeur;
    }

    public Client getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Client recepteur) {
        this.recepteur = recepteur;
    }

    // public int getMontant() {
    // return montant;
    // }
    // Getter et Setter pour montant
    public BigDecimal getMontant() {
        return montant;
    }

    // public void setMontant(int montant) {
    // this.montant = montant;
    // }
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

}
