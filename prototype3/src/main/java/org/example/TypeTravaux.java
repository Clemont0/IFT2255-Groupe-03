package org.example;

import java.util.ArrayList;

/**
 * Classe permettant d'obtenir les types possibles d'un travail.
 */
public class TypeTravaux {
    private final ArrayList<String> types;

    public TypeTravaux() {
        this.types = makeTypes();
    }

    public ArrayList<String> getTypes() {return types;}

    private static ArrayList<String> makeTypes() {
        ArrayList<String> ts = new ArrayList<>();
        ts.add("Travaux routiers");
        ts.add("Travaux de gaz ou électricité");
        ts.add("Construction ou rénovation");
        ts.add("Entretien paysager");
        ts.add("Travaux liés aux transports en commun");
        ts.add("Travaux de signalisation et éclairage");
        ts.add("Travaux souterrains");
        ts.add("Travaux résidentiel");
        ts.add("Entretien urbain");
        ts.add("Entretien des réseaux de télécommunication");
        return ts;
    }
}
