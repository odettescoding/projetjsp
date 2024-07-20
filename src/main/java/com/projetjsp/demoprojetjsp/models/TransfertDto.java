package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransfertDto {
    @NotBlank
    private String idEnv;

    @NotBlank
    private String numEnvoyeur;
    @NotBlank
    private String numRecepteur;

    @NotNull(message = "Le montant ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à zéro")
    private BigDecimal montant;

    @NotNull(message = "La date ne peut pas être nulle")
    private Date date;

    @NotNull(message = "Le frais ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à zéro")
    private BigDecimal fraisDeTransfert;

    @NotBlank
    private String raison;

    public String getIdEnv() {
        return idEnv;
    }

    public void setIdEnv(String idEnv) {
        this.idEnv = idEnv;
    }

    public String getNumEnvoyeur() {
        return numEnvoyeur;
    }

    public void setNumEnvoyeur(String numEnvoyeur) {
        this.numEnvoyeur = numEnvoyeur;
    }

    public String getNumRecepteur() {
        return numRecepteur;
    }

    public void setNumRecepteur(String numRecepteur) {
        this.numRecepteur = numRecepteur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getFraisDeTransfert() {
        return fraisDeTransfert;
    }

    public void setFraisDeTransfert(BigDecimal fraisDeTransfert) {
        this.fraisDeTransfert = fraisDeTransfert;
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
