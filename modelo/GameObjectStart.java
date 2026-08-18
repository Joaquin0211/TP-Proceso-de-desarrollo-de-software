package modelo;

public abstract class GameObjectStart {
	protected int x, y;
	
	public GameObjectStart(int x, int y) {
        this.x = x;
        this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}

/*
 * Clase abtracta centrada en el uso de la herencia y simplicidad de codigo, mayor legibilidad en
 * el codigo, esta destinada a la clase City unicamente  
 */