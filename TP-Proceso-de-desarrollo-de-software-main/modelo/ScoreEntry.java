package modelo;

public class ScoreEntry implements Comparable<ScoreEntry> {
    private String name;
    private int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreEntry o) {
        return Integer.compare(o.score, this.score); // Orden descendente
    }

    @Override
    public String toString() {
        return name + " - " + score;
    }
}

/*
 * La clase ScoreEntry hace una entrada de puntuación dentro del juego.
 * Cada instancia almacena el nombre del jugador y su puntaje asociado, lo que 
 * permite construir rankings o tablas de puntuación.
 * Implementa la interfaz Comparable para permitir su ordenamiento por puntaje 
 * de forma descendente, facilitando así la visualización de los mejores puntajes 
 * en primer lugar.
 * Esta clase es útil para registrar resultados y generar listados de "high scores".
 */