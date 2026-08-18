package modelo;

import java.awt.*;

public class Explosion {
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

    public boolean hits(Misil misil) {
    	double dist2 = Math.pow(misil.getX() - x, 2) + Math.pow(misil.getY() - y, 2);
        return dist2 <= radius * radius;
    }

    public void draw(Graphics2D g2) {
        // Dibuja un círculo rojo semitransparente fijo
        g2.setColor(new Color(255, 0, 0, 128));
        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
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
