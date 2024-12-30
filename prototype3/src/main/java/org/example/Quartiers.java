package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe permettant la gestion des quartiers.
 */
public class Quartiers {
    private final ArrayList<String> quartiers;

    public Quartiers() {
        this.quartiers = makeQuartiers();
    }

    public ArrayList<String> getQuartiers() {return quartiers;}

    private static ArrayList<String> makeQuartiers() {
        ArrayList<String> qs = new ArrayList<>();
        qs.add("Ahuntsic-Cartierville");
        qs.add("Anjou");
        qs.add("Côte-des-Neiges-Notre-Dame-de-Grâce");
        qs.add("Lachine");
        qs.add("Lasalle");
        qs.add("Le Plateau-Mont-Royal");
        qs.add("Le Sud-Ouest");
        qs.add("L'Île-Bizard-Sainte-Geneviève");
        qs.add("Mercier-Hochelaga-Maisonneuve");
        qs.add("Montréal-Nord");
        qs.add("Outremont");
        qs.add("Pierrefonds-Roxboro");
        qs.add("Rivière-des-Prairies-Pointe-aux-Trembles");
        qs.add("Rosemont-La Petite-Patrie");
        qs.add("Saint-Laurent");
        qs.add("Saint-Léonard");
        qs.add("Verdun");
        qs.add("Ville-Marie");
        qs.add("Villeray-Saint-Michel-Parc-Extension");
        return qs;
    }

    /**
     * Méthode permettant d'obtenir le quartier d'un résident en fonction de son code postal.
     * @param codePostal Le code postal du résident
     * @return Le quartier du résident.
     * @throws IOException
     */
    public static String getQuartierFromCP(String codePostal) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("prototype3/src/main/resources/codesPostaux.csv"));
        String line;
        String quartier = "";
        String areaCode = codePostal.toUpperCase().substring(0, 3);
        while ((line = reader.readLine()) != null) {
            String[] tmp = line.split(",");
            if (areaCode.equals(tmp[0])) {
                quartier = tmp[1];
                break;
            }
        }
        return quartier;
    }
}
