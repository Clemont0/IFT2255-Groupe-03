import java.util.ArrayList;
import java.util.List;

public class Registre {

    private List<Resident> residentsInscrits;
    private List<Intervenant> intervenantsInscrits;

    public Registre() {
        this.residentsInscrits = new ArrayList<>();
        this.intervenantsInscrits = new ArrayList<>();
    }

    public List<Intervenant> getIntervenantsInscrits() {
        return intervenantsInscrits;
    }

    public List<Resident> getResidentsInscrits() {
        return residentsInscrits;
    }


    public void inscriptionResident(Resident resident) {
        residentsInscrits.add(resident);
    }

    public void inscriptionIntervenant(Intervenant intervenant){
        intervenantsInscrits.add(intervenant);
    }


}