package modelo;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String FILE_NAME = "scores.txt";

    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // Si no existe, crearlo con datos base
            inicializarScores(scores);
            saveScores(scores);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new ScoreEntry(name, score));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo de puntajes: " + e);
        }

        Collections.sort(scores);
        return scores;
    }

    public static void saveScores(List<ScoreEntry> scores) {
        // Ordenar de mayor a menor
        Collections.sort(scores);

        // Limitar a los 10 primeros
        if (scores.size() > 10) {
            scores = scores.subList(0, 10); // Solo los mejores 10
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (ScoreEntry entry : scores) {
                writer.println(entry.getName() + "," + entry.getScore());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar puntajes: " + e);
        }
    }


    private static void inicializarScores(List<ScoreEntry> scores) {
        scores.add(new ScoreEntry("AAA", 900));
        scores.add(new ScoreEntry("BOB", 850));
        scores.add(new ScoreEntry("ZED", 800));
        scores.add(new ScoreEntry("MAX", 750));
        scores.add(new ScoreEntry("SAM", 700));
        scores.add(new ScoreEntry("LIZ", 650));
        scores.add(new ScoreEntry("TED", 600));
        scores.add(new ScoreEntry("IVY", 550));
        scores.add(new ScoreEntry("JAY", 500));
        scores.add(new ScoreEntry("KIM", 450));
    }
}

/*
 * La clase ScoreManager se encarga de gestionar el almacenamiento y recuperación 
 * de las puntuaciones del juego desde un archivo de texto persistente ("scores.txt").
 * Proporciona métodos estáticos para:
 * - Cargar los puntajes desde el archivo, creando uno nuevo con datos predefinidos si no existe.
 * - Guardar las puntuaciones actuales, manteniendo solamente las 10 mejores en orden descendente.
 * Esta clase facilita la persistencia del sistema de "high scores", permitiendo 
 * que los logros de los jugadores se mantengan entre ejecuciones del programa.
 */