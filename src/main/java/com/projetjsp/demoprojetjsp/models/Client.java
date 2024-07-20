package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "clients", uniqueConstraints = { @UniqueConstraint(columnNames = "numtel") })
public class Client {

    @Id
    @Column(name = "numtel")
    private String numtel;

    @Column(name = "nom")
    private String nom;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "pays")
    private String pays;

    // @Column(name = "solde")
    // private int solde;
    @Column(name = "solde")
    private BigDecimal solde;

    @Column(name = "mail")
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
    // public int getSolde() {
    // return solde;
    // }

    public BigDecimal getSolde() {
        return solde;
    }

    // Setter pour solde
    // public void setSolde(int solde) {
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

    @Override
    public String toString() {
        return "Client [numtel=" + numtel + ", nom=" + nom + ", sexe=" + sexe + ", pays=" + pays + ", solde=" + solde
                + ", mail=" + mail + "]";
    }

}
