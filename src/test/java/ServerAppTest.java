
import org.example.Entraves;
import org.example.ServerApp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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


}
