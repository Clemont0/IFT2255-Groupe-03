package org.example;

import java.util.ArrayList;
import java.util.List;

public class Resident extends Utilisateur {
    private String adresse;
    private String postalCode;

    public Resident(String nom, String courriel, String motDePasse, String adresse, String postalCode) {
        super(nom, courriel, motDePasse);
        this.adresse = adresse;
        this.postalCode = postalCode;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getPostalCode() {
        return postalCode;
    }



}
