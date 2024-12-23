package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe qui contient tout ce qui est relié avec le parsing de données JSON.
 */
public class JSONParser {
    /**
     * Méthode permettant de parse un string JSON et obtenir une liste des travaux.
     * @param json Le string JSON à parse
     * @return La List des travaux.
     * @throws Exception
     */
    public static List<Travail> parseTravaux(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Travail>>() {});
    }

    /**
     * Méthode permettant de parse un string JSON et obtenir une liste des entraves.
     * @param jsonResponse Le string JSON à parse
     * @return La Lisr des entraves.
     * @throws IOException
     */
    public static List<Entraves> parseEntrave(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Entraves> entravesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Entraves>>() {});
        return entravesList;
    }

}

