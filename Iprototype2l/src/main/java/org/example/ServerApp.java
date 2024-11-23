package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.javalin.Javalin;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.http.Context;

public class ServerApp {
    // si vous devez changer le port, utiliser plutôt le path: "src/main/resources/"
    // pour exécuter, car le path actuel est pour le fichier .jar
    // lancer ensuite Main.java
    private static final String path = "database/";
    private static final String FILE_PATH = path + "requeteTravaux.json";
    private static final ObjectMapper mapper = new ObjectMapper();
                                            // port 8000
    static Javalin app = Javalin.create().start(8000);
    public static void startServer() {

        app.get("/api/login", ctx -> {
            try {
                JsonObject fichierJson = new JsonObject();
                fichierJson.add("residents", new Gson().toJsonTree(lireUtilisateurs("residents")));
                fichierJson.add("intervenants", new Gson().toJsonTree(lireUtilisateurs("intervenants")));
                Map<String, Object> responseMap = jsonObjectToMap(fichierJson);
                ctx.status(200).json(responseMap);
            } catch (Exception e) {
                ctx.status(500).json("Erreur lors de la lecture des utilisateurs.");
            }
        });
        app.get("/api/requetes", ctx -> {
            List<RequeteTravail> requetes = lireRequetes();
            if (requetes.isEmpty()) {
                ctx.status(404).json("Aucun fichier trouvé");
            } else {
                ctx.status(200).json(requetes);
            }
        });
        app.post("/ajouter-requete", ctx -> {
            Map<String, String> nouvelleRequete = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, String>> requetes = chargerRequetes();
            requetes.add(nouvelleRequete);
            sauvegarderRequetes(requetes);
            ctx.status(201).result("Requête ajoutée avec succès !");
        });
        app.get("/travaux", ServerApp::getTravaux);
        app.get("/entraves", ServerApp::getEntraves);

    }

    private static void getEntraves(Context ctx) {
        try {
            String apiUrl = "https://donnees.montreal.ca/api/3/action/datastore_search?resource_id=a2bc8014-488c-495d-941b-e7ae1999d1bd";
            String jsonResponse = fetchAPIData(apiUrl);
            List<Entraves> entravesList = reponseAPIExterne1(jsonResponse);
            ctx.json(entravesList);
        } catch (Exception e) {
            ctx.status(500).result("Erreur lors de la récupération ou du traitement des données.");

        }
    }

    private static void getTravaux(Context ctx) {
        try {
            String apiUrl = "https://donnees.montreal.ca/api/3/action/datastore_search?resource_id=cc41b532-f12d-40fb-9f55-eb58c9a2b12b";
            String reponseJson = fetchAPIData(apiUrl);
            List<Travail> travaux = reponseAPIExterne(reponseJson);
            ctx.json(travaux);
        } catch (Exception e) {
            ctx.status(500).result("Erreur lors de la récupération ou du traitement des données.");
        }
    }
    private static String fetchAPIData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Erreur lors de la récupération des données : Code " + responseCode);
        }
        StringBuilder reponseJson = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                reponseJson.append(scanner.nextLine());
            }
        }
        return reponseJson.toString();
    }
    private static List<Travail> reponseAPIExterne(String reponseJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode noeudRacine = mapper.readTree(reponseJson);

        JsonNode Nouedrecords = noeudRacine.path("result").path("records");
        if (Nouedrecords.isMissingNode() || !Nouedrecords.isArray()) {
            throw new Exception("Le champ 'records' est absent ou invalide.");
        }
        List<Travail> travaux = new ArrayList<>();
        for (JsonNode record : Nouedrecords) {
            Travail travail = new Travail(
                    record.path("_id").asText(),
                    record.path("boroughid").asText(),
                    record.path("currentstatus").asText(),
                    record.path("reason_category").asText(),
                    record.path("submittercategory").asText(),
                    record.path("organizationname").asText()
            );
            travaux.add(travail);
        }

        return travaux;
    }


    private static List<RequeteTravail> lireRequetes() {
        List<RequeteTravail> requetes = new ArrayList<>();
        try (Reader reader = new FileReader(FILE_PATH)) {
            Gson gson = new Gson();
            RequeteTravail[] requetesArray = gson.fromJson(reader, RequeteTravail[].class);
            if (requetesArray != null) {
                requetes.addAll(Arrays.asList(requetesArray));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
        return requetes;
    }



    private static List<JsonObject> lireUtilisateurs(String role) {
        String filePath = path + "utilisateurs.json";
        List<JsonObject> utilisateurs = new ArrayList<>();
        try (Reader reader = new FileReader(filePath)) {
            JsonObject fichierJson = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray roleArray = fichierJson.getAsJsonArray(role);
            for (JsonElement userElement : roleArray) {
                utilisateurs.add(userElement.getAsJsonObject());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
        return utilisateurs;
    }

    public static Map<String, Object> jsonObjectToMap(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                map.put(entry.getKey(), jsonObjectToMap(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                map.put(entry.getKey(), jsonArrayToList(value.getAsJsonArray()));
            } else {
                map.put(entry.getKey(), value.getAsString());
            }
        }
        return map;
    }

    public static List<Object> jsonArrayToList(JsonArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                list.add(jsonObjectToMap(element.getAsJsonObject()));
            } else if (element.isJsonArray()) {
                list.add(jsonArrayToList(element.getAsJsonArray()));
            } else {
                list.add(element.getAsString());
            }
        }
        return list;
    }

    private static List<Map<String, String>> chargerRequetes() throws IOException {
        return mapper.readValue(new File(FILE_PATH), new TypeReference<>() {});
    }

    private static void sauvegarderRequetes(List<Map<String, String>> requetes) throws IOException {
        mapper.writeValue(new File(FILE_PATH), requetes);
    }


    public static List<Entraves> reponseAPIExterne1(String reponseJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode noeudRacine = mapper.readTree(reponseJson);
        JsonNode noeudResultat = noeudRacine.path("result");
        if (noeudResultat.isMissingNode() || !noeudResultat.isObject()) {
            throw new Exception("Le champ 'result' est absent ou invalide.");
        }

        JsonNode Nouedrecords = noeudResultat.path("records");
        if (Nouedrecords.isMissingNode() || !Nouedrecords.isArray()) {
            throw new Exception("Le champ 'records' est absent ou invalide.");
        }

        List<Entraves> entraves = new ArrayList<>();
        for (JsonNode record : Nouedrecords) {
            Entraves entrave = new Entraves(
                    record.path("id_request").asText(),
                    record.path("streetid").asText(),
                    record.path("shortname").asText(),
                    record.path("streetimpacttype").asText()
            );
            entraves.add(entrave);
        }

        return entraves;
    }

    public static void stopServer() {
        if (app != null) {
            app.stop();
            System.out.println("Serveur arrêté proprement.");
        }
    }
}
