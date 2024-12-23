package org.example;

import java.util.ArrayList;

/**
 * Classe permettant d'obtenir les statuts possibles d'un travail.
 */
public class StatutTravaux {
    private final ArrayList<String> statuts;

    public StatutTravaux() {
        this.statuts = makeStatut();
    }

    public ArrayList<String> getStatuts() {return statuts;}

    private ArrayList<String> makeStatut() {
        ArrayList<String> st = new ArrayList<>();
        st.add("Prévu");
        st.add("En cours");
        st.add("Suspendu");
        st.add("Terminé");
        return st;
    }
}
