import java.time.LocalDate;
import java.util.Random;

public class Notification {
    private int id;
    private LocalDate date;
    private String message;
    private Quartier quartier;
    private Travail projet;

    public Notification(Travail travail, Quartier quartier){
        Random random = new Random();

        this.id = 100000 + random.nextInt(900000) ;
        this.date = LocalDate.now();
        this.projet = travail;
        this.quartier = quartier;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Quartier getQuartier() {
        return quartier;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Projet concerné: ").append(projet.getTitre()).append("        ").append("Date: ").append(date).append("\n");
        sb.append("Quartier Affecté: ").append(quartier).append("\n");
        sb.append("Message: \n");
        sb.append(message);

        return sb.toString();
    }

}
