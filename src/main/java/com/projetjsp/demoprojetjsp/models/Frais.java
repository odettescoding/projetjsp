package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "frais")
public class Frais {
    @Id
    @Column(name = "idfrais")
    private String idfrais;

    @Column(name = "montant1")
    private BigDecimal montant1;

    @Column(name = "montant2")
    private BigDecimal montant2;

    @Column(name = "fraisdetransfert")
    private BigDecimal fraisdetransfert;

    public void setIdfrais(String idfrais) {
        this.idfrais = idfrais;
    }

    public void setMontant1(BigDecimal montant1) {
        this.montant1 = montant1;
    }

    public void setMontant2(BigDecimal montant2) {
        this.montant2 = montant2;
    }

    public void setFraisdetransfert(BigDecimal fraisdetransfert) {
        this.fraisdetransfert = fraisdetransfert;
    }

    public String getIdfrais() {
        return idfrais;
    }

    public BigDecimal getMontant1() {
        return montant1;
    }

    public BigDecimal getMontant2() {
        return montant2;
    }

    public BigDecimal getFraisdetransfert() {
        return fraisdetransfert;
    }

}
