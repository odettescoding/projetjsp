package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TauxDto {
    @NotBlank
    private String idtaux;

    // @NotNull
    // @Min(1)
    // private Integer montant1;

    @NotNull
    private BigDecimal montant1;

    // @NotNull
    // @Min(1)
    // private Integer montant2;
    @NotNull
    private BigDecimal montant2;

    // @NotNull
    // private BigDecimal tauxDeChange;

    // public BigDecimal getTauxDeChange() {
    // return tauxDeChange;
    // }

    // public void setTauxDeChange(BigDecimal tauxDeChange) {
    // this.tauxDeChange = tauxDeChange;
    // }

    // Getters et Setters
    public String getIdtaux() {
        return idtaux;
    }

    public void setIdtaux(String idtaux) {
        this.idtaux = idtaux;
    }

    // public Integer getMontant1() {
    // return montant1 != null ? montant1 : 1; // Default to 1 if solde is null
    // }
    // Getter pour montant1
    public BigDecimal getMontant1() {
        return montant1;
    }

    // public void setMontant1(Integer montant1) {
    // this.montant1 = montant1;
    // }
    // Setter pour montant1
    public void setMontant1(BigDecimal montant1) {
        this.montant1 = montant1;
    }

    // public Integer getMontant2() {
    // return montant2 != null ? montant2 : 1; // Default to 1 if solde is null
    // }
    // Getter pour montant2
    public BigDecimal getMontant2() {
        return montant2;
    }

    // public void setMontant2(Integer montant2) {
    // this.montant2 = montant2;
    // }
    // Setter pour montant2
    public void setMontant2(BigDecimal montant2) {
        this.montant2 = montant2;
    }
}