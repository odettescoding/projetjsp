package com.projetjsp.demoprojetjsp.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FraisDto {
    @NotBlank
    private String idfrais;

    @NotNull
    private BigDecimal montant1;

    @NotNull
    private BigDecimal montant2;

    @NotNull
    private BigDecimal fraisdetransfert;

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

}
