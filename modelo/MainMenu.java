package modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton startButton = new JButton("Iniciar");
        JButton scoreButton = new JButton("Tabla de Puntajes");
        JButton exitButton = new JButton("Salir");

        startButton.addActionListener(e -> {
            new GameFrame(); // Lanza el juego
            dispose();       // Cierra menú
        });

        scoreButton.addActionListener(e -> {
            List<ScoreEntry> scores = ScoreManager.loadScores();
            StringBuilder sb = new StringBuilder("Top 10 Puntajes:\n\n");
            for (int i = 0; i < scores.size(); i++) {
                sb.append((i + 1)).append(". ").append(scores.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Tabla de Puntajes", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> System.exit(0));

        panel.add(new JLabel("Missile Command", SwingConstants.CENTER));
        panel.add(startButton);
        panel.add(scoreButton);
        panel.add(exitButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}

/*
 * La clase MainMenu representa el menú principal de la aplicación "Missile Command".
 * Extiende JFrame y proporciona una interfaz gráfica con opciones para:
 * - Iniciar el juego (creando una nueva instancia de GameFrame),
 * - Visualizar la tabla de los 10 mejores puntajes mediante una ventana emergente,
 * - Salir del programa.
 * Utiliza componentes Swing como botones, etiquetas y paneles con diseño en cuadrícula
 * para organizar las opciones de forma clara y accesible.
 * Esta clase constituye el punto de entrada visual del juego y gestiona la navegación
 * inicial del usuario.
 */