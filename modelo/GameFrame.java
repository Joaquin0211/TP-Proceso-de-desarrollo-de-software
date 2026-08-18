package modelo;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Missile Command");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        add(new MissileComand());
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}

/*
 * La clase GameFrame define la ventana principal del juego "Missile Command".
 * Extiende la clase JFrame de Swing y configura los parámetros básicos de la 
 * interfaz gráfica, incluyendo título, tamaño fijo, comportamiento al cerrar 
 * la ventana y ubicación centrada en la pantalla.
 * En su interior, se agrega una instancia de la clase MisileCommand, que 
 * representa el panel principal del juego donde se desarrolla la lógica y 
 * los gráficos interactivos.
 * La ejecución del juego comienza desde el método main(), que instancia y 
 * despliega esta ventana principal.
 */
