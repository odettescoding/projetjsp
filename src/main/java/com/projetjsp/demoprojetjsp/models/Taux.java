package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "taux", uniqueConstraints = { @UniqueConstraint(columnNames = "idtaux") })
public class Taux {
    @Id
    @Column(name = "idtaux")
    private String idtaux;

    // @Column(name = "montant1")
    // private int montant1;
    @Column(name = "montant1")
    private BigDecimal montant1;

    // @Column(name = "montant2")
    // private int montant2;
    @Column(name = "montant2")
    private BigDecimal montant2;

    // pour stocker le taux de change
    @Column(name = "tauxDeChange")
    private BigDecimal tauxDeChange;

    public Taux() {
        // Initialisation des attributs avec des valeurs par défaut
        this.idtaux = ""; // Valeur par défaut pour idtaux
        this.montant1 = BigDecimal.ZERO; // Valeur par défaut pour montant1
        this.montant2 = BigDecimal.ZERO; // Valeur par défaut pour montant2
        this.tauxDeChange = BigDecimal.ZERO; // Valeur par défaut pour tauxDeChange
    }

    public Taux(String idtaux, BigDecimal montant1, BigDecimal montant2) {
        this.idtaux = idtaux;
        this.montant1 = montant1;
        this.montant2 = montant2;
        this.tauxDeChange = calculateTauxDechange();
    }

    private BigDecimal calculateTauxDechange() {
        // Vérifiez si montant1 n'est pas zéro pour éviter une division par zéro
        if (montant1.compareTo(BigDecimal.ZERO) != 0) {
            // Calcul du taux de transfert en divisant montant2 par montant1
            return montant2.divide(montant1, 10, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO; // Ou une autre valeur par défaut si montant1 est zéro
        }
    }

    public String getIdtaux() {
        return idtaux;
    }

    public void setIdtaux(String idtaux) {
        this.idtaux = idtaux;
    }

    // // Getter pour montant1
    // public int getMontant1() {
    // return montant1;
    // }

    // Getter pour montant1
    public BigDecimal getMontant1() {
        return montant1;
    }

    // // Setter pour montant1
    // public void setMontant1(int montant1) {
    // this.montant1 = montant1;
    // }

    // Setter pour montant1
    public void setMontant1(BigDecimal montant1) {
        this.montant1 = montant1;
    }

    // // Getter pour montant2
    // public int getMontant2() {
    // return montant2;
    // }

    // Getter pour montant2
    public BigDecimal getMontant2() {
        return montant2;
    }

    // // Setter pour montant2
    // public void setMontant2(int montant2) {
    // this.montant2 = montant2;
    // }
    // Setter pour montant2
    public void setMontant2(BigDecimal montant2) {
        this.montant2 = montant2;
    }

    public BigDecimal getTauxDeChange() {
        return tauxDeChange;
    }

    // Setter pour tauxDeChange
    public void setTauxDeChange(BigDecimal tauxDeChange) {
        this.tauxDeChange = tauxDeChange;
    }

    @Override
    public String toString() {
        return "Taux [idtaux=" + idtaux + ", montant1=" + montant1 + ", montant2=" +
                montant2 + "]";
    }
}
