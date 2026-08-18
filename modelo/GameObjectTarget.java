package modelo;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class GameObjectTarget {
	protected double x, y;
	protected int targetX, targetY;

	public GameObjectTarget(int x, int y, int targetX, int targetY) {
		super();
		this.x = x;
	    this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
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