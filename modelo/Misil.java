package modelo;

import java.awt.*;

import interfaces.IReachTarget;
import interfaces.IUpdatable;

public class Misil extends GameObjectTarget implements IUpdatable, IReachTarget{
    private static final double SPEED = 200.0; // px/s
    private boolean markedForRemoval = false;

    public Misil(int x, int y, int targetX, int targetY) {
        this(x, y, targetX, targetY, new LinearMovementStrategy(SPEED));
    }

    public Misil(int x, int y, int targetX, int targetY, IMovementStrategy movementStrategy) {
        super(x, y, targetX, targetY, movementStrategy);
    }

    public void update(long deltaMs) {
        move(deltaMs);
    }


    public boolean hasReachedTarget() {
        return Point.distance(x, y, targetX, targetY) < 5;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillOval((int)(x - 2), (int)(y - 2), 6, 6); // 6x6 círculo rojo
    }

    // Método para marcarlo
    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    // Método para consultar el flag
    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }
}

/*
 * La clase Misil representa un proyectil en movimiento dentro del juego, 
 * dirigido hacia un objetivo determinado. Cada misil posee una posición inicial, 
 * un destino final y se desplaza a una velocidad constante.
 * Esta clase se encarga de calcular la trayectoria del misil, actualizar su 
 * posición en función del tiempo transcurrido, detectar si ha alcanzado su 
 * objetivo y dibujarse gráficamente en pantalla.
 * Además, incorpora un mecanismo para marcar el misil como "eliminado" 
 * cuando ya no debe permanecer activo en el juego (por ejemplo, al impactar).
 * Extiende la clase GameObjectTarget e implementa la interfaz IReachTarget, 
 * integrándose así con la lógica general de los elementos móviles del juego.
 */
