import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.lang.System.out;

public class Menu {

    public void menuResident(Resident resident, MaVille ville) {
        Scanner scanner = new Scanner(System.in);
        MaVilleResident user = new MaVilleResident(ville, resident);

        boolean exit = false;

        while (!exit) {
            out.println("    Veuiller choisir parmi les options suivantes : \n");
            out.println("1 - Consulter la liste des travaux en cours ou à venir.");  //API...
            out.println("2 - Rechercher des travaux.");                              //DONE
            out.println("3 - Gestions des requêtes de travail.");                    //DONE
            out.println("4 - Consulter les entraves routières en cours.");           //API...
            out.println("5 - Participer à une planification parcipative.");          //DONE
            out.println("6 - Gérer les notifications.");                             //DONE
            out.println("7 - Gestion du compte.");                                   //DONE

            out.println("EXIT - Déconnexion.");

            switch (scanner.nextLine()) {
                case "1":
                    out.println("Option indisponible.");
                    break;
                case "2":
                    List<Object> choix = choixCriteres(scanner);
                    List<Travail> trouvaille = user.flitrerTravaux(choix);

                    out.println("Les resultats de votre recherche sont : \n" + trouvaille);
                    break;
                case "3":
                    gestionRequetes(scanner,user);
                    break;
                case "4":
                    out.println("Indisponible.");
                    break;
                case "5":
                    planification(scanner,user);
                    break;
                case "6":
                    notifications(scanner,user);
                    break;
                case "7":
                    gestionCompte(scanner,user);
                    break;

                case "EXIT":
                    out.println("Retour au menu principal.");
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    public void menuIntervenant(Intervenant intervenant, MaVille ville) {
        Scanner scanner = new Scanner(System.in);
        MaVilleIntervenant user = new MaVilleIntervenant(ville,intervenant);

        boolean exit = false;

        while (!exit) {
            out.println("    Veuiller choisir parmi les options suivantes : \n");
            out.println("1 - Consulter les requêtes de travail.");       // DONE
            out.println("2 - Gestion des candidatures.");                // DONE
            out.println("3 - Gestions de projet.");                      // DONE
            out.println("4 - Gestion du compte.");                       // DONE

            out.println("EXIT - Déconnexion.");

            switch (scanner.nextLine()) {
                case "1":
                    user.voirRequetes();
                    break;
                case "2":
                    gestionCandidature(scanner,user);
                    break;
                case "3":
                    gestionProjet(scanner,user);
                    break;
                case "4":
                    gestionCompte(scanner,user);
                    break;
                case "EXIT":
                    out.println("Retour au menu principal.");
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    // Méthodes relatives aux residents.

    public void gestionRequetes(Scanner scanner, MaVilleResident resident){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Soumettre une requête.");
            out.println("2 - Suivre une requête.");
            out.println("3 - Voir les candidatures relative à une requête.");
            out.println("4 - Valider une candidature");
            out.println("5 - Retour à la page précédente.");
            scanner.reset();

            switch (scanner.nextLine()) {
                case "1":
                    String titre = saisirAttribut(scanner,"un titre");
                    String desc = saisirAttribut(scanner,"une description");
                    TypeTravail travail = choixType(scanner);
                    LocalDate debut = saisirDate(scanner, "date de début");
                    resident.soumettreRequete(titre,desc,travail,debut);
                    break;
                case "2":
                    int id = saisirValeur(scanner,"l'id de la requête recherchée");
                    if (id == 0){
                        break;
                    }
                    resident.suiviRequete(id);
                    break;
                case "3":
                    int idx  = saisirValeur(scanner,"l'id de la requête dont vous désirez voir les candidatures");
                    if (idx == 0){
                        break;
                    }
                    resident.voirCandidatures(idx);
                    break;
                case "4":
                    int valide = saisirValeur(scanner,"l'id de la candidature à valider");
                    if (valide == 0){
                        break;
                    }
                    String nom = saisirAttribut(scanner, "le nom de l'intervenant ayant deposer la candidature");

                    out.println("Voulez vous ajouter un message? (Oui/Non)");
                    String choix = scanner.nextLine();

                    if (choix.equalsIgnoreCase("oui")){
                        out.println("Veuiller saisir votre message.");
                        String message = scanner.nextLine();
                        resident.validationCandidature(nom,valide,message);
                    }else if(choix.equalsIgnoreCase("non")) {
                        out.println("Vous avez choisi de ne pas ajouter de message.");
                        String msg = "Votre candidature a été accepter pour cette requête.";
                        resident.validationCandidature(nom,valide,msg);
                    } else {
                        out.println("Option invalide");
                    }
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    public void planification(Scanner scanner, MaVilleResident resident){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Ajouter une plage horaire.");
            out.println("2 - Consulter les propositions de plages horaires dans votre quartier.");
            out.println("3 - Retour à la page précédente.");

            switch (scanner.nextLine()) {
                case "1":
                    LocalDate debut = saisirDate(scanner,"date de début");
                    LocalDate fin = saisirDate(scanner, "date de fin");
                    resident.ajouterPlageHoraire(debut,fin);
                    out.println("La plage horaire : " + debut + " - " + fin +" a bien été ajouté aux propositions" +
                            "de votre quartier.");
                    break;
                case "2":
                    resident.voirPlagesHoraires();
                    break;

                case "3":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }

    }

    public void notifications(Scanner scanner, MaVilleResident resident){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Acceder à vos notifications.");
            out.println("2 - S'abonner à des notification d'un autre quartier.");
            out.println("3 - Retour à la page précédente.");

            switch (scanner.nextLine()) {
                case "1":
                    resident.voirNotification();
                    break;
                case "2":
                    List<Quartier> choix = choixQuartiers(scanner);
                    resident.ajouterNotification(choix);
                    break;
                case "3":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    public void gestionCompte(Scanner scanner, MaVilleResident resident){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Voir les information de votre compte.");
            out.println("2 - Supprimer votre compte.");
            out.println("3 - Retour à la page précédente.");

            switch (scanner.nextLine()) {
                case "1":
                    out.println(resident);
                    break;
                case "2":
                    out.println("Cette étape est irreversible. Etes vous sûr de vouloir continuer? (Oui/Non)");
                    String choix = scanner.nextLine();

                    if (choix.equalsIgnoreCase("oui")){
                        resident.supprimerCompte();
                        out.println("Suppression du compte");
                        exit = true;
                    }else if(choix.equalsIgnoreCase("non")) {
                        out.println("Vous avez choisi de ne pas continuer avec la suppression");
                    } else {
                        out.println("Option invalide");
                    }
                    break;

                case "3":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    // Méthodes relatives aux intervenants.

    public void gestionCandidature(Scanner scanner, MaVilleIntervenant user){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Soumettre une candidature pour une requête.");
            out.println("2 - Suivre des candidature.");
            out.println("3 - Soustraire une candidature.");
            out.println("4 - Retour à la page précédente.");
            scanner.reset();

            switch (scanner.nextLine()) {
                case "1":
                    int id  = saisirValeur(scanner,"l'id de la requête pour laquelle vous voulez postuler");
                    if (id == 0){
                        break;
                    }
                    LocalDate debut = saisirDate(scanner,"date de debut");
                    LocalDate fin = saisirDate(scanner,"date de fin");
                    user.soumettreCandidature(id,debut,fin);
                    out.println("Candidature soumise.");
                    break;
                case "2":
                    List<Candidature> deposer = user.voirCandidatures();
                    if (deposer.isEmpty()){
                        out.println("Aucune candidature n'a été déposer.");
                    } else {
                       out.println("Les candidatures déposées sont : \n" + deposer);
                    }
                    break;
                case "3":
                    int idx = saisirValeur(scanner,"l'id de la candidature rechercher");
                    if (idx == 0){
                        break;
                    }
                    user.sousttraireCandidature(idx);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    public void gestionProjet(Scanner scanner, MaVilleIntervenant user){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Soumettre un nouveau projet.");
            out.println("2 - Mettre à jour un projet.");
            out.println("3 - Retour à la page précédente.");

            switch (scanner.nextLine()) {
                case "1":
                    String titre = saisirAttribut(scanner,"un titre");
                    String desc = saisirAttribut(scanner,"une description");
                    TypeTravail type = choixType(scanner);
                    List<Quartier> quartiers = choixQuartiers(scanner);
                    LocalDate debut = saisirDate(scanner, "date de debut");
                    LocalDate fin = saisirDate(scanner, "date de fin");
                    String horaire  = saisirAttribut(scanner, "un horaire");
                    user.soumettreTravail(titre,desc,type,quartiers,debut,fin,horaire);
                    break;

                case "2":
                    int id = saisirValeur(scanner, "l'id du projet à modifier");
                    scanner.reset();
                    if (id == 0){
                        break;
                    }

                    boolean sortie = false;

                    while (!sortie){
                        out.println("Veuillez choisi laquelle des informations vous voulez modifier");
                        out.println("1 - Mettre à jour la description.");
                        out.println("2 - Mettre à jour la date de fin.");
                        out.println("3 - Mettre à jour le statue.");
                        out.println("4 - Retour à la page précédente.");

                        String choix = scanner.nextLine();
                        switch (choix){
                            case "1":
                                user.miseAJourDesc(id,saisirAttribut(scanner,"une nouvelle description"));
                                break;
                            case "2":
                                user.miseAJourDateFin(id,saisirDate(scanner,"nouvelle date de fin"));
                                break;
                            case "3":
                                user.miseAJourStatut(id,choixStatut(scanner));
                                break;
                            case "4":
                                sortie = true;
                                break;
                            default:
                                out.println("Option non valide, veuillez réessayer.");
                        }
                    }
                    break;

                case "3":
                    exit = true;
                    break;

                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    public void gestionCompte(Scanner scanner, MaVilleIntervenant user){
        boolean exit = false;

        while (!exit) {
            out.println("1 - Voir les information de votre compte.");
            out.println("2 - Supprimer votre compte.");
            out.println("3 - Retour à la page précédente.");

            switch (scanner.nextLine()) {
                case "1":
                    out.println(user);
                    break;
                case "2":
                    out.println("Cette étape est irreversible. Etes vous sûr de vouloir continuer? (Oui/Non)");
                    String choix = scanner.nextLine();

                    if (choix.equalsIgnoreCase("oui")){
                        user.supprimerCompte();
                        out.println("Suppression du compte");
                        exit = true;
                    }else if(choix.equalsIgnoreCase("non")) {
                        out.println("Vous avez choisi de ne pas continuer avec la suppression");
                    } else {
                        out.println("Option invalide");
                    }
                    break;

                case "3":
                    exit = true;
                    break;
                default:
                    out.println("Option non valide, veuillez réessayer.");
            }
        }
    }

    // Méthodes pour faciliter la saisie des données.

    public String saisirAttribut(Scanner scanner, String string){
        String valeur;

        while (true) {
            out.println("* Veuiller entrer " + string + " :");
            valeur = scanner.nextLine();
            if (!valeur.trim().isEmpty()) {
                break;
            } else {
                out.println("Cette case est obligatoire.");
            }
        }

        return valeur;
    }

    public int saisirValeur(Scanner scanner, String string){
        int valeur = 0;

        try {
            out.println("Veuillez entrez " + string + " :");
            valeur = scanner.nextInt();
        } catch (InputMismatchException e) {
            out.println("Format incorrect. Veuillez entrer un chiffre.");
        }

        return valeur;
    }

    public LocalDate saisirDate(Scanner scanner, String string){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date;

        while (true) {
            try {
                out.println("Veuillez entrez une " + string + "(format yyyy/mm/dd) :");

                String date1 = scanner.nextLine();
                date = LocalDate.parse(date1, formatter);

                if (!date1.trim().isEmpty()) {
                    break;

                } else {
                    out.println("Cette case est obligatoire.");
                }
            } catch (DateTimeParseException e) {
                out.println("Format de date incorrect. Veuillez utiliser le format yyyy/MM/dd.");
            }
        }

        return date;
    }

    public TypeTravail choixType(Scanner scanner){
        TypeTravail travail = null;

        while (true) {
            out.println("* Veuiller chosir un type de travail parmi ceux cités ci-dessous : \n");
            out.println(Arrays.toString(TypeTravail.values()));
            boolean found = false;

            String type = scanner.nextLine();

            for (TypeTravail var : TypeTravail.values()) {
                if (type.equalsIgnoreCase(var.toString())) {
                    travail = var;
                    found = true;
                    break;
                }
            }

            if (!found) {
                out.println("Type introuvable, veuiller réessayer.");
            }else{
                break;
            }
        }

        return travail;

    }

    public Statut choixStatut(Scanner scanner){
        Statut statut = null ;

        while (true) {
            out.println("* Veuiller chosir un statut parmi ceux cités ci-dessous : \n");
            out.println(Arrays.toString(Statut.values()));
            boolean found = false;

            String type = scanner.nextLine();

            for (Statut var : Statut.values()) {
                if (type.equalsIgnoreCase(var.toString())) {
                    statut = var;
                    found = true;
                    break;
                }
            }

            if (!found) {
                out.println("Type introuvable, veuiller réessayer.");
            }else{
                break;
            }
        }

        return statut;
    }

    public Quartier choixQuartier(Scanner scanner){
        Quartier quartier = null;

        while (true) {
            out.println("* Veuiller chosir un quartier parmi ceux cités ci-dessous :\n");
            out.println(Arrays.toString(Quartier.values()));
            boolean found = false;

            String type = scanner.nextLine();

            for (Quartier var : Quartier.values()) {
                if (type.equalsIgnoreCase(var.toString())) {
                    quartier = var;
                    found = true;
                    break;
                }
            }

            if (!found) {
                out.println("Quartier introuvable, veuiller réessayer.");
            } else {
                break;
            }
        }

        return quartier;
    }

    public List<Quartier> choixQuartiers(Scanner scanner){
        List<Quartier> quartiers = new ArrayList<>();

        while (true) {
            out.println("* Veuiller choisir un quartier (SAISIR EXIT POUR QUITTER): \n");
            out.println(Arrays.toString(Quartier.values()));
            boolean found = false;

            String lieu = scanner.nextLine();

            if (!lieu.equalsIgnoreCase("EXIT")){
                for (Quartier var : Quartier.values()) {
                    if (lieu.equalsIgnoreCase(var.toString())) {
                        quartiers.add(var);
                        found = true;
                        out.println(var + " a été ajouté.");
                        break;
                    }
                }

                if (!found) {
                    out.println("Quartier introuvable, veuiller réessayer.");
                }

            }else{
                break;
            }
        }

        return quartiers;
    }

    public List<Object> choixCriteres(Scanner scanner){
        List<Object> criteres = new ArrayList<>();

        while (true) {
            out.println("* Veuiller choisir un critère selon lequel vous voulez effectuer votre recherche (SAISIR EXIT POUR QUITTER): \n");
            out.println(Arrays.toString(CritereTravaux.values()));
            boolean found = false;

            String choix = scanner.nextLine();

            if (!choix.equalsIgnoreCase("EXIT")){
                for (CritereTravaux critere : CritereTravaux.values()) {
                    if (choix.equalsIgnoreCase(critere.toString())) {

                        if (critere.equals(CritereTravaux.TYPE)){
                            TypeTravail type = choixType(scanner);
                            criteres.add(type);
                        }else if(critere.equals(CritereTravaux.TITRE)) {
                            String titre = saisirAttribut(scanner, "un titre");
                            criteres.add(titre);
                        }else if(critere.equals(CritereTravaux.QUARTIER)) {
                            Quartier quartier = choixQuartier(scanner);
                            criteres.add(quartier);
                        }else{
                            LocalDate debut = saisirDate(scanner, "date de début");
                            criteres.add(debut);
                        }

                        found = true;
                        break;
                    }
                }
                if (!found) {
                    out.println("Ce critère n'est pas listé, veuiller réessayer.");
                }

            }else{
                break;
            }
        }

        return criteres;

    }

}