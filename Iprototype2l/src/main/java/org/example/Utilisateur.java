package org.example;

public class Utilisateur {
    protected String nom;
    protected String courriel;
    protected String motDePasse;

    public Utilisateur(String nom, String courriel, String motDePasse) {
        this.nom = nom;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }

    public String getCourriel() {
        return courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}
