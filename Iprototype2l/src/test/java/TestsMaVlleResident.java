

import org.example.MaVilleResident;
import org.example.Travail;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestsMaVlleResident {

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
    public void testIsValidDate() {
        assertTrue(MaVilleResident.isValidDate("2024-11-22"));  // Date correcte
        assertTrue(MaVilleResident.isValidDate("2000-01-01"));  // Date au format valide
    }



}
