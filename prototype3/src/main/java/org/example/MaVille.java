package org.example;

import com.google.gson.*;
import io.javalin.http.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MaVille {
    private static final int port = 8000;

    private static String emailUtilisateurConnecte = null;

    private static final String API_URL = "http://localhost:" + port + "/api/login";

    /**
     * Méthode permettant la connexion d'un utilisateur.
     * @param email Courriel de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @param ctx Contexte initial
     */
    public static void seConnecter(String email, String password, Context ctx) {
        try {
            String reponseJson = fetchApi(API_URL);

            if (reponseJson == null || reponseJson.isEmpty()) {
                reponseErreur(ctx, 500, "Erreur : Aucun utilisateur trouvé.");
                return;
            }

            JsonObject fichierJson = JsonParser.parseString(reponseJson).getAsJsonObject();

            if (validerUtilisateurs(fichierJson.getAsJsonArray("residents"), email, password, ctx, "Résident")) {

                new Resident("x",email,"x","x","x");
                MaVilleResident.afficherMenuResident(email);
                return;
            }

            // Check for intervenants
            if (validerUtilisateurs(fichierJson.getAsJsonArray("intervenants"), email, password, ctx, "Intervenant")) {
                MaVilleIntervenant.afficherMenuIntervenant();
                return;
            }

            // If no match is found
            reponseErreur(ctx, 401, "Identifiants incorrects !");
        } catch (Exception e) {
            reponseErreur(ctx, 500, "Erreur lors de l'accès ou du traitement des données API : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String fetchApi(String apiUrl) throws IOException {
        StringBuilder response = new StringBuilder();

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        if (connection.getResponseCode() == 200) {
            return response.toString();
        } else {
            System.out.println("Erreur HTTP : " + connection.getResponseCode());
            return null;
        }
    }

    /**
     * Méthode permettant de valider un utilisateur et voir s'il est dans la base de données.
     * @param userArray Un JSON array des données de l'utilisateur
     * @param email Courriel de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @param ctx Contexte donné
     * @param userType Type de l'utilisateur
     * @return True si l'utilisateur est trouvé et False sinon.
     */
    public static boolean validerUtilisateurs(JsonArray userArray, String email, String password, Context ctx, String userType) {
        for (JsonElement userElement : userArray) {
            JsonObject userObject = userElement.getAsJsonObject();
            String userEmail = safeGetString(userObject, "courriel");
            String userPassword = safeGetString(userObject, "mdp");
            if (userEmail.equals(email) && userPassword.equals(password)) {
                String successMessage = userType + " connecté : " + safeGetString(userObject, "nom");
                if (ctx != null) {
                    JsonObject response = new JsonObject();
                    response.addProperty("status", "success");
                    response.addProperty("message", successMessage);
                    ctx.status(200).json(response);
                } else {
                    System.out.println(successMessage);
                }
                return true;
            }
        }
        return false;
    }

    private static void reponseErreur(Context ctx, int statusCode, String errorMessage) {
        if (ctx != null) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "error");
            response.addProperty("message", errorMessage);
            ctx.status(statusCode).json(response);
        } else {
            System.out.println(errorMessage);
        }
    }

    /**
     * Méthode permettant d'obtenir un string d'un objet JSON de manière null-safe.
     * @param jsonObject Un objet JSON
     * @param key Une clé de l'objet JSON
     * @return Le string en question.
     */
    public static String safeGetString(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : "";
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Méthode permettant de valider une adresse courriel.
     * @param emailStr Le courriel à valider
     * @return True si le courriel est valide et False sinon.
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

}
