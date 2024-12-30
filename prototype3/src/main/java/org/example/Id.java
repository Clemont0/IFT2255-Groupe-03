package org.example;

import java.io.*;

/**
 * Classe qui gère les identifiants uniques
 */
public class Id {
    private static String url = "src/main/resources/ids.csv";

    private static int[] nextId() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(url));
        String line;
        String projetId = "";
        String requeteId = "";
        String cadidatureId = "";
        while ((line = reader.readLine()) != null) {
            String[] tmp = line.split(",");
            projetId = tmp[0];
            requeteId = tmp[1];
            cadidatureId = tmp[2];
        }
        int[] out = new int[3];
        out[0] = Integer.parseInt(projetId);
        out[1] = Integer.parseInt(requeteId);
        out[2] = Integer.parseInt(cadidatureId);
        return out;
    }

    /**
     * Méthode permettant d'obtenir le prochain identifiant valide pour un projet.
     * @return L'identifiant
     * @throws IOException
     */
    public static int nextProjetId() throws IOException {
        int[] ids = nextId();
        int pid = ids[0] + 1;
        int rid = ids[1];
        int cid = ids[2];
        writeId(pid + "," + rid + "," + cid);
        return pid;
    }

    /**
     * Méthode permettant d'obtenir le prochain identifiant valide pour une requête de travail.
     * @return L'identifiant
     * @throws IOException
     */
    public static int nextRequeteId() throws IOException {
        int[] ids = nextId();
        int pid = ids[0];
        int rid = ids[1] + 1;
        int cid = ids[2];
        writeId(pid + "," + rid + "," + cid);
        return rid;
    }

    /**
     * Méthode permettant d'obtenir le prochain identifiant valide pour une candidature.
     * @return L'identifiant
     * @throws IOException
     */
    public static int nextCandidateId() throws IOException {
        int[] ids = nextId();
        int pid = ids[0];
        int rid = ids[1];
        int cid = ids[2] + 1;
        writeId(pid + "," + rid + "," + cid);
        return rid;
    }

    private static void writeId(String data) throws IOException {
        File csvOutputFile = new File(url);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            pw.write(data);
        }
    }

}
