package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe qui gère la connexion HTTP avec l'API.
 */
public class HTTPClient {

    /**
     * Méthode permettant de gérer la connexion entre le serveur et l'API.
     * @param url Le lien de l'API
     * @return Un String de la réponse de l'API.
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        System.out.println("Requête GET vers : " + url);
        HttpURLConnection connexion = (HttpURLConnection) new URL(url).openConnection();
        connexion.setRequestMethod("GET");

        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(connexion.getInputStream()))) {
            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                contenu.append(ligne);
            }
            System.out.println("Réponse reçue : " + contenu.substring(0, Math.min(100, contenu.length())));
            return contenu.toString();
        }
    }

}

