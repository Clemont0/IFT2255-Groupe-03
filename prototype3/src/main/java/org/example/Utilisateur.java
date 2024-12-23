package org.example;

/**
 * Classe contenant tout ce qui est relié aux utilisateurs, leur création et leurs données
 */
public class Utilisateur {
    protected String nom;
    protected static String courriel;
    protected String motDePasse;

    public Utilisateur(String nom, String courriel, String motDePasse) {
        this.nom = nom;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }


    public static String getCourriel() {
        return courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }
}
