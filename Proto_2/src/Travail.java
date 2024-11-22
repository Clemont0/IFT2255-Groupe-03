import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class Travail {
    private int id;
    private String titre;
    private String desc;
    private TypeTravail type;
    private List<Quartier> quartiersAffectes;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String horaire;
    private Statut statut;
    private Intervenant intervenant;

    public Travail(String titre, String desc, TypeTravail type, List<Quartier> quartiers, LocalDate debut, LocalDate fin,
                   String horaire, Statut statut, Intervenant responsable){
        Random random = new Random();

        this.id = 100000 + random.nextInt(900000) ; // Id à 6 chiffres généré de manière aléatoire.
        this.titre = titre;
        this.desc = desc;
        this.type = type;
        this.quartiersAffectes = quartiers;
        this.dateDebut = debut;
        this.dateFin = fin;
        this.horaire = horaire;
        this.statut = statut;
        this.intervenant = responsable;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public TypeTravail getType(){return  type; }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public List<Quartier> getQuartiersAffectes() {
        return quartiersAffectes;
    }

    public void setDesc(String desc) { this.desc = desc; }

    public void setDateFin(LocalDate fin) { this.dateFin = fin; }

    public void setStatut(Statut statut) { this.statut = statut; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Information du Projet :\n");
        sb.append("ID :").append(id).append("\n");
        sb.append("Titre : ").append(titre).append("\n");
        sb.append("Projet soumis par : ").append(intervenant.getNom()).append("\n");
        sb.append("Type de travaux : ").append(type).append("\n");
        sb.append("Quartiers affectés : \n").append(quartiersAffectes).append("\n");
        sb.append("Date de debut des travaux: ").append(dateDebut).append("    ").append(" Date de fin prévu des travaux: ").append(dateFin).append("\n");
        sb.append("Horaire : ").append(horaire).append("\n");
        sb.append("Description générale du travail: \n").append(desc).append("\n");
        sb.append("Statue: ").append(statut).append("\n");

        return sb.toString();
    }

}
