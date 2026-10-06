package modelo;

import java.awt.Point;

import interfaces.IReachTarget;
import interfaces.IUpdatable;

public class PlayerMisil extends GameObjectTarget implements IUpdatable, IReachTarget{
    private double dx, dy;    
    private static final double SPEED = 450.0; // px/s

    public PlayerMisil(int x, int y, int targetX, int targetY) {
        super(x, y, targetX, targetY);

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
    	return Point.distance(x, y, targetX, targetY) < 15;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

}

