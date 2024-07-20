package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClientDto {

    @NotBlank
    private String numtel;

    @NotBlank
    private String nom;

    @NotBlank
    private String sexe;

    @NotBlank
    private String pays;

    @NotNull
    // @Min(0)
    // private Integer solde;
    private BigDecimal solde;

    @NotBlank

    private String mail;

    // Getters and setters

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    // Getter pour solde
    // public Integer getSolde() {
    // return solde != null ? solde : 0; // Default to 0 if solde is null
    // }
    public BigDecimal getSolde() {
        return solde != null ? solde : BigDecimal.ZERO; // Valeur par défaut de BigDecimal
    }

    // Setter pour solde
    // public void setSolde(Integer solde) {
    // this.solde = solde;
    // }
    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    // Getter pour mail
    public String getMail() {
        return mail;
    }

    // Setter pour mail
    public void setMail(String mail) {
        this.mail = mail;
    }

}
