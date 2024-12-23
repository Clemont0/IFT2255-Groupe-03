import java.time.LocalDate;
import java.util.Random;

public class RequeteTravail {
    private int id;
    private String titre;
    private String desc;
    private TypeTravail type;
    private LocalDate dateDebut;
    private Quartier quartier;
    public Resident resident;

    public RequeteTravail(String titre, String desc, TypeTravail type, LocalDate dateDebut, Resident resident){
        Random random = new Random();

        this.id = 10000000 + random.nextInt(90000000);      // Génération d'un id à 8 chiffres.
        this.titre = titre;
        this.desc = desc;
        this.type = type;
        this.dateDebut = dateDebut;
        this.resident = resident;
        this.quartier = resident.getQuartier();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Information de la requête :\n");
        sb.append("ID : ").append(id).append("\n");
        sb.append("Titre : ").append(titre).append("\n");
        sb.append("Projet soumis par : ").append(resident.getNom()).append("\n");
        sb.append("Type de travaux : ").append(type).append("\n");
        sb.append("Quartier : \n").append(quartier).append("\n");
        sb.append("Date de debut des travaux: ").append(dateDebut).append("\n");
        sb.append("Description générale du travail: \n").append(desc).append("\n");

        return sb.toString();
    }
}
