
import org.example.Entraves;
import org.example.MaVilleResident;
import org.example.Projet;
import org.example.ServerApp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ServerAppTest {

    @Test
    void testreponseApi1() {
        String jsonResponse = "{\n" +
                "\"result\": {\n" +
                "\"records\": [\n" +
                "  {\"id_request\": \"1\", \"streetid\": \"101\", \"shortname\": \"Street 1\", \"streetimpacttype\": \"type1\"},\n" +
                "  {\"id_request\": \"2\", \"streetid\": \"102\", \"shortname\": \"Street 2\", \"streetimpacttype\": \"type2\"}\n" +
                "]\n" +
                "}\n" +
                "}";

        try {
            List<Entraves> entraves = ServerApp.reponseAPIExterne1(jsonResponse);

            assertEquals(2, entraves.size(), "2 entraves attendu");
            assertEquals("1", entraves.get(0).getIdRequest(), "attendu '1'.");
            assertEquals("101", entraves.get(0).getStreetId(), "attendu '101'.");
            assertEquals("Street 1", entraves.get(0).getShortName(), "attendu 'Street 1'.");
            assertEquals("type1", entraves.get(0).getStreetImpactType(), "attendu 'type1'.");
        } catch (Exception e) {
            fail("Exeption " + e.getMessage());
        }
    }

    @Test
    void testIsValidDate() {
        assertTrue(ServerApp.isValidDate("2024-11-22"));  // Date correcte
        assertTrue(ServerApp.isValidDate("2000-01-01"));  // Date au format valide
    }

    @Test
    void testIsLaterDate() throws ParseException {
        String date1 = "2024-12-22";
        String date2 = "2025-01-01";
        String date3 = "2024-12-20";
        String date4 = "25-02-20";
        assertTrue(ServerApp.isLaterDate(date1, date2));
        assertFalse(ServerApp.isLaterDate(date1, date3));
        assertFalse(ServerApp.isLaterDate(date2, date4));
    }

    @Test
    void testFiltrerProjetsTroisMois() {
        List<String> quartiers = new ArrayList<>();
        quartiers.add("Anjou");
        quartiers.add("Lasalle");
        Projet projet1 = new Projet(10,"Rénovation","Rénovation salle de bain","Rénovation",quartiers, "2025-01-10","2025-02-03","vendredi", "Prévu");
        Projet projet2 = new Projet(11,"Construction","Construction salle de bain","Construction",quartiers, "2025-12-12","2026-10-10","vendredi", "Prévu");
        List<Projet> projets = List.of(projet1, projet2);
        List<Projet> result = ServerApp.filterProjetsDansLesTroisMois(projets);
        assertEquals(1, result.size());
        assertEquals("Rénovation", result.get(0).getTitre());
    }

}
