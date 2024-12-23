package org.example;

import java.util.List;

public class Projet {
    private int id;
    private String titre;
    private String description;
    private String type;
    private List<String> quartiers;
    private String dateDebut;
    private String dateFin;
    private String horaire;
    private String statut;

    // Constructeur
    public Projet(int id, String titre, String description, String type, List<String> quartiers,
                  String dateDebut, String dateFin, String horaire, String statut) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.quartiers = quartiers;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.horaire = horaire;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getQuartiers() {
        return quartiers;
    }

    public void setQuartiers(List<String> quartiers) {
        this.quartiers = quartiers;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", quartiers=" + quartiers +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", horaire='" + horaire + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
