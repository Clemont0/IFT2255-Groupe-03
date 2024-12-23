import java.util.Scanner;

public class Acceuil {

    static Utilisateur resident = new Resident();
    static Utilisateur intervenant = new Intervenant();

    public static String separator = "################################################################################";

    // Menu principal
    public static void menuAcceuil() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            afficherMenu();  // Afficher le menu principal
            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    resident.connection(scanner);
                    break;
                case "2":
                    intervenant.connection(scanner);
                    break;
                case "3":
                    resident.inscription(scanner);
                    break;
                case "4":
                    intervenant.inscription(scanner);
                    break;
                case "EXIT":
                    System.out.println("Fermeture de l'application.");
                    exit = true;
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Menu d'accueil pour les résidents
    public static void afficherAcceuilResidents(Resident resident) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exitResidentMenu = false;

        while (!exitResidentMenu) {
            System.out.println(separator);
            System.out.println("#################### Choisissez les options ######################");
            System.out.println("1 - Afficher la liste des travaux en cours");
            System.out.println("2 - Requête de travail");
            System.out.println("3 - Signaler un problème");
            System.out.println("4 - Planification participative");
            System.out.println("5 - Gérer les notifications");
            System.out.println("EXIT - Retour au menu principal");

            String choix = scanner.nextLine();  // Lire l'entrée utilisateur

            switch (choix) {
                case "1":
                    afficherListeTravaux();
                    break;
                case "2":
                    afficherSousMenuRequetes(resident);
                    break;
                case "3":
                    resident.signalerProbleme(scanner);
                    break;
                case "4":
                    afficherSousMenuPlanification(resident);
                    break;
                case "5":
                    resident.gererNotif(scanner);
                    break;
                case "EXIT":
                    System.out.println("Retour au menu principal.");
                    exitResidentMenu = true;  // Revenir au menu principal
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Menu d'accueil pour les intervenants
    public static void afficherAcceuilIntervenant(Intervenant intervenant) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exitIntervenantMenu = false;

        while (!exitIntervenantMenu) {
            System.out.println(separator);
            System.out.println("#################### Choisissez les options ######################");
            System.out.println("1 - Afficher la liste des requêtes en cours");
            System.out.println("2 - Soumettre une requête de travail");
            System.out.println("3 - Mettre à jour les infos de chantier");
            System.out.println("EXIT - Retour au menu principal");

            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    Travaux.afficherTravauxIntervenant();
                    break;
                case "2":
                    intervenant.soummettre();
                    break;
                case "3":
                    System.out.println("RIEN À AFFICHER POUR LE MOMENT.");
                    break;
                case "EXIT":
                    System.out.println("Retour au menu principal.");
                    exitIntervenantMenu = true;
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Méthode d'affichage des travaux
    private static void afficherListeTravaux() throws InterruptedException {
        Travaux listeTravaux = new Travaux();
        listeTravaux.ajouterTravail("Réfection de la rue Saint-Denis");
        listeTravaux.ajouterTravail("Construction d'une nouvelle piste cyclable sur le boulevard De Maisonneuve");
        listeTravaux.afficherTravauxResident();
    }

    // Sous-menu des requêtes de travail
    private static void afficherSousMenuRequetes(Resident resident) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exitSousMenuRequete = false;

        while (!exitSousMenuRequete) {
            System.out.println("#################### Requête de travail ######################");
            System.out.println("1 - Soumettre une requête de travail");
            System.out.println("2 - Afficher les requêtes en cours");
            System.out.println("EXIT - Retour au menu résident");

            String choixSousMenu = scanner.nextLine();

            switch (choixSousMenu) {
                case "1":
                    resident.soummettre();
                    break;
                case "2":
                    System.out.println("- Réfection de la rue Sainte-Catherine, 2024/12/30");
                    System.out.println("- Travaux sur le Boulevard Pie-IX, 2025/01/14");
                    break;
                case "EXIT":
                    exitSousMenuRequete = true;  // Retour au menu résident
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Sous-menu de planification participative
    private static void afficherSousMenuPlanification(Resident resident) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean exitSousMenuPlanification = false;

        while (!exitSousMenuPlanification) {
            System.out.println("#################### Planification participative ######################");
            System.out.println("1 - Envoyer ses préférences de plage horaire");
            System.out.println("2 - Envoyer son avis sur les travaux terminés");
            System.out.println("EXIT - Retour au menu résident");

            String choixSousMenu = scanner.nextLine();

            switch (choixSousMenu) {
                case "1":
                    System.out.println("Fonctionnalité non disponible.");
                    break;
                case "2":
                    System.out.println("Fonctionnalité non disponible.");
                    break;
                case "EXIT":
                    exitSousMenuPlanification = true;  // Retour au menu résident
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
                    break;
            }
        }
    }

    // Méthode d'affichage du menu principal
    public static void afficherMenu() {
        System.out.println(separator);
        System.out.println("     M     M    AAAAA    V     V    IIIII   L         L         EEEEE ");
        System.out.println("     MM   MM   A     A   V     V      I     L         L         E     ");
        System.out.println("     M M M M   AAAAAAA    V   V       I     L         L         EEEEE ");
        System.out.println("     M  M  M   A     A     V V        I     L         L         E     ");
        System.out.println("     M     M   A     A      V       IIIII   LLLLLL    LLLLLL    EEEEE ");
        System.out.println("\n---------------------- Bienvenue sur Maville! -----------------------");
        System.out.println("\n1 - Connexion résident");
        System.out.println("2 - Connexion intervenant");
        System.out.println("3 - Créer un compte résident");
        System.out.println("4 - Créer un compte intervenant");
        System.out.println("EXIT - Quitter l'application");
        System.out.print("\nVeuillez sélectionner une option:\n");
    }
}