package modelo;

public abstract class GameObjectTarget {
	protected double x, y;
	protected int targetX, targetY;
	protected double dx, dy;

	public GameObjectTarget(int x, int y, int targetX, int targetY) {
		this.x = x;
	    this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	protected void initializeDirection(double speed) {
		double distance = Math.hypot(targetX - x, targetY - y);
		if (distance == 0) {
			dx = 0;
			dy = 0;
			return;
		}
		dx = ((targetX - x) / distance) * speed;
		dy = ((targetY - y) / distance) * speed;
	}

	protected void move(long deltaMs) {
		double deltaSec = deltaMs / 1000.0;
		x += dx * deltaSec;
		y += dy * deltaSec;
	}

	public double getDistanceToTarget() {
		return Math.hypot(x - targetX, y - targetY);
	}

	public boolean hasReachedTarget(double threshold) {
		return getDistanceToTarget() < threshold;
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}
}
/*
 * La clase encapsula la informacion del movimiento del proyectil:
 * su posicion actual, el objetivo y la direccion de avance.
 * Por eso es la clase que mejor sabe como moverse y como comprobar si llega al destino.
 */