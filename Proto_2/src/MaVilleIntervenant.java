import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MaVilleIntervenant extends MaVille{
    private Intervenant intervenant;

    public MaVilleIntervenant(MaVille maVille, Intervenant intervenant){
        super(maVille.database);
        this.intervenant = intervenant;
    }

    public void inscrire(int id, String nom, String courriel, String mdp, TypeIntervenant type){
        Intervenant intervenant = new Intervenant(id, nom, courriel, mdp, type);
        boolean test = intervenant.estValide();

        if(!test){
            throw new IllegalArgumentException("L'un des pamramètres que vous avez entrer pour completer l'inscription est invalide.");
        }else{
            database.ajoutIntervenant(intervenant);
        }
    }

    public void supprimerCompte() {
        boolean val = false;

        for (Intervenant user : database.getIntervenants()) {
            if (intervenant.equals(user)) {
                database.getIntervenants().remove(intervenant);
                val = true;
                break;
            }
        }
        if (val){
            System.out.println("Le compte que vous désirez supprimer est introuvable.");
        }
    }

    public void voirRequetes() {
        System.out.println(database.getRequetes());
    }

    public void flitrerRequetes(CritereTravaux[] criteres){}

    public void soumettreCandidature(int id, LocalDate debut,LocalDate fin) {
        Candidature candidature = new Candidature(id,debut,fin,intervenant);
        database.ajoutCandidatures(candidature);
    }

    public List<Candidature> voirCandidatures(){
        List<Candidature> candidaturesDepose = new ArrayList<>();

        for (Candidature candidature: database.getCandidatures()){
            if (intervenant.equals(candidature.getIntervenant())){
                candidaturesDepose.add(candidature);
            }
        }

        return candidaturesDepose;
    }

    public void sousttraireCandidature(int id) {
        boolean found = false;

        for(Candidature candidature : database.getCandidatures()){
            if(id == candidature.getId() && intervenant.equals(candidature.getIntervenant())){
                found = true;
                database.getCandidatures().remove(candidature);
                break;
            }
        }

        if(!found){
            System.out.println("Candidature introuvable.");
        }
    }

    public void soumettreTravail(String titre, String desc, TypeTravail type, List<Quartier> quartiers, LocalDate dateDebut, LocalDate dateFin, String horaire) {
        Statut statut = Statut.PREVU;
        Travail travail = new Travail(titre,desc,type,quartiers,dateDebut,dateFin,horaire,statut,
                intervenant);
        database.ajoutTravaux(travail);

        for (Quartier quartier : quartiers){
            Notification notification = new Notification(travail,quartier);
            notification.setMessage("Un nouveau projet à été créer dans votre quartier.");
            database.ajoutNotif(notification);
        }

        System.out.println("Projet créer : \n" + travail);
    }

    public void miseAJourDesc(int id, String newDesc){
        int found = 0;

        for (Travail travail : database.getTravaux()){
            if (id == travail.getId()){
                travail.setDesc(newDesc);
                found ++ ;

                for (Quartier quartier : travail.getQuartiersAffectes()){
                    Notification notification = new Notification(travail,quartier);
                    notification.setMessage("La description du projet à été mise à jour");
                    database.ajoutNotif(notification);
                }
                break;
            }
        }

        if (found == 0){
            System.out.println("Travail introuvable. Veuiller revérifier l'id entré");
        }

    }

    public void miseAJourDateFin(int id, LocalDate dateFin){
        int found = 0;

        for (Travail travail : database.getTravaux()){
            if (id == travail.getId()){
                travail.setDateFin(dateFin);
                found ++;

                for (Quartier quartier : travail.getQuartiersAffectes()){
                    Notification notification = new Notification(travail,quartier);
                    notification.setMessage("La date de fin du projet à été mise à jour.");
                    database.ajoutNotif(notification);
                }
                break;
            }
        }

        if (found == 0){
            System.out.println("Travail introuvable. Veuiller revérifier l'id entré");
        }
    }

    public void miseAJourStatut(int id, Statut statut){
        int found = 0;

        for (Travail travail : database.getTravaux()){
            if (id == travail.getId()){
                travail.setStatut(statut);
                found ++ ;

                for (Quartier quartier : travail.getQuartiersAffectes()){
                    Notification notification = new Notification(travail,quartier);
                    notification.setMessage("Le statut du projet à été mise à jour.");
                    database.ajoutNotif(notification);
                }
                break;
            }
        }

        if (found == 0){
            System.out.println("Travail introuvable. Veuiller revérifier l'id entré");
        }
    }

    private void voirPlagesHoraires(){
        for (Map.Entry<Quartier, List<PlageHoraire>> entry : database.getTriHoraire().entrySet()) {
            System.out.println("Quartiers : " + entry.getKey() + ", Plages horaires proposées : \n" + entry.getValue());
        }
    }

}
