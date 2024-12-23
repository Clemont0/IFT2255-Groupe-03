

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.MaVille;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe contenant certains tests pour des méthodes de MaVille.java
 */
public class MaVilleTest {

    @Test
    void testFetch() {
        String simulatedApiResponse = """
                {
                  "residents": [
                    { "courriel": "resident@example.com", "mdp": "password123", "nom": "Resident One" }
                  ],
                  "intervenants": [
                    { "courriel": "intervenant@example.com", "mdp": "password456", "nom": "Intervenant One" }
                  ]
                }
                """;

        JsonObject jsonObject = JsonParser.parseString(simulatedApiResponse).getAsJsonObject();
        assertNotNull(jsonObject, "TObjet ne doit pas etre nul");
        assertTrue(jsonObject.has("residents"), "attendu 'residents'.");
        assertTrue(jsonObject.has("intervenants"), "attendu 'intervenants'.");
    }

    @Test
    void testValiderUtilisateurs() {
        String userArray = """
                [
                  { "courriel": "resident@example.com", "mdp": "password123", "nom": "Resident" },
                  { "courriel": "anotheruser@example.com", "mdp": "password456", "nom": "Bomboclat" }
                ]
                """;

        var jsonArray = JsonParser.parseString(userArray).getAsJsonArray();
        boolean isValid = MaVille.validerUtilisateurs(jsonArray, "resident@example.com", "password123", null, "Résident");
        assertTrue(isValid, "Les identifiants devraient être valides pour l'utilisateur résident.");
    }

    @Test
    void testSafeGetString() {
        String jsonObject = """
                {
                  "key1": "value1",
                  "key2": null
                }
                """;

        JsonObject parsedObject = JsonParser.parseString(jsonObject).getAsJsonObject();
        String value1 = MaVille.safeGetString(parsedObject, "key1");
        String value2 = MaVille.safeGetString(parsedObject, "key2");
        String value3 = MaVille.safeGetString(parsedObject, "noKey");

        assertEquals("value1", value1, "La valeur pour 'key1' devrait être 'value1'.");
        assertEquals("", value2, "La valeur pour 'key2' devrait être une chaîne vide.");
        assertEquals("", value3, "La valeur pour une clé manquante devrait être une chaîne vide.");
    }




}
