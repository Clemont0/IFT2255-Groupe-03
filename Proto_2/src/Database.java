import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private List<Resident> residents;
    private List<RequeteTravail> requetes;
    private List<Intervenant> intervenants;
    private List<Travail> travaux;
    private List<Candidature> candidatures;
    private Map<Quartier, List<Notification>> triNotifs;
    private Map<Quartier, List<PlageHoraire>> triHoraire;


    public Database() {
        this.residents = new ArrayList<>();
        this.requetes = new ArrayList<>();
        this.intervenants = new ArrayList<>();
        this.travaux = new ArrayList<>();
        this.candidatures = new ArrayList<>();
        this.triHoraire = new HashMap<>();
        this.triNotifs = new HashMap<>();


        Resident resident1 = new Resident("Moussa Sogoba", LocalDate.of(2000,1,18),
                "moussa@gmail.com", "MoussaS","","3200 rue");
        resident1.setQuartier(Quartier.COTE_DES_NEIGES_NOTRE_DAME_DE_GRACE);
        if (resident1.estValide()){
            residents.add(resident1);
        }
        String titre1 ="Renovation d'un quartier à l'est de Montréal";
        String desc1 ="Travaux de renovation sur les routes et différent accès du quartier.";
        TypeTravail type1 = TypeTravail.CONSTRUCTION_RENOVATION;
        LocalDate date1 = LocalDate.parse("2024/11/20", DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        RequeteTravail requete1 = new RequeteTravail(titre1,desc1,type1,date1,resident1);
        requete1.setId(10000000);
        this.requetes.add(requete1);

        Resident resident2 = new Resident("Toky Erick Rabenantoandro", LocalDate.of(2000,1,18),
                "toky@gmail.com", "TokyE","","5782 Avenue");
        resident2.setQuartier(Quartier.OUTREMONT);
        residents.add(resident2);
        if (resident2.estValide()){
            residents.add(resident2);
        }

        String titre2 ="Renovation urbain à l'ouest de Montréal";
        String desc2 ="Travaux urbain sur les chemins d'accès à différent quartier de l'ouest de Montréal.";
        TypeTravail type2 = TypeTravail.URBAIN;
        LocalDate date2 = LocalDate.parse("2024/11/18", DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        RequeteTravail requete2 = new RequeteTravail(titre2,desc2,type2,date2,resident2);
        requete2.setId(20000000);
        this.requetes.add(requete2);

        Resident resident3 = new Resident("Hugo Bouchard", LocalDate.of(2000,1,18),
                "hugo@gmail.com", "HugoB","","5178 Boulevard");
        resident3.setQuartier(Quartier.PLATEAU_MONTROYAL);
        if (resident3.estValide()){
            residents.add(resident3);
        }

        String titre3 ="Entretien de paysage d'un parc de montréal.";
        String desc3 ="Travaux d'entretien d'un parc de montréal au sud de Montréal.";
        TypeTravail type3 = TypeTravail.ENTRETIEN_PAYSAGE;
        LocalDate date3 = LocalDate.parse("2024/11/19", DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        RequeteTravail requete3 = new RequeteTravail(titre3,desc3,type3,date3,resident3);
        requete3.setId(30000000);
        this.requetes.add(requete3);

        Intervenant intervenant1 = new Intervenant(51475236,"Toky Erick Rabenantoandro","tRabenantoandro@gmail.com",
                "TER",TypeIntervenant.PRIVE);
        if (intervenant1.estValide()){
            intervenants.add(intervenant1);
        }

        Intervenant intervenant2 = new Intervenant(51485237,"Hugo Bouchard","hBouchard@gmail.com",
                "HB",TypeIntervenant.PRIVE);
        if (intervenant1.estValide()){
            intervenants.add(intervenant2);
        }

        Intervenant intervenant3 = new Intervenant(51495238,"Moussa Sogoba","mSogoba@gmail.com",
                "MS",TypeIntervenant.PRIVE);
        if (intervenant1.estValide()){
            intervenants.add(intervenant3);
        }

    }

    public List<Intervenant> getIntervenants() {
        return intervenants;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public List<Travail> getTravaux() {
        return travaux;
    }

    public List<Candidature> getCandidatures() {
        return candidatures;
    }

    public List<RequeteTravail> getRequetes() { return requetes; }

    public Map<Quartier, List<PlageHoraire>> getTriHoraire() {
        return triHoraire;
    }

    public List<Notification> obtenirNotifQuartier(Quartier quartier){
        List<Notification> notifs = new ArrayList<>();

        for (Quartier cle: triNotifs.keySet()){
            if (quartier.equals(cle)){
                notifs = triNotifs.get(cle);
            }
        }

        return notifs;
    }

    public void ajoutResident(Resident resident) {
        this.residents.add(resident);
    }

    public void ajoutIntervenant(Intervenant intervenant) {
        this.intervenants.add(intervenant);
    }

    public void ajoutTravaux(Travail travail){
        this.travaux.add(travail);
    }

    public void ajoutCandidatures(Candidature candidature){this.candidatures.add(candidature);}

    public void ajoutRequetes(RequeteTravail requeteTravail) {this.requetes.add(requeteTravail);}

    public void ajoutHoraire(PlageHoraire proposition, Resident resident){

        for (Quartier key : triHoraire.keySet()) {
            if (resident.getQuartier().equals(key)) {
                triHoraire.get(key).add(proposition);
            }
        }

        triHoraire.put(resident.getQuartier(),new ArrayList<>());
        triHoraire.get(resident.getQuartier()).add(proposition);
    }

    public void ajoutNotif(Notification notification){

        for(Quartier key : triNotifs.keySet()) {
            if (notification.getQuartier().equals(key)){
                triNotifs.get(key).add(notification);
            }
        }

        triNotifs.put(notification.getQuartier(), new ArrayList<>());
        triNotifs.get(notification.getQuartier()).add(notification);
    }

}
