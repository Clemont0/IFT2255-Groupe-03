import java.time.LocalDate;
import java.util.*;

public class MaVilleResident extends MaVille {
    private Resident resident;
    private List<Quartier> abonnement;

    public MaVilleResident(MaVille maVille, Resident resident){
        super(maVille.database);
        this.resident = resident;
        this.abonnement = new ArrayList<>();
        this.abonnement.add(resident.getQuartier());
    }

    public void inscrire(String nom, LocalDate dateNaissance, String courriel, String mdp, String phone, String adresse){
        Resident newResident = new Resident(nom,dateNaissance,courriel,mdp,phone,adresse);
        boolean test = newResident.estValide();

        if(!test){
            System.out.println("L'un des paramètres que vous avez entrer pour completer l'inscription est invalide.");
        }else{
            database.ajoutResident(resident);
        }
    }

    public void supprimerCompte() {
        boolean val = false;

        for (Resident user : database.getResidents()) {
            if (resident.equals(user)) {
                database.getIntervenants().remove(user);
                val = true;
                break;
            }
        }
        if (val){
            System.out.println("Le compte que vous désirez supprimer est introuvable.");
        }
    }

    public List<Travail> voirTravaux() {
        return database.getTravaux();
    }

    public List<Travail> flitrerTravaux(List<Object> criteres){
        List<Travail> resultats = new ArrayList<>();
        boolean found = false;

        for (Object var : criteres){
            if (var instanceof String){
                for (Travail travail: database.getTravaux()){
                    if (((String) var).equalsIgnoreCase(travail.getTitre())){
                         resultats.add(travail);
                         found =true;
                    }
                }

                if (!found){
                    System.out.println("Aucun travail trouvé pour le critère :" + var);
                }
            } else if (var instanceof  TypeTravail) {
                for (Travail travail: database.getTravaux()){
                    if (var.equals(travail.getType())){
                        resultats.add(travail);
                        found =true;
                    }
                }

                if (!found){
                    System.out.println("Aucun travail trouvé pour le critère :" + var);
                }
            } else if (var instanceof Quartier) {
                for (Travail travail: database.getTravaux()){
                    for(Quartier quartier : travail.getQuartiersAffectes()){
                        if (var.equals(quartier)){
                            resultats.add(travail);
                            found = true;
                        }
                    }
                }

                if (!found){
                    System.out.println("Aucun travail trouvé pour le critère :" + var);
                }
            } else if (var instanceof LocalDate) {
                for (Travail travail: database.getTravaux()){
                    if (var.equals(travail.getDateDebut())){
                        resultats.add(travail);
                        found =true;
                    }
                }

                if (!found){
                    System.out.println("Aucun travail trouvé pour le critère :" + var);
                }
            }
        }

        return resultats;
    }

    public void voirEntraves(){}

    public void  flitrerEntraves(CritereEntraves criteres){}

    public void voirNotification() {
        List<Notification> notifications;

        for(Quartier quartier : abonnement){
            notifications = database.obtenirNotifQuartier(quartier);

            if (notifications.isEmpty()) {
                System.out.println("Aucune notification n'est disponible pour le quartier" + quartier.toString());
            }else{
                System.out.println("Quartier :" + quartier + "\nNotifications :\n" + notifications);
            }
        }
    }

    // Abonnement notification.
    public void ajouterNotification(List<Quartier> quartiers) {
        abonnement.addAll(quartiers);
    }

    public void voirPlagesHoraires(){
        List<PlageHoraire> horaireList = new ArrayList<>();

        for (Quartier key : database.getTriHoraire().keySet()) {
            if (resident.getQuartier().equals(key)) {
                System.out.println("Les propositions de plages horaires pour votre quartier sont :\n");
                horaireList = database.getTriHoraire().get(key);
                System.out.println(horaireList);
                break;
            }
        }

        if (horaireList.isEmpty()){
            System.out.println("Aucune plage horaire n'a été ajouté pour votre quartier.");
        }

    }

    public void ajouterPlageHoraire(LocalDate dateDebut, LocalDate dateFin){
        PlageHoraire horaire = new PlageHoraire(dateDebut,dateFin);
        database.ajoutHoraire(horaire,resident);
    }

    public void soumettreRequete(String titre, String desc, TypeTravail type, LocalDate dateDebut){
        RequeteTravail requete  = new RequeteTravail(titre,desc,type,dateDebut,resident);
        database.ajoutRequetes(requete);

        System.out.println("L'ID de votre requête est :" + requete.getId());
    }

    public void suiviRequete(int id) {
        RequeteTravail requete = null;

        for (RequeteTravail travail : database.getRequetes()) {
            if (id == travail.getId()) {
                requete = travail;
                break;
            }
        }

        if(requete == null){
            System.out.println("Requête introuvable.");
        }else{
            System.out.println("Requête trouvé :\n" + requete);
        }
    }

    public List<Candidature> voirCandidatures(int id) {
        List<Candidature> trouver = new ArrayList<>();

        for (Candidature candidature: database.getCandidatures()){
            if (id == candidature.getId()){
                trouver.add(candidature);
            }
        }

        if (trouver.isEmpty()){
            System.out.println("Aucune candidature n'a été déposer.");
        } else {
            System.out.println("Les candidatures déposées sont : \n" + trouver);
        }

        return trouver;
    }

    public void validationCandidature(String nom, int id, String msg){

        for(Candidature candidat : voirCandidatures(id)){
            if (candidat.getIntervenant().getNom().equalsIgnoreCase(nom) && candidat.getId() == id){
                candidat.valider(msg);
            }else{
                candidat.setMessage("Votre candidature a été rejété pour cette demande.");
            }
        }
    }

    private void validerRequete(String titre, String desc, TypeTravail type, Date dateDebut) {}

    private void validerInscription(String nom, Date dateNaissance, String courriel, String mdp, String phone, String adresse) {}

}
