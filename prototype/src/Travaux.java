import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Travaux {

    private static List<String> listeTravaux;

    public Travaux() {
        listeTravaux = new ArrayList<>();

    }


    public void ajouterTravail(String travail) {
        listeTravaux.add(travail);
    }



    public static void afficherTravauxResident() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);


        if (listeTravaux.isEmpty()) {
            System.out.println("Aucun travail en cours.");
            return;
        }

        System.out.println("Travaux en cours dans la municipalité de Montréal :");

        for (int i = 0; i < listeTravaux.size(); i++) {
            System.out.println((i + 1) + ". " + listeTravaux.get(i));
        }

        while (true) {
            System.out.println("Tapez 'EXIT' pour revenir au menu.");
            String choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous revenez au menu.");
                Acceuil.afficherAcceuilResidents((Resident) Acceuil.resident);
                return;
            } else {
                System.out.println("Choix invalide. Veuillez taper 'EXIT' pour revenir au menu.");
            }
        }
    }

    public static void afficherTravauxIntervenant() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);


        if (listeTravaux.isEmpty()) {
            System.out.println("Aucun travail en cours.");
            return;
        }

        System.out.println("Travaux en cours dans la municipalité de Montréal :");

        for (int i = 0; i < listeTravaux.size(); i++) {
            System.out.println((i + 1) + ". " + listeTravaux.get(i));
        }

        while (true) {
            System.out.println("Tapez 'EXIT' pour revenir au menu.");
            String choix = scanner.nextLine();

            if (choix.equalsIgnoreCase("EXIT")) {
                System.out.println("Vous revenez au menu.");
                Acceuil.afficherAcceuilIntervenant((Intervenant) Acceuil.intervenant);
                return;
            } else {
                System.out.println("Choix invalide. Veuillez taper 'EXIT' pour revenir au menu.");
            }
        }
    }




    public static List<String> getListeTravaux() {
        return listeTravaux;
    }




}