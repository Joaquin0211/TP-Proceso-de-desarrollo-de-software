package modelo;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class GameObjectTarget {
	protected double x, y;
	protected int targetX, targetY;
	protected final int startX, startY;
	private IMovementStrategy movementStrategy;

	public GameObjectTarget(int x, int y, int targetX, int targetY, IMovementStrategy movementStrategy) {
		super();
		this.x = x;
	    this.y = y;
	    this.startX = x;
	    this.startY = y;
		this.targetX = targetX;
		this.targetY = targetY;
		setMovementStrategy(movementStrategy);
	}

	public void move(long deltaMs) {
		movementStrategy.move(this, deltaMs);
	}

	public void setMovementStrategy(IMovementStrategy movementStrategy) {
		if (movementStrategy == null) {
			throw new IllegalArgumentException("movementStrategy no puede ser null");
		}
		this.movementStrategy = movementStrategy;
	}
	
	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
		
}
/*
 * Clase abtracta centrada en el uso de la herencia y simplicidad de codigo, mayor legibilidad en
 * el codigo, esta destinada a las clases PlayerMisil y Misil  
 */