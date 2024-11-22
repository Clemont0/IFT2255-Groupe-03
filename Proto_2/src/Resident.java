import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Resident extends Utilisateur{
    private LocalDate dateNaissance;
    private String phone;
    private String adresse;
    private Quartier quartier;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public Resident(String nom, LocalDate birthday,String courriel, String mdp, String phone, String adresse){
        super(nom,courriel,mdp);
        this.dateNaissance = birthday;
        this.adresse = adresse;
        this.phone = phone;
    }

    public LocalDate getDateNaissance() {return this.dateNaissance;}

    public String getAdresse() {
        return adresse;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Quartier getQuartier() {
        return quartier;
    }

    @Override
    public boolean estValide() {
        int diff = LocalDate.now().getYear() - this.dateNaissance.getYear();
        boolean cond1 = this.dateNaissance.isBefore(LocalDate.now());
        boolean cond2 = diff >= 16;

        return super.estValide() && cond1 && cond2;
    }

    @Override
    public String toString() {
        String string = super.toString();
        StringBuilder sb = new StringBuilder();

        sb.append(string);
        sb.append(" Date de naissance : ").append(dateNaissance).append("\n");
        sb.append(" Numero de Telephone: ").append(phone).append("\n");
        sb.append(" Adresse: ").append(adresse).append("\n");
        sb.append(" Quartier: ").append(quartier).append("\n");

        return sb.toString();
    }
}
