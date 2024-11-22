package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

import java.util.List;

public class JSONParser {
    public static List<Travail> parseTravaux(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<Travail>>() {});
    }
    public static List<Entraves> parseEntrave(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Entraves> entravesList = objectMapper.readValue(jsonResponse, new TypeReference<List<Entraves>>() {});
        return entravesList;
    }
}

