
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Intervenant;
import org.example.RequeteTravail;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaVilleIntervenantTest {

    @Test
    void testVoirRequetesSansData() {
        List<RequeteTravail> simulatedRequetes = new ArrayList<>();
        if (simulatedRequetes.isEmpty()) {
            assertEquals(0, simulatedRequetes.size(), "LA liste doit etre vide");
        } else {
            fail("PAs de data attendu");
        }
    }

    @Test
    void testVoirRequetesAvecData() {
        List<RequeteTravail> requeteSimuler = new ArrayList<>();
        requeteSimuler.add(new RequeteTravail("1", "Fixer la route", "Trou", "Maintenance", "2024-11-01", "2024-11-30"));
        requeteSimuler.add(new RequeteTravail("2", "Deinniegage", "Troitoire", "Neige", "2024-12-01", "2024-12-15"));
        assertEquals(2, requeteSimuler.size(), "LA liste doit contenir 2 objet");
        assertEquals("Fixer la route", requeteSimuler.get(0).getTitre(), "attendu dans la liste 'Fix road'.");
    }

    @Test
    void testObtenirRequetesReponseVailde() {
        String reponse = "[\n" +
                "  {\n" +
                "    \"id\": \"1\",\n" +
                "    \"titre\": \"Fixer la route\",\n" +
                "    \"description\": \"Trou\",\n" +
                "    \"type\": \"Maintenance\",\n" +
                "    \"dateDebut\": \"2024-11-01\",\n" +
                "    \"dateFin\": \"2024-11-30\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"2\",\n" +
                "    \"titre\": \"Nettoyage\",\n" +
                "    \"description\": \"Trotoirre\",\n" +
                "    \"type\": \"Maintenance\",\n" +
                "    \"dateDebut\": \"2024-12-01\",\n" +
                "    \"dateFin\": \"2024-12-15\"\n" +
                "  }\n" +
                "]";

        Gson gson = new Gson();
        Type listType = new TypeToken<List<RequeteTravail>>() {}.getType();
        List<RequeteTravail> requetes = gson.fromJson(reponse, listType);
        assertEquals(2, requetes.size(), "LA liste devrai avoir 2 entrée");
        assertEquals("Fixer la route", requetes.get(0).getTitre(), "attendu 'Fix road'.");
        assertEquals("Nettoyage", requetes.get(1).getTitre(), "attendu 'Snow removal'.");
    }

    @Test
    void testObtenirRequetesSansReponses() {
        String reponse = "[]";
        Gson gson = new Gson();
        Type listType = new TypeToken<List<RequeteTravail>>() {}.getType();
        List<RequeteTravail> requetes = gson.fromJson(reponse, listType);
        assertTrue(requetes.isEmpty(), "La liste doit etre vide");
    }

    @Test
    void testVoirIntervenantsVide() {
        List<Intervenant> response = new ArrayList<>();
        if (response.isEmpty()) {
            assertEquals(0, response.size(), "La liste doit etre vide");
        } else {
            fail("Taille nulle attendue");
        }
    }

    @Test
    void testVoirIntervenantNonVide() {
        List<Intervenant> response = new ArrayList<>();
        response.add(new Intervenant("Jean Dupont", "JeanDupont@intervenant.com","intmotDePasse1","Particulier","12345678"));
        response.add(new Intervenant("Marie Curie", "MarieCurie@intervenant.com","intmotDePasse2","Entrepreneur privé","87654321"));
        assertEquals(2, response.size(), "La liste doit contenir 2 objet");
        assertEquals("Marie Curie", response.get(1).getNom(), "attendu dans la liste 'Marie Curie'.");
    }


}
