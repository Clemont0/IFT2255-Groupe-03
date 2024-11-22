package org.example;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class MaVilleResident extends MaVille {
    public static void soummettreRequete() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez l'ID de la requête :");
        String id = scanner.nextLine();

        System.out.println("Entrez le titre de la requête :");
        String titre = scanner.nextLine();

        if (titre.length() <= 3) {
            System.out.println("Le titre doit comporter plus de 3 caractères.");
            return;
        }

        System.out.println("Entrez la description :");
        String description = scanner.nextLine();

        if (description.length() <= 10) {
            System.out.println("La description doit comporter plus de 10 caractères.");
            return;
        }

        System.out.println("Entrez le type :");
        String type = scanner.nextLine();

        System.out.println("Entrez la date de début (yyyy-mm-dd) :");
        String dateDebut = scanner.nextLine();

        if (!isValidDate(dateDebut)) {
            System.out.println("La date de début n'est pas valide. Format attendu : yyyy-mm-dd.");
            return;
        }

        System.out.println("Entrez la date de fin (yyyy-mm-dd) :");
        String dateFin = scanner.nextLine();

        if (!isValidDate(dateFin)) {
            System.out.println("La date de fin n'est pas valide. Format attendu : yyyy-mm-dd.");
            return;
        }
        String jsonRequete = String.format(
                "{\"id\":\"%s\", \"titre\":\"%s\", \"description\":\"%s\", \"type\":\"%s\", \"dateDebut\":\"%s\", \"dateFin\":\"%s\"}",
                id, titre, description, type, dateDebut, dateFin
        );

        try {
            URL url = new URL("http://localhost:7000/ajouter-requete");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequete.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                System.out.println("Requête ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la requête. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    public static void afficherTravaux() {
        try {
            String response = HTTPClient.get("http://localhost:7000/travaux");

            List<Travail> travaux = JSONParser.parseTravaux(response);

            System.out.println("Travaux récupérés:");
            for (Travail travail : travaux) {
                System.out.println("ID: " + travail.getId());
                System.out.println("Quartier: " + travail.getBoroughId());
                System.out.println("Statut: " + travail.getCurrentStatus());
                System.out.println("Raison: " + travail.getReasonCategory());
                System.out.println("Soumetteur: " + travail.getSubmitterCategory());
                System.out.println("Organisation: " + travail.getOrganizationName());
                System.out.println("-------------------------------");
            }

            afficherSousMenuFiltre(travaux);


        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des travaux.");
        }
    }

    public static void afficherEntraves() {
        try {
            String response = HTTPClient.get("http://localhost:7000/entraves");

            List<Entraves> entraves = JSONParser.parseEntrave(response);

            System.out.println("Entraves récupérées:");
            for (Entraves entrave : entraves) {
                System.out.println("ID Demande: " + entrave.getIdRequest());
                System.out.println("Rue: " + entrave.getStreetId());
                System.out.println("Nom court: " + entrave.getShortName());
                System.out.println("Largeur de l'impact: " + entrave.getStreetImpactType());
                System.out.println(STR."Type d'impact sur la rue: \{entrave.getStreetImpactType()}");
                System.out.println("-------------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des entraves.");
            e.printStackTrace();
        }
    }

    public static List<Travail> filtrerQuartier(List<Travail> travaux, String quartier) {
        return travaux.stream()
                .filter(travail -> quartier.equalsIgnoreCase(travail.getBoroughId()))
                .collect(Collectors.toList());
    }

    public static List<Travail> filtrerParType(List<Travail> travaux, String type) {
        return travaux.stream()
                .filter(travail -> type.equalsIgnoreCase(travail.getReasonCategory()))
                .collect(Collectors.toList());
    }

    public static void afficherSousMenuFiltre(List<Travail> travaux) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSous-menu :");
            System.out.println("1. Filtrer les travaux par quartier");
            System.out.println("2. Filtrer les travaux par type");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    System.out.print("Entrez le nom du quartier : ");
                    String quartier = scanner.nextLine();

                    List<Travail> travauxFiltresQuartier = filtrerQuartier(travaux, quartier);
                    if (travauxFiltresQuartier.isEmpty()) {
                        System.out.println("Aucun travail trouvé pour le quartier : " + quartier);
                    } else {
                        System.out.println("Travaux dans le quartier " + quartier + " :");
                        afficherListeTravaux(travauxFiltresQuartier);
                    }
                    break;

                case 2:
                    System.out.print("Entrez le type de travaux (raison) : ");
                    String type = scanner.nextLine();

                    // Filtrer les travaux par le type spécifié
                    List<Travail> travauxFiltresType = filtrerParType(travaux, type);
                    if (travauxFiltresType.isEmpty()) {
                        System.out.println("Aucun travail trouvé pour le type : " + type);
                    } else {
                        System.out.println("Travaux pour le type " + type + " :");
                        afficherListeTravaux(travauxFiltresType);
                    }
                    break;

                case 0:
                    System.out.println("Retour au menu principal.");
                    return;

                default:
                    System.out.println("Choix invalide. Réessayez.");
            }
        }
    }

    static void afficherMenuResident() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu résident :");
            System.out.println("1. Consulter les travaux en cours ou à venir");
            System.out.println("2. Consulter les entraves");
            System.out.println("3. Soumettre une requête");
            System.out.println("4. Rechercher des travaux");
            System.out.println("5. Recevoir des notifications");
            System.out.println("6. Permettre une planification participative");
            System.out.println("7. Quitter");
            System.out.print("Veuillez choisir une option : ");
            try {
                int choix = Integer.parseInt(scanner.nextLine());

                if (choix == 1) {
                    afficherTravaux();
                } else if (choix == 2) {
                    afficherEntraves();
                } else if (choix == 3) {
                    soummettreRequete();
                } else if (choix == 4) {
                    System.out.println("Pas implementer");
                } else if (choix == 5) {
                    System.out.println("Pas implementer");
                } else if (choix == 6) {
                    System.out.println("Pas implementer");
                } else if (choix == 7){
                    System.out.println("Au revoir!");
                    break;
                } else {
                    System.out.println(" Option non valide...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    public static void afficherListeTravaux(List<Travail> travaux) {
        for (Travail travail : travaux) {
            System.out.println("ID: " + travail.getId());
            System.out.println("Quartier: " + travail.getBoroughId());
            System.out.println("Statut: " + travail.getCurrentStatus());
            System.out.println("Type: " + travail.getReasonCategory());
            System.out.println("Soumetteur: " + travail.getSubmitterCategory());
            System.out.println("Organisation: " + travail.getOrganizationName());
            System.out.println("-------------------------------");
        }
    }

}





