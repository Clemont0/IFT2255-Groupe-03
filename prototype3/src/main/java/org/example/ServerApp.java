package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.javalin.Javalin;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.javalin.http.Context;

/**
 * ServerApp est la classe dédiée au serveur. Elle comporte
 * les méthodes servant à communiquer avec le client et
 * obtenir les données.
 */
public class ServerApp {
    // si vous devez changer le port, utiliser plutôt le path: "src/main/resources/"
    // pour exécuter, car le path actuel est pour le fichier .jar
    // lancer ensuite Main.java
    private static final String path = "src/main/resources/";
    private static final String FILE_PATH = path + "requeteTravaux.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int port = 8000;
                                            // port 8000
    static Javalin app = Javalin.create().start(port);

    /**
     * Méthode permettant l'ouverture du serveur
     */
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
            Map<String, Object> nouvelleRequete = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, Object>> requetes = chargerRequetes();
            requetes.add(nouvelleRequete);
            sauvegarderRequetes(requetes);
            ctx.status(201).result("Requête ajoutée avec succès !");
        });
        app.post("/ajouter-projet", ctx -> {
            Map<String, Object> nouveauProjet = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, Object>> projets = chargerProjets();
            projets.add(nouveauProjet);
            sauvegarderProjets(projets);
            ctx.status(201).result("Projet ajouté avec succès !");
        });
        app.post("/ajouter-candidature", ctx -> {
            Map<String, Object> nouvelleCandidature = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, Object>> candidatures = chargerCandidatures();
            candidatures.add(nouvelleCandidature);
            sauvegarderCandidatures(candidatures);
            ctx.status(201).result("Candidature soumise !");
        });
        app.post("/ajouter-resident", ctx -> {
            Map<String, List<Map<String, Object>>> users = getUsers();
            Map<String, Object> newUser = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, Object>> residents = users.get("residents");
            residents.add(newUser);
            users.put("residents", residents);
            sauvegarderUsers(users);
            ctx.status(201).result("Nouveau résident ajouté avec succès !");
        });
        app.post("/ajouter-intervenant", ctx -> {
            Map<String, List<Map<String, Object>>> users = getUsers();
            Map<String, Object> newUser = mapper.readValue(ctx.body(), new TypeReference<>() {});
            List<Map<String, Object>> intervenants = users.get("intervenants");
            intervenants.add(newUser);
            users.put("intervenants", intervenants);
            sauvegarderUsers(users);
            ctx.status(201).result("Nouvel intervenant ajouté avec succès !");
        });
        app.post("/suppimer-candidature", ctx ->{
            int index = Integer.parseInt(ctx.body());
            List<Map<String, Object>> candidatures = chargerCandidatures();
            candidatures.remove(index);
            sauvegarderCandidatures(candidatures);
            ctx.status(201).result("Candidature supprimée !");
        });
        app.post("/valider-candidature", ctx ->{
            int index = Integer.parseInt(ctx.body())-1;
            List<Map<String, Object>> candidatures = chargerCandidatures();
            candidatures.get(index).replace("etat","En attente d'une réponse.","Approuvée.");
            sauvegarderCandidatures(candidatures);
            ctx.status(201).result("Candidature validée !");
        });
        app.post("/rejetter-candidature", ctx ->{
            int index = Integer.parseInt(ctx.body())-1;
            List<Map<String, Object>> candidatures = chargerCandidatures();
            candidatures.get(index).replace("etat","En attente d'une réponse.","Rejetée.");
            sauvegarderCandidatures(candidatures);
            ctx.status(201).result("Candidature rejetté !");
        });
        app.post("/message-candidature", ctx ->{
            Map<String, Object> element = mapper.readValue(ctx.body(), new TypeReference<>() {});
            int index = (int) element.get("idx")-1;
            String msg = element.get("msg").toString();
            List<Map<String, Object>> candidatures = chargerCandidatures();
            candidatures.get(index).replace("message","",msg);
            sauvegarderCandidatures(candidatures);
            ctx.status(201).result("Candidature rejetté !");
        });
        app.post("/envoyer-notification", ctx -> {
            Map<String, List<Map<String, Object>>> users = getUsers();
            Map<String, Object> notif = mapper.readValue(ctx.body(), new TypeReference<>() {});
            int id = (int) notif.get("projetId");
            Map<String, Object> projet = new HashMap<>();
            List<Map<String, Object>> projets = chargerProjets();
            for (Map<String, Object> p : projets) {
                if (p.get("id").equals(id)) {projet = p;}
            }
            List<Map<String, Object>> residents = users.get("residents");
            String qs = projet.get("quartiers").toString();
            List<String> quartiers = Arrays.asList(qs.substring(1, qs.length() - 1).split("\\s*,\\s*"));
            for (Map<String, Object> r : residents) {
                String rq = r.get("quartier").toString();
                if (quartiers.contains(rq)) {
                    Object q = r.get("notifications");
                    List<Map<String, Object>> not = mapper.readValue(parseStringAsJSON(q.toString()), new TypeReference<>() {});
                    not.add(notif);
                    r.put("notifications", not);
                }
            }
            users.put("residents", residents);
            sauvegarderUsers(users);
            ctx.status(201).result("Notification envoyée avec succès !");
        });
        app.post("/ajouter-plage", ctx -> {
            Map<String, String> plage = mapper.readValue(ctx.body(), Map.class);
            Map<String, List<Map<String, Object>>> users = getUsers();
            String email = Resident.getCourriel();
            if (email == null || email.isEmpty()) {
                ctx.status(404).result("Aucun résident connecté.");
                return;
            }
            Map<String, Object> resident = null;
            for (Map<String, Object> r : users.get("residents")) {
                if (r.get("courriel").equals(email)) {
                    resident = r;
                    break;
                }
            }
            if (resident == null) {
                ctx.status(404).result("Résident non trouvé.");
                return;
            }
            List<Map<String, String>> plages = (List<Map<String, String>>) resident.get("plages");
            if (plages == null) {
                plages = new ArrayList<>();
                resident.put("plages", plages);
            }
            plages.add(plage);
            sauvegarderUsers(users);
            ctx.status(201).result("Plage horaire ajoutée avec succès!");
        });

        app.get("/api-projets", ctx -> {
            List<Projet> projets = lireProjets();
            if (projets.isEmpty()) {
                ctx.status(404).json("Aucun fichier trouvé");
            } else {
                List<Projet> projetsFiltrés = filterProjetsDansLesTroisMois(projets);
                ctx.status(200).json(projetsFiltrés);
            }
        });
        app.get("/travaux", ServerApp::getTravaux);
        app.get("/entraves", ServerApp::getEntraves);
    }

    /**
     * Méthode permettant l'envoie d'une notification à partir
     * du serveur.
     * @param json Le string JSON de la notification
     */
    public static void envoyerNotification(String json) {
        try {
            URL url = new URL("http://localhost:" + port + "/envoyer-notification");
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
                System.out.println("Notification envoyée avec succès !");
            } else {
                System.out.println("Erreur lors de l'envoi de la notification. Code : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Méthode permettant d'obtenir les utilistaurs de l'application.
     * @return Une Map des utilisateurs où les clés "residents" et "intervenants"
     * servent à obtenir les utilisateurs correspondants.
     */
    public static Map<String, List<Map<String, Object>>> getUsers() {
        Map<String, List<Map<String, Object>>> fichierJson = new HashMap<>();
        List<Map<String, Object>> residents = new ArrayList<>();
        for (JsonObject jo : lireUtilisateurs("residents")) {residents.add(jsonObjectToMap(jo));}

        List<Map<String, Object>> intervenants = new ArrayList<>();
        for (JsonObject jo : lireUtilisateurs("intervenants")) {intervenants.add(jsonObjectToMap(jo));}

        fichierJson.put("residents", residents);
        fichierJson.put("intervenants", intervenants);
        return fichierJson;
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
                    record.path("organizationname").asText(),
                    record.path("duration_start_date").asText()
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

    private static Map<String, Object> jsonObjectToMap(JsonObject jsonObject) {
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

    private static List<Object> jsonArrayToList(JsonArray jsonArray) {
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

    private static List<Map<String, Object>> chargerRequetes() throws IOException {
        return mapper.readValue(new File(FILE_PATH), new TypeReference<>() {});
    }

    /**
     * Méthode permettant de charger les projets actifs dans l'application.
     * @return Une List des projets sous forme de Map.
     * @throws IOException
     */
    public static List<Map<String, Object>> chargerProjets() throws IOException {
        return mapper.readValue(new File(path + "projets.json"), new TypeReference<>() {});
    }

    /**
     * Méthode permettant de charger les candidatures actives dans l'application.
     * @return Une List des candidatures sous forme de Map.
     * @throws IOException
     */
    public static List<Map<String, Object>> chargerCandidatures() throws IOException {
        return mapper.readValue(new File(path + "candidatures.json"), new TypeReference<>() {});
    }

    private static void sauvegarderRequetes(List<Map<String, Object>> requetes) throws IOException {
        mapper.writeValue(new File(FILE_PATH), requetes);
    }

    /**
     * Méthode permettant de sauvegarder les projets dans la base de données.
     * @param requetes La liste des requêtes de travail
     * @throws IOException
     */
    public static void sauvegarderProjets(List<Map<String, Object>> requetes) throws IOException {
        mapper.writeValue(new File(path + "projets.json"), requetes);
    }

    /**
     * Méthode permettant de sauvegarder les utilisateurs dans la base de données.
     * @param users La Map des utilisateurs
     * @throws IOException
     */
    public static void sauvegarderUsers(Map<String, List<Map<String, Object>>> users) throws IOException {
        mapper.writeValue(new File(path + "utilisateurs.json"), users);
    }

    /**
     * Méthode permettant de sauvegarder les candidatures dans la base de données.
     * @param candidature La liste des candidatures
     * @throws IOException
     */
    public static void sauvegarderCandidatures(List<Map<String,Object>> candidature) throws IOException {
        mapper.writeValue(new File(path + "candidatures.json"), candidature);
    }


    /**
     * Méthode permettant de formatter en une List la réponse JSON de l'API.
     * @param reponseJson Le string JSON de la réponse
     * @return Une List des entraves obtenues par l'API.
     * @throws Exception
     */
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

    /**
     * Méthode permettant de vérifier si une date a le bon format.
     * @param date La date à valider
     * @return True si la date est valide et False sinon
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Méthode permettant de vérifier si la date1 vient avant la date2. Autrement dit, vérifie
     * si la date de fin est après la date de début.
     * @param date1 Date de début
     * @param date2 Date de fin
     * @return True si date2 vient après date1, False sinon
     * @throws ParseException
     */
    public static boolean isLaterDate(String date1, String date2) throws ParseException {
        if (!isValidDate(date1) || !isValidDate(date2)) {
            System.out.println("Les dates sont erronées");
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        boolean flag = d1.compareTo(d2) < 0;
        if (!flag) {
            System.out.println("La date de fin arrive avant la date de début");
        }
        return flag;
    }

    /**
     * Méthode permettant de transformer un string en un JSON string pour
     * ajouter une notification.
     * @param raw Le string de données initiales
     * @return JSON string
     */
    public static String parseStringAsJSON(String raw) {
        String[] objects = raw.substring(1, raw.length() - 1).trim().split("\\s*}\\s*,\\s*\\{\\s*");
        String json = "";
        int n = objects.length;
        for (int i = 0; i < n; i++) {
            String to = objects[i];
            String o;
            // string is an empty array
            if (raw.length() == 2) {
                return raw;
            } else if (n == 1) {
                o = to.substring(1, to.length() - 1);
            } else if (i == 0) {
                o = to.substring(1);
            } else if (i == n - 1) {
                o = to.substring(0, to.length() - 1);
            } else {
                o = to;
            }
            String obj = "{";
            String[] items = o.trim().split("\\s*,\\s*");
            for (String it : items) {
                String[] elems = it.trim().split("\\s*=\\s*");
                String projet = elems[0].equals("projetId") ? elems[1] : "\"" + elems[1] + "\"";
                obj = obj + "\"" + elems[0] + "\":" + projet + ",";
            }
            json = json + (obj.substring(0, obj.length() - 1) + "},");
        }
        return "[" + json.substring(0, json.length() - 1) + "]";
    }

    /**
     * Méthode permettant d'arrêter le serveur.
     */
    public static void stopServer() {
        if (app != null) {
            app.stop();
            System.out.println("Serveur arrêté proprement.");
        }
    }

    private static List<Projet> lireProjets() {
        List<Projet> projets = new ArrayList<>();
        try (Reader reader = new FileReader(path + "projets.json")) {
            Gson gson = new Gson();
            Projet[] projetsArray = gson.fromJson(reader, Projet[].class);
            if (projetsArray != null) {
                projets.addAll(Arrays.asList(projetsArray));
                System.out.println(projets);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
        return projets;
    }

    /**
     * Méthode permettant d'obtenir les projets actifs sur les 3 prochains mois.
     * @param projets La liste des projets à filtrer
     * @return Une List des projets des 3 prochains mois.
     */
    public static List<Projet> filterProjetsDansLesTroisMois(List<Projet> projets) {
        List<Projet> projetsFiltrés = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate troisMoisPlusTard = today.plusMonths(3);
        for (Projet projet : projets) {
            String dateFinStr = projet.getDateFin();
            LocalDate dateFin = LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (!dateFin.isBefore(today) && !dateFin.isAfter(troisMoisPlusTard)) {
                projetsFiltrés.add(projet);
            }
        }

        return projetsFiltrés;
    }
}



