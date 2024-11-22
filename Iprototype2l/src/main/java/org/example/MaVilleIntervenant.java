package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class MaVilleIntervenant {
    static void afficherMenuIntervenant() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu intervenant :");
            System.out.println("1. Consulter les requêtes de travail");
            System.out.println("2. Soumettre un nouveau projet");
            System.out.println("3. Mettre à jour les informations d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Veuillez choisir une option : ");
            try {
                int choix = Integer.parseInt(scanner.nextLine());

                if (choix == 1) {
                    voirRequetes();
                }
                if (choix == 2) {
                    System.out.println("Pas implementer");
                } if (choix == 3) {
                    System.out.println("Pas implementer");
                }else if (choix == 4) {
                    System.out.println("Au revoir!");
                    break;
                } else {
                    System.out.println("Option non valide...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }
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

    public static List<RequeteTravail> obtenirRequetes() {
        String url = "http://localhost:7000/api/requetes";
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
}
