import java.time.LocalDate;

public class PlageHoraire {
    private LocalDate dateDebut,dateFin;

    public PlageHoraire (LocalDate dateDebut, LocalDate dateFin){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Date de début de l'horaire proposé :").append(dateDebut).append("\n");
        sb.append("Date de fin de l'horaire proposé :").append(dateFin);

        return sb.toString();
    }

}
