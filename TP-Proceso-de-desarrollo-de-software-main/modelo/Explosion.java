package modelo;

import interfaces.IUpdatable;

public class Explosion implements IUpdatable {
    private int x, y;
    private final int maxRadius = 60;    // radio máximo
    private int radius;
    private int durationMs = 1000;  // duración fija 1 segundo
    private int elapsedTime = 0;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = 0;  // radio fijo para la explosión visible
    }

    public void update(long deltaMs) {
        elapsedTime += deltaMs;
        float frac = Math.min(1f, (float)elapsedTime / durationMs);
        radius = (int)(maxRadius * frac);
    }

    public boolean isFinished() {
        return elapsedTime >= durationMs;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public boolean hits(Misil misil) {
    	double dist2 = Math.pow(misil.getX() - x, 2) + Math.pow(misil.getY() - y, 2);
        return dist2 <= radius * radius;
    }

}
/*
 * La clase Explosion representa un efecto visual de explosión en el juego.
 * Se encarga de calcular la expansión progresiva del radio de la explosión 
 * hasta alcanzar un valor máximo, en un tiempo de duración fijo. 
 * Además, permite verificar si la explosión ha finalizado y si ha impactado 
 * con un objeto del tipo Misil. También proporciona un método para dibujar 
 * gráficamente la explosión con un efecto semitransparente.
 * Esta clase es útil para modelar efectos visuales de destrucción o impacto
 * dentro del entorno gráfico del juego.
 */
