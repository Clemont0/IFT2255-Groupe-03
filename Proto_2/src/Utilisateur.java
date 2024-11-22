import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilisateur {

    private String nom;
    private String courriel;
    private String motPasse;

    public Utilisateur(String nom, String courriel, String motPasse){
        this.nom = nom;
        this.courriel = courriel;
        this.motPasse = motPasse;
    }

    public String getNom() {
        return nom;
    }

    public String getCourriel() {
        return courriel;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public boolean estValide () {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Matcher matcher = Pattern.compile(emailRegex).matcher(this.courriel);

        return (!this.courriel.isEmpty() && !this.nom.isEmpty() && !this.motPasse.isEmpty() && matcher.matches());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Profil :\n");
        sb.append(" Nom : ").append(nom).append("\n");
        sb.append(" Email : ").append(courriel).append("\n");

        return sb.toString();
    }
}
