import java.time.LocalDate;

public class Candidature {
    private int id;
    private LocalDate dateDebut,dateFin;
    private Intervenant intervenant;
    private Boolean etat;
    private String message;

    public Candidature (int id ,LocalDate debut, LocalDate fin, Intervenant intervenant){
        this.id = id;
        this.dateDebut = debut;
        this.dateFin = fin;
        this.intervenant = intervenant;
        this.etat = false;
        this.message ="";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String isThereAMessage(){
        String msg;

        if(this.message.isEmpty()){
            msg = "";
        } else {
            msg = "Message : " + this.message;
        }

        return msg;
    }

    public int getId() {
        return id;
    }

    public Intervenant getIntervenant() {
        return intervenant;
    }

    public void valider(String msg){
        this.etat = true;
        this.message = msg;
    }

    public String Etat(){
        String state;

        if(this.etat.equals(false)){
            state = "En attente";
        }else{
            state = "Approuvé";
        }

        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nInformation sur la candidature :\n");
        sb.append("ID : ").append(id).append("\n");
        sb.append("Candidature soumis par : ").append(intervenant.getNom()).append("\n");
        sb.append("Dates proposées par l'intervenant  -  Debut : ").append(dateDebut).append("  Fin:  ").append(dateFin).append("\n");
        sb.append("Etat : ").append(Etat()).append("\n");
        sb.append(isThereAMessage());

        return sb.toString();
    }

}
