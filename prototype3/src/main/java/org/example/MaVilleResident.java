package org.example;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.ServerApp.*;


public class MaVilleResident extends MaVille {
    private static final int port = 8000;

    public static void soummettreRequete() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

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

        System.out.println(
                "1. Travaux routiers\n" +
                "2. Travaux de gaz ou électricité\n" +
                "3. Construction ou rénovation\n" +
                "4. Entretien paysager\n" +
                "5. Travaux liés aux transports en commun\n" +
                "6. Travaux de signalisation et éclairage\n" +
                "7. Travaux souterrains\n" +
                "8. Travaux résidentiel\n" +
                "9. Entretien urbain\n" +
                "10. Entretien des réseaux de télécommunication"
        );
        System.out.print("Entrez le type du travail : ");
        String type;
        int opt = Integer.parseInt(scanner.nextLine());
        if (opt < 1 || opt > 10) {
            type = "Type invalide";
        } else {
            type = new TypeTravaux().getTypes().get(opt - 1);
        }

        System.out.println("Entrez la date de début (yyyy-mm-dd) :");
        String dateDebut = scanner.nextLine();

        System.out.println("Entrez la date de fin (yyyy-mm-dd) :");
        String dateFin = scanner.nextLine();

        if (!isLaterDate(dateDebut, dateFin)) {return;}

        int id = Id.nextRequeteId();
        String jsonRequete = String.format(
                "{\"id\":%d, \"titre\":\"%s\", \"description\":\"%s\", \"type\":\"%s\", \"dateDebut\":\"%s\", \"dateFin\":\"%s\"}",
                id, titre, description, type, dateDebut, dateFin
        );
        try {
            URL url = new URL("http://localhost:" + port + "/ajouter-requete");
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

    public static void afficherCandidatures() throws IOException {
        Scanner scanner = new Scanner(System.in);
        MaVilleIntervenant.voirRequetes();
        System.out.print("Veuiller entrer l'ID de la requête dont vous souhaitez consulter les candidatures : ");
        String id = scanner.nextLine();
        int count = 1;

        if(ServerApp.chargerCandidatures().isEmpty()) {
            System.out.println("Aucune candidature n'a été soumise dans le système.");
        }

        System.out.println("\n--- Liste des candidatures ---\n");
        for(Map<String, Object> candidature : ServerApp.chargerCandidatures()) {
            if(candidature.get("Id_Requete").equals(id)){
                System.out.println(count +".");
                for (String key : candidature.keySet()) {
                    System.out.println(key + ": " + candidature.get(key).toString());
                }
                count++;
            }
        }
    }

    public static void validerCandidature() throws IOException{
        afficherCandidatures();

        boolean found = false;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuiller saisir le numero correspondant à la candidature à valider : ");
        String id = scanner.nextLine();

        for(Map<String,Object> candidature : ServerApp.chargerCandidatures()){
            if (Integer.parseInt(id)-1 == ServerApp.chargerCandidatures().indexOf(candidature)){
                found = true;
                System.out.println("Voulez vous laisser un message ? (OUI/NON) :");
                String choix = scanner.nextLine();
                if(choix.equalsIgnoreCase("OUI")){
                    System.out.println("Veuiller saisir le message :");
                    String msg = scanner.nextLine();
                    String jsonRequete = String.format(
                            "{\"idx\":%d,\"msg\":\"%s\"}",
                            Integer.parseInt(id),msg
                    );
                    try {
                        URL url = new URL("http://localhost:" + port + "/message-candidature");
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
                            System.out.println("Candidature validée avec succès !");
                        } else {
                            System.out.println("Erreur lors de la validation de la candidature. Code : " + responseCode);
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Vous avez choisi de ne pas ajouté de message.");
                }
                try {
                    URL url = new URL("http://localhost:" + port + "/valider-candidature");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(id.getBytes());
                        os.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201) {
                        System.out.println("Candidature validée avec succès !");
                        break;
                    } else {
                        System.out.println("Erreur lors de la validation de la candidature. Code : " + responseCode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                String position = String.valueOf(ServerApp.chargerCandidatures().indexOf(candidature));
                try {
                    URL url = new URL("http://localhost:" + port + "/rejetter-candidature");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(position.getBytes());
                        os.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (!(responseCode == 201)) {
                        System.out.println("Erreur lors du rejet de la candidature. Code : " + responseCode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(!found){
            System.out.println("Il n'existe aucune candidature associée à ce numméro.");
        }
    }

    public static void afficherTravaux() {
        try {
            String response = HTTPClient.get("http://localhost:" + port + "/travaux");

            List<Travail> travaux = JSONParser.parseTravaux(response);

            System.out.println("Travaux récupérés:");
            for (Travail travail : travaux) {
                System.out.println("ID: " + travail.getId());
                System.out.println("Quartier: " + travail.getBoroughId());
                System.out.println("Statut: " + travail.getCurrentStatus());
                System.out.println("Raison: " + travail.getReasonCategory());
                System.out.println("Soumetteur: " + travail.getSubmitterCategory());
                System.out.println("Organisation: " + travail.getOrganizationName());
                System.out.println("Date de fin" + travail.getduration_start_date());
                System.out.println("-------------------------------");
            }

            afficherSousMenuFiltre(travaux);


        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des travaux.");
        }
    }

    public static void afficherEntraves() {
        try {
            String response = HTTPClient.get("http://localhost:" + port + "/entraves");

            List<Entraves> entraves = JSONParser.parseEntrave(response);

            System.out.println("Entraves récupérées:");
            for (Entraves entrave : entraves) {
                System.out.println("ID Demande: " + entrave.getIdRequest());
                System.out.println("Rue: " + entrave.getStreetId());
                System.out.println("Nom court: " + entrave.getShortName());
                System.out.println("Largeur de l'impact: " + entrave.getStreetImpactType());
                System.out.println("Type d'impact sur la rue: " + entrave.getStreetImpactType());
                System.out.println("-------------------------------");
            }
            afficherSousMenuEntraves(entraves);

        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des entraves.");
            e.printStackTrace();
        }
    }

    public static List<Travail> filtrer3mois(List<Travail> travaux) {
        List<Travail> travauxProchainsTroisMois = new ArrayList<>();
        LocalDate aujourdHui = LocalDate.now();
        LocalDate dansTroisMois = aujourdHui.plusMonths(3);
        for (Travail travail : travaux) {
            LocalDate dateFin = LocalDate.parse(travail.getduration_start_date().substring(0, 10));
            if ((dateFin.isAfter(aujourdHui) || dateFin.isEqual(aujourdHui)) && dateFin.isBefore(dansTroisMois)) {
                travauxProchainsTroisMois.add(travail);
            }
        }
        return travauxProchainsTroisMois;
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
    public static List<Entraves> filtrerParRue(List<Entraves> entraves, String rue) {
        return entraves.stream()
                .filter(entrave -> rue.equalsIgnoreCase(entrave.getShortName()))
                .collect(Collectors.toList());
    }

    public static void afficherSousMenuEntraves(List<Entraves> entraves) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSous-menu des entraves :");
            System.out.println("1. Filtrer les entraves par rue");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            int choix = -1;
            while (choix == -1) {
                try {
                    choix = scanner.nextInt();
                    scanner.nextLine();
                    if (choix < 0 || choix > 1) {
                        System.out.println("Choix invalide. Veuillez entrer 0 ou 1.");
                        choix = -1;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                    scanner.nextLine();
                }
            }
            switch (choix) {
                case 1:
                    System.out.print("Entrez le nom court de la rue : ");
                    String rue = scanner.nextLine();
                    List<Entraves> entravesFiltreesRue = filtrerParRue(entraves, rue);
                    if (entravesFiltreesRue.isEmpty()) {
                        System.out.println("Aucune entrave trouvée pour la rue : " + rue);
                    } else {
                        System.out.println("Entraves dans la rue " + rue + " :");
                        afficherListeEntraves(entravesFiltreesRue);
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

    public static void afficherSousMenuFiltre(List<Travail> travaux) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSous-menu :");
            System.out.println("1. Filtrer les travaux par quartier");
            System.out.println("2. Filtrer les travaux par type");
            System.out.println("3. Filtrer les travaux pour les 3 mois à venir");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            int choix = -1;
            while (choix == -1) {
                try {
                    choix = scanner.nextInt();
                    scanner.nextLine();
                    if (choix < 0 || choix > 3) {
                        System.out.println("Choix invalide. Veuillez entrer un nombre entre 0 et 3.");
                        choix = -1;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
                    scanner.nextLine();
                }
            }
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
                    List<Travail> travauxFiltresType = filtrerParType(travaux, type);
                    if (travauxFiltresType.isEmpty()) {
                        System.out.println("Aucun travail trouvé pour le type : " + type);
                    } else {
                        System.out.println("Travaux pour le type " + type + " :");
                        afficherListeTravaux(travauxFiltresType);
                    }
                    break;
                case 3:
                    MaVilleResident resident = new MaVilleResident();
                    List<Travail> travauxTroisMois = resident.filtrer3mois(travaux);
                    if (travauxTroisMois.isEmpty()) {
                        System.out.println("Aucun travail prévu pour les 3 mois à venir.");
                    } else {
                        System.out.println("Travaux prévus pour les 3 mois à venir :");
                        afficherListeTravaux(travauxTroisMois);
                        voirProjets();
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


    public static List<Map<String, Object>> getNotifications(String email) throws JsonProcessingException {
        Map<String, List<Map<String, Object>>> users = ServerApp.getUsers();
        List<Map<String, Object>> residents = users.get("residents");
        Map<String, Object> user = null;
        for (Map<String, Object> u : residents) {
            if (u.get("courriel").equals(email)) {user = u;}
        }
        if (user == null) {
            System.out.println("Utilisateur introuvable");
            return null;
        }
        String ns = user.get("notifications").toString();
        List<Map<String, Object>> notif = new ObjectMapper().readValue(parseStringAsJSON(ns), new TypeReference<>() {});
        return notif;
    }

    static void afficherNotifications(List<Map<String, Object>> notif) {
        System.out.println();
        if (notif == null) {
            System.out.println("Aucune notification");
            System.out.println();
            return;
        }
        for (Map<String, Object> n : notif) {
            System.out.println("Identifiant du projet: " + n.get("projetId"));
            System.out.println("Message: " + n.get("message"));
            System.out.println();
        }
    }

    static void afficherMenuResident(String email) throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            List<Map<String, Object>> notif = getNotifications(email);
            int numNotifs = notif == null ? 0 : notif.size();
            System.out.println("\nMenu résident :");
            System.out.println("1. Consulter les travaux en cours ou à venir");
            System.out.println("2. Consulter les entraves");
            System.out.println("3. Soumettre une requête");
            System.out.println("4. Consulter les candidatures en lien avec une requête");
            System.out.println("5. Valider une candidature");
            System.out.println("6. Notifications (" + numNotifs + ")");
            System.out.println("7. Permettre une planification participative");
            System.out.println("8. Quitter");
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
                    afficherCandidatures();
                } else if (choix == 5) {
                    validerCandidature();
                } else if (choix == 6) {
                    afficherNotifications(notif);
                } else if (choix == 7) {
                afficherSousMenuPlages();
            } else if (choix == 8){
                    System.out.println("Au revoir!");
                    break;
                } else {
                    System.out.println(" Option non valide...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void afficherListeEntraves(List<Entraves> entraves) {
        for (Entraves entrave : entraves) {
                System.out.println("ID Demande: " + entrave.getIdRequest());
                System.out.println("Rue: " + entrave.getStreetId());
                System.out.println("Nom court: " + entrave.getShortName());
                System.out.println("Largeur de l'impact: " + entrave.getStreetImpactType());
                System.out.println("Type d'impact sur la rue: " + entrave.getStreetImpactType());
                System.out.println("-------------------------------");
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

    public static void inscription() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Nom : ");
            String name = scanner.nextLine();

            System.out.println("Date de naissance (yyyy-mm-dd) : ");
            String birthDate = scanner.nextLine();

            if (!isValidDate(birthDate)) {
                System.out.println("La date de fin n'est pas valide. Format attendu : yyyy-mm-dd.");
                return;
            }

            System.out.println("Courriel : ");
            String courriel = scanner.nextLine();
            boolean isValidEmail = MaVille.validate(courriel);
            if (!isValidEmail) return;

            System.out.println("Mot de passe : ");
            String mdp = scanner.nextLine();

            System.out.println("Téléphone : ");
            String phone = scanner.nextLine();
            if (phone.isBlank()) {phone = "";}

            System.out.println("Adresse résidentielle : ");
            String adresse = scanner.nextLine();

            System.out.println("Code postal : ");
            String codePostal = scanner.nextLine();

            String quartier = Quartiers.getQuartierFromCP(codePostal);

            String json = String.format("{\"nom\":\"%s\", \"dateDeNaissance\":\"%s\", \"courriel\":\"%s\"," +
                            " \"mdp\":\"%s\", \"phone\":\"%s\", \"adresse\":\"%s\", \"postalCode\":\"%s\"," +
                            " \"quartier\":\"%s\", \"notifications\":[]}",
                    name, birthDate, courriel, mdp, phone, adresse, codePostal, quartier
            );
            try {
                URL url = new URL("http://localhost:" + port + "/ajouter-resident");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(json.getBytes());
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 201) {
                    System.out.println("Compte créé avec succès !");
                } else {
                    System.out.println("Erreur lors de l'ajout du résident. Code : " + responseCode);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void ajouterPlage() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Jour de la plage (format : yyyy-mm-dd) : ");
        String jour = scanner.nextLine();
        if (!isValidDate(jour)) {
            System.out.println("La date n'est pas valide. Format attendu : yyyy-mm-dd.");
            return;
        }
        System.out.println("Heure de début (hh:mm) : ");
        String heureDebut = scanner.nextLine();
        System.out.println("Heure de fin (hh:mm) : ");
        String heureFin = scanner.nextLine();

        if (!isValidTime(heureDebut) || !isValidTime(heureFin)) {
            System.out.println("L'heure n'est pas valide. Format attendu : hh:mm.");
            return;
        }
        String plage = String.format("{\"jour\":\"%s\", \"heureDebut\":\"%s\", \"heureFin\":\"%s\"}",
                jour, heureDebut, heureFin);

        try {
            URL url = new URL("http://localhost:" + port + "/ajouter-plage");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(plage.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                System.out.println("Plage horaire ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la plage horaire. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getPlages() throws JsonProcessingException {
        Map<String, List<Map<String, Object>>> users = ServerApp.getUsers();
        List<Map<String, Object>> residents = users.get("residents");
        if (residents == null || residents.isEmpty()) {
            System.out.println("Aucun résident trouvé.");
            return;
        }
        for (Map<String, Object> user : residents) {
            String nom = (String) user.get("nom");
            List<Map<String, Object>> plages = (List<Map<String, Object>>) user.get("plages");
            if (plages == null || plages.isEmpty()) {
                System.out.println(nom.toUpperCase() + " n'a pas de plages horaires.");
            } else {
                System.out.println("Plages horaires pour " + nom.toUpperCase() + " :");
                for (Map<String, Object> plage : plages) {
                    String jour = (String) plage.get("jour");
                    String heureDebut = (String) plage.get("heureDebut");
                    String heureFin = (String) plage.get("heureFin");
                    System.out.println("   " + jour + " de " + heureDebut + " à " + heureFin);
                }
            }
        }
    }

    public static void afficherSousMenuPlages() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Préférences horaires ---");
            System.out.println("1. Afficher les plages horaires");
            System.out.println("2. Ajouter une plage horaire");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    getPlages();
                    break;
                case "2":
                    ajouterPlage();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    public static boolean isValidTime(String time) {
        if (time == null || !time.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        String[] parts = time.split(":");
        int h = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return h >= 0 && h < 24 && minute >= 0 && minute < 60;
    }

    public static void voirProjets() {
        List<Projet> projets = obtenirProjets();
        if (projets.isEmpty()) {
            System.out.println("Aucun projet trouvé.");
        } else {
            System.out.println("--- Liste des projets soumis (JSON) dans les 3 prochains mois ---");
            for (int i = 0; i < projets.size(); i++) {
                Projet projet = projets.get(i);
                System.out.println((i + 1) + ". ID : " + projet.getId());
                System.out.println("   Titre : " + projet.getTitre());
                System.out.println("   Description : " + projet.getDescription());
                System.out.println("   Type : " + projet.getType());
                System.out.println("   Date de début : " + projet.getDateDebut());
                System.out.println("   Date de fin : " + projet.getDateFin());
                System.out.println(" Statut: "+ projet.getStatut());
                System.out.println("-------------------------------");
            }
        }
    }
    public static List<Projet> obtenirProjets() {
        String url = "http://localhost:" + port + "/api-projets";
        List<Projet> projets = new ArrayList<>();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String l;
                while ((l = reader.readLine()) != null) {
                    response.append(l);
                }
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Projet>>() {}.getType();
                List<Projet> t = gson.fromJson(response.toString(), listType);
                projets = filterProjetsDansLesTroisMois(t);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la récupération des projets : " + e.getMessage());
        }

        return projets;
    }
}





