package modelo;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import interfaces.ScoreRepository;

public class GameOverDialog {
    public enum Action {
        RESTART,
        RETURN_TO_MENU,
        CANCELLED
    }

    private final ScoreRepository scoreRepository;

    public GameOverDialog(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Action show(Component parent, int score) {
        List<ScoreEntry> scores = scoreRepository.loadScores();
        boolean qualifiesForTopTen = scores.size() < 10
                || score > scores.get(scores.size() - 1).getScore();

        if (qualifiesForTopTen) {
            String name;
            do {
                name = JOptionPane.showInputDialog(parent,
                        "¡Nuevo récord!\nIngresá tu nombre (3 letras):",
                        "Nuevo Puntaje", JOptionPane.PLAIN_MESSAGE);
                if (name == null) {
                    return Action.CANCELLED;
                }
                name = name.trim();
            } while (name.length() != 3);

            scores.add(new ScoreEntry(name.toUpperCase(), score));
            scoreRepository.saveScores(scores);
        }

        int option = JOptionPane.showOptionDialog(parent,
                "¡Game Over!\nTu puntaje final: " + score,
                "Fin del juego", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[] {"Jugar de nuevo", "Salir al menú"}, "Jugar de nuevo");

        return option == JOptionPane.YES_OPTION ? Action.RESTART : Action.RETURN_TO_MENU;
    }
}