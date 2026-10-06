package modelo;

import java.awt.Point;

import interfaces.IReachTarget;
import interfaces.IUpdatable;

public class Misil extends GameObjectTarget implements IUpdatable, IReachTarget{
    private double dx, dy; 
    private static final double SPEED = 200.0; // px/s
    private boolean markedForRemoval = false;

    public Misil(int x, int y, int targetX, int targetY) {
        super(x,y, targetX, targetY);

        double distance = Point.distance(x, y, targetX, targetY);
        dx = (targetX - x) / distance * SPEED;
        dy = (targetY - y) / distance * SPEED;
    }

    public void update(long deltaMs) {
    	double deltaSec = deltaMs / 1000.0;
        x += dx * deltaSec;
        y += dy * deltaSec;
    }


    public boolean hasReachedTarget() {
        return Point.distance(x, y, targetX, targetY) < 5;
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

