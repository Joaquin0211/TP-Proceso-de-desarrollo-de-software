package modelo;

import java.awt.*;

import interfaces.IReachTarget;
import interfaces.IUpdatable;

public class PlayerMisil extends GameObjectTarget implements IUpdatable, IReachTarget{
    private static final double SPEED = 450.0; // px/s

    public PlayerMisil(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);
        initializeDirection(SPEED);
    }

    public void update(long deltaMs) {
        move(deltaMs);
    }

    public boolean hasReachedTarget() {
    	return hasReachedTarget(15);
    }

    public void draw(Graphics2D g2, int baseX, int baseY) {
        g2.setColor(Color.CYAN);
        g2.drawLine(baseX, baseY, getX(), getY());
    }

}

/*
 * La clase PlayerMisil representa un misil disparado por el jugador hacia una 
 * posición objetivo específica dentro del entorno del juego.
 * Esta clase calcula su trayectoria en función de la distancia y una velocidad 
 * fija superior a la de los misiles enemigos, lo que le permite interceptar 
 * objetivos de forma eficiente. Su comportamiento incluye la actualización 
 * de su posición con el paso del tiempo, la detección de colisión con su 
 * objetivo y la visualización gráfica mediante una línea desde la base del 
 * jugador hasta su posición actual.
 * Hereda de GameObjectTarget y utiliza la interfaz IPlayerMisil, lo cual permite
 * integrarla de manera polimórfica dentro del sistema de juego.
 */
