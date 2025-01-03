package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import static org.example.ServerApp.getUsers;
import static org.example.ServerApp.isLaterDate;

/**
 * Classe contenant toutes les interactions et actions possibles par un intervenant.
 */
public class MaVilleIntervenant {
    private static final int port = 8000;

    /**
     * Méthode permettant d'afficher le menu d'un intervenant.
     */
    public static void afficherMenuIntervenant() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu intervenant :");
            System.out.println("1. Consulter les requêtes de travail");
            System.out.println("2. Soumettre une candidature.");
            System.out.println("3. Retirer une candidature.");
            System.out.println("4. Voir les candidatures soumises.");
            System.out.println("5. Soumettre un nouveau projet");
            System.out.println("6. Mettre à jour les informations d'un projet");
            System.out.println("7. Quitter");
            System.out.print("Veuillez choisir une option : ");
            try {
                int choix = Integer.parseInt(scanner.nextLine());

                if (choix == 1) {
                    voirRequetes();
                }if (choix == 2){
                    soumettreCandidature();
                }if (choix == 3){
                    retirerCandidature();
                }if(choix == 4){
                    afficherCandidatures();
                }if (choix == 5) {
                    soumettreProjet();
                } if (choix == 6) {
                    modifierProjet();
                }else if (choix == 7) {
                    System.out.println("Au revoir!");
                    break;
                } else {
                    System.out.println("Option non valide...");
                }
            } catch (NumberFormatException | IOException | ParseException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    /**
     * Méthode permettant d'afficher les requêtes de travail actives.
     */
    public static void voirRequetes() {
        List<RequeteTravail> requetes = obtenirRequetes();
        if (requetes.isEmpty()) {
            System.out.println("Aucune requête trouvée.");
        } else {
            System.out.println("\n--- Liste des requêtes ---");
            for (int i = 0; i < requetes.size(); i++) {
                RequeteTravail requete = requetes.get(i);
                System.out.println((i + 1) + ". ID : " + requete.getId());
                System.out.println("   Titre : " + requete.getTitre());
                System.out.println("   Description : " + requete.getDescription());
                System.out.println("   Type : " + requete.getType());
                System.out.println("   Date de début : " + requete.getDateDebut());
                System.out.println("   Date de fin : " + requete.getDateFin());
                System.out.println();
            }
        }
    }

    private static List<RequeteTravail> obtenirRequetes() {
        String url = "http://localhost:" + port + "/api/requetes";
        List<RequeteTravail> requetes = new ArrayList<>();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RequeteTravail>>() {}.getType();
                requetes = gson.fromJson(response.toString(), listType);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la récupération des requêtes : " + e.getMessage());
        }

        return requetes;
    }

    /**
     * Méthode permettant de soumettre une candidature sur une requête de travail.
     * @throws IOException
     * @throws ParseException
     */
    public static void soumettreCandidature() throws IOException, ParseException {
        boolean found1 = false,found2  = false;
        Map<String, List<Map<String, Object>>> users = getUsers();
        String nom = "",idIntervenant,idRequete,etat = "En attente d'une réponse.",message="";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuiller saisir votre ID personnel:");
        idIntervenant = scanner.nextLine();
        System.out.println(idIntervenant);

        for(Map <String,Object> user : users.get("intervenants")){
            String val1 = user.get("id").toString();
            if(val1.equalsIgnoreCase(idIntervenant)){
                nom = user.get("nom").toString();
                found1 = true;
                break;
            }
        }if(!found1){
            System.out.println("Id introuvable. Veuiller verifier si l'id entré est valide.");
            return;
        }

        voirRequetes();

        System.out.println("Veuiller saisir l'id de la requête pour laquelle vous voulez postuler parmi les requêtes montrées précédemment.");
        idRequete = scanner.nextLine();

        for(RequeteTravail requeteTravail : obtenirRequetes()){
            if(requeteTravail.getId().equalsIgnoreCase(idRequete)){
                found2 = true ;
                System.out.println("requête trouvée.");
                break;
            }
        }if(!found2){
            System.out.println("Requête introuvable. Veuiller verifier si l'id entré est valide.");
            return;
        }

        System.out.print("Entrez la date de début (yyyy-mm-dd) : ");
        String debut = scanner.nextLine();

        System.out.print("Entrez la date de fin (yyyy-mm-dd) : ");
        String fin = scanner.nextLine();

        if (!isLaterDate(debut, fin)) {return;}

        int idx = Id.nextCandidateId();

        String jsonCandidature = String.format(
                "{\"Id_Candidature\":%d, \"Nom de l'intervenant\":\"%s\", \"Date de Soumission\":\"%s\",\"Date de Debut\":\"%s\", \"Date de Fin\":\"%s\", \"Id_Requete\":\"%s\", \"Id_Intervenant\":\"%s\", \"etat\":\"%s\", \"message\":\"%s\"}",
                idx,nom, LocalDate.now(),debut,fin,idRequete,idIntervenant,etat,message
        );

        try {
            URL url = new URL("http://localhost:" + port + "/ajouter-candidature");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonCandidature.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                System.out.println("Candidature ajouté avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout de la candidature. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode permettant de retirer une candidature d'une requête de travail.
     * @throws IOException
     */
    public static void retirerCandidature() throws IOException {
        afficherCandidatures();
        boolean found = false;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuiller saisir le numero correspondant à la candidature à supprimer : ");
        String id  = scanner.nextLine();

        for(Map<String,Object> candidature : ServerApp.chargerCandidatures()){
            if (Integer.parseInt(id)-1 == ServerApp.chargerCandidatures().indexOf(candidature)){
                String position = String.valueOf(Integer.parseInt(id)-1);
                found = true;
                try {
                    URL url = new URL("http://localhost:" + port + "/suppimer-candidature");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(position.getBytes());
                        os.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 201) {
                        System.out.println("Candidature supprimée");
                        break;
                    }else {
                        System.out.println("Erreur lors de la suppression de la candidature. Code : " + responseCode);
                        System.out.println(id);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(!found){
            System.out.println("Il n'existe aucune candidature associée à cet ID.");
        }
    }

    /**
     * Méthode permettant d'afficher les candidatures actives.
     * @throws IOException
     */
    public static void afficherCandidatures() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuiller entrer votre ID personnels");
        String id = scanner.nextLine();
        int count = 1;

        if(ServerApp.chargerCandidatures().isEmpty()) {
            System.out.println("Aucune candidature n'a été soumise dans le système.");
        }

        System.out.println("\n--- Liste des candidatures ---");
        for(Map<String, Object> candidature : ServerApp.chargerCandidatures()) {
            if(candidature.get("Id_Intervenant").equals(id)){
                System.out.println(count +".");
                for (String key : candidature.keySet()) {
                    System.out.println(key + ": " + candidature.get(key).toString());
                }
                count++;
            }
        }
    }

    /**
     * Méthode permettant de soumettre un nouveau projet.
     * @throws IOException
     * @throws ParseException
     */
    public static void soumettreProjet() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez le titre du projet :");
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

        System.out.println(
                "1. Ahuntsic-Cartierville\n" +
                        "2. Anjou\n" +
                        "3. Côte-des-Neiges–Notre-Dame-de-Grâce\n" +
                        "4. Lachine\n" +
                        "5. Lasalle\n" +
                        "6. Le Plateau-Mont-Royal\n" +
                        "7. Le Sud-Ouest\n" +
                        "8. L’Île-Bizard–Sainte-Geneviève\n" +
                        "9. Mercier–Hochelaga-Maisonneuve\n" +
                        "10. Montréal-Nord\n" +
                        "11. Outremont\n" +
                        "12. Pierrefonds-Roxboro\n" +
                        "13. Rivière-des-Prairies–Pointe-aux-Trembles\n" +
                        "14. Rosemont–La Petite-Patrie\n" +
                        "15. Saint-Laurent\n" +
                        "16. Saint-Léonard\n" +
                        "17. Verdun\n" +
                        "18. Ville-Marie\n" +
                        "19. Villeray–Saint-Michel–Parc-Extension\n"
        );
        System.out.print("Sélectionnez les quartiers affectés (1 2 ...) : ");
        String[] quartiers = scanner.nextLine().split(" ");
        String quartiersJson = "";
        ArrayList<String> qs = new Quartiers().getQuartiers();
        for (String q : quartiers) {
            int i = Integer.parseInt(q) - 1;
            quartiersJson = quartiersJson + "\"" + qs.get(i) + "\",";
        }
        quartiersJson = "[" + quartiersJson.substring(0, quartiersJson.length() - 1) + "]";

        System.out.print("Entrez la date de début (yyyy-mm-dd) : ");
        String dateDebut = scanner.nextLine();


        System.out.print("Entrez la date de fin (yyyy-mm-dd) : ");
        String dateFin = scanner.nextLine();

        if (!isLaterDate(dateDebut, dateFin)) {return;}

        System.out.print("Horaire des travaux : ");
        String horaire = scanner.nextLine();

        int id = Id.nextProjetId();
        String jsonRequete = String.format(
                "{\"id\":%d, \"titre\":\"%s\", \"description\":\"%s\", \"type\":\"%s\",\"quartiers\":" + quartiersJson +
                        ", \"dateDebut\":\"%s\", \"dateFin\":\"%s\", \"horaire\":\"%s\", \"statut\":\"Prévu\"}",
                id, titre, description, type, dateDebut, dateFin, horaire
        );

        try {
            URL url = new URL("http://localhost:" + port + "/ajouter-projet");
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
                System.out.println("Projet ajouté avec succès !");
                String msg = "Nouveau projet: " + titre + "!";
                String notif = "{\"projetId\": " + id + ", \"message\":\"" + msg + "\"}";
                ServerApp.envoyerNotification(notif);
            } else {
                System.out.println("Erreur lors de l'ajout de du projet. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode permettant d'afficher les projets.
     * @return Une List des projets sous forme de Map.
     * @throws IOException
     */
    public static List<Map<String, Object>> afficherProjets() throws IOException {
        List<Map<String, Object>> projets = ServerApp.chargerProjets();
        System.out.println("\n--- Liste des projets ---");
        for (Map<String, Object> projet : projets) {
            for (String key : projet.keySet()) {
                System.out.println(key + ": " + projet.get(key).toString());
            }
            System.out.println();
        }
        return projets;
    }

    /**
     * Méthode permettant de modifier un certain projet.
     * @throws IOException
     * @throws ParseException
     */
    public static void modifierProjet() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            List<Map<String, Object>> projets = afficherProjets();
            Map<String, Object> travail = null;
            System.out.println();
            System.out.println("1. Entrez l'identifiant (id) du travail à modifier : ");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix : ");
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 0) {break;}

            for (Map<String, Object> projet : projets) {
                if (projet.get("id").equals(input)) {
                    travail = projet;
                    projets.remove(projet);
                    break;
                }
            }
            if (travail == null) {
                System.out.println("Travail introuvable. Veuillez réessayer.");
                continue;
            }

            System.out.println(
                    "1. Modifier la description\n" +
                            "2. Modifier la date de fin\n" +
                            "3. Modifier le statut"
            );
            String typeModif;
            System.out.print("Entrez votre choix : ");
            int choix = Integer.parseInt(scanner.nextLine());
            if (choix < 1 || choix > 3) {
                System.out.println("Choix invalide");
                continue;
            }
            if (choix == 1) {
                System.out.println("Entrez la nouvelle description : ");
                String desc = scanner.nextLine();
                travail.put("description", desc);
                typeModif = "description";
            } else if (choix == 2) {
                System.out.print("Entrez la nouvelle date de fin : ");
                String date = scanner.nextLine();
                if (!isLaterDate(travail.get("dateDebut").toString(), date)) {
                    continue;
                }
                travail.put("dateFin", date);
                typeModif = "date de fin";
            } else {
                System.out.println(
                        "1. Prévu\n" +
                                "2. En cours\n" +
                                "3. Suspendu\n" +
                                "4. Terminé\n"
                );
                System.out.print("Choisissez le nouveau statut : ");
                int opt = Integer.parseInt(scanner.nextLine());
                if (opt < 1 || opt > 4) {
                    System.out.println("Choix invalide");
                    continue;
                }
                travail.put("statut", new StatutTravaux().getStatuts().get(opt - 1));
                typeModif = "statut";
            }
            projets.add(travail);
            System.out.println("Projet modifié avec succès !");
            String msg = "Le projet " + travail.get("titre") + " a été modifié! Le changement effectué est: " + typeModif;
            String notif = "{\"projetId\": " + travail.get("id") + ", \"message\":\"" + msg + "\"}";
            ServerApp.envoyerNotification(notif);
            ServerApp.sauvegarderProjets(projets);
        }
    }

    /**
     * Méthode permettant de procéder à l'inscription d'un intervenant.
     * @throws IOException
     */
    public static void inscription() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Nom : ");
            String name = scanner.nextLine();

            System.out.println("Courriel : ");
            String courriel = scanner.nextLine();
            boolean isValidEmail = MaVille.validate(courriel);
            if (!isValidEmail) return;

            System.out.println("Mot de passe : ");
            String mdp = scanner.nextLine();

            System.out.println(
                    "1. Entreprise publique\n" +
                    "2. Entrepreneur privé\n" +
                    "3. Particulier"
            );
            System.out.print("Type : ");
            int typeChoix = scanner.nextInt();
            if (typeChoix < 1 || typeChoix > 3) {
                System.out.println("Choix invalide");
                return;
            }
            String choix;
            if (typeChoix == 1) {
                choix = "Entreprise publique";
            } else if (typeChoix == 2) {
                choix = "Entrepreneur privé";
            } else {
                choix = "Particulier";
            }

            System.out.print("Identifiant de la ville : ");
            String idVille = scanner.nextLine();


            String json = String.format("{\"nom\":\"%s\", \"courriel\":\"%s\"," +
                            " \"mdp\":\"%s\", \"type\":\"%s\", \"id\":\"%s\"}",
                    name, courriel, mdp, choix, idVille
            );
            try {
                URL url = new URL("http://localhost:" + port + "/ajouter-intervenant");
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
                    System.out.println("Erreur lors de l'ajout de l'intervenant. Code : " + responseCode);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
