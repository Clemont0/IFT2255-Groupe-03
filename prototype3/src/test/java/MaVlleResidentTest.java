

import org.example.MaVilleResident;
import org.example.Travail;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaVlleResidentTest {

     @Test
    public void testFiltrerQuartier() {
        Travail travail1 = new Travail();
        travail1.setId("1");
        travail1.setBoroughId("Quartier1");
        travail1.setCurrentStatus("Travail1");
        travail1.setReasonCategory("Raison1");
        travail1.setSubmitterCategory("Statut1");
        travail1.setOrganizationName("Soumetteur1");
        Travail travail2 = new Travail();
        travail2.setId("2");
        travail2.setBoroughId("Quartier2");
        travail2.setCurrentStatus("Travail2");
        travail2.setReasonCategory("Raison2");
        travail2.setSubmitterCategory("Statut2");
        travail2.setOrganizationName("Soumetteur2");
        List<Travail> travaux = List.of(travail1, travail2);
        List<Travail> result = MaVilleResident.filtrerQuartier(travaux, "Quartier1");
        assertEquals(1, result.size());
        assertEquals("Quartier1", result.get(0).getBoroughId());
    }

    @Test
    public void testFiltrerParType() {
        Travail travail1 = new Travail();
        travail1.setId("1");
        travail1.setBoroughId("Quartier1");
        travail1.setCurrentStatus("EnCours");
        travail1.setReasonCategory("Type1"); // Type de travail
        travail1.setSubmitterCategory("Statut1");
        travail1.setOrganizationName("Soumetteur1");
        Travail travail2 = new Travail();
        travail2.setId("2");
        travail2.setBoroughId("Quartier2");
        travail2.setCurrentStatus("Terminé");
        travail2.setReasonCategory("Type2");
        travail2.setSubmitterCategory("Statut2");
        travail2.setOrganizationName("Soumetteur2");
        List<Travail> travaux = List.of(travail1, travail2);
        List<Travail> result = MaVilleResident.filtrerParType(travaux, "Type1");
        assertEquals(1, result.size());
        assertEquals("Type1", result.get(0).getReasonCategory());
    }

    @Test
    void testFiltrerTravauxTroisMois() {
        Travail travail1 = new Travail();
        travail1.setId("1");
        travail1.setBoroughId("Quartier1");
        travail1.setCurrentStatus("EnCours");
        travail1.setReasonCategory("Type1"); // Type de travail
        travail1.setSubmitterCategory("Statut1");
        travail1.setOrganizationName("Soumetteur1");
        travail1.setDuration_start_date("2025-01-10");
        Travail travail2 = new Travail();
        travail2.setId("2");
        travail2.setBoroughId("Quartier2");
        travail2.setCurrentStatus("Terminé");
        travail2.setReasonCategory("Type2");
        travail2.setSubmitterCategory("Statut2");
        travail2.setOrganizationName("Soumetteur2");
        travail2.setDuration_start_date("2025-12-12");
        List<Travail> travaux = List.of(travail1, travail2);
        List<Travail> result = MaVilleResident.filtrer3mois(travaux);
        assertEquals(1, result.size());
        assertEquals("Type1", result.get(0).getReasonCategory());
    }

    @Test
    void testVoirNotificationsVide() {
        List<Map<String, Object>> notif = new ArrayList<>();
        if (notif.isEmpty()) {
            assertEquals(0, notif.size(), "La liste doit etre vide");
        } else {
            fail("Pas de data attendu");
        }
    }

    @Test
    void testVoirNotificationsNonVide() {
        List<Map<String, Object>> notif = new ArrayList<>();
        Map<String, Object> notif1 = new HashMap<>();
        notif1.put("projetId", 1);
        notif1.put("message", "Nouveau projet!");
        notif.add(notif1);
        Map<String, Object> notif2 = new HashMap<>();
        notif2.put("projetId", 2);
        notif2.put("message", "Projet modifié!");
        notif.add(notif2);
        assertEquals(2, notif.size(), "La taille doit être 2");
        assertEquals(1, notif.get(0).get("projetId"));
    }

}
