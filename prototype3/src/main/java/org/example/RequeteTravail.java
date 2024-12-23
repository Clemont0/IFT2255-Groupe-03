package org.example;

/**
 * Classe contenant tout ce qui est relié aux requêtes de travail, leur création et leurs données.
 */
public class RequeteTravail {
    private String id;
    private String titre;
    private String description;
    private String type;
    private String dateDebut; // Utiliser String au lieu de LocalDate
    private String dateFin;   // Utiliser String au lieu de LocalDate

    public RequeteTravail(String id, String titre, String description, String type, String dateDebut, String dateFin) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = this.dateFin;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }
}
