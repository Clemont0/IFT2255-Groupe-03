package org.example;

public class Intervenant extends Utilisateur {
    private String type;
    private String id;

    public Intervenant(String nom, String courriel, String motDePasse, String type, String id) {
        super(nom, courriel, motDePasse);
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
