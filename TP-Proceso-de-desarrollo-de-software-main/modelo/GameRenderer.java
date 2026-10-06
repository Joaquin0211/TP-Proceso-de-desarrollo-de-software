package modelo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class GameRenderer {
    private static final int BASE_X = 400;
    private static final int BASE_Y = 500;
    private final Image cityExplosionImage;

    public GameRenderer() {
        URL imageUrl = getClass().getResource("/resources/explosion.gif");
        cityExplosionImage = imageUrl == null ? null : new ImageIcon(imageUrl).getImage();
    }

    public void draw(Graphics2D graphics, GameSession session) {
        graphics.setColor(Color.GREEN);
        graphics.fillRect(BASE_X - 25, BASE_Y, 50, 30);

        for (PlayerMisil missile : session.getCounterMissiles()) {
            graphics.setColor(Color.CYAN);
            graphics.drawLine(BASE_X, BASE_Y, missile.getX(), missile.getY());
        }

        for (Misil missile : session.getEnemyMissiles()) {
            graphics.setColor(Color.RED);
            graphics.fillOval(missile.getX() - 2, missile.getY() - 2, 6, 6);
        }

        for (Explosion explosion : session.getExplosions()) {
            graphics.setColor(new Color(255, 0, 0, 128));
            int radius = explosion.getRadius();
            graphics.fillOval(explosion.getX() - radius, explosion.getY() - radius, radius * 2, radius * 2);
        }

        for (City city : session.getCities()) {
            drawCity(graphics, city);
        }

        graphics.setColor(Color.WHITE);
        graphics.drawString("Puntaje: " + session.getScore(), 10, 20);
    }

    private void drawCity(Graphics2D graphics, City city) {
        int x = city.getX();
        int y = city.getY();
        if (!city.isDestroyed()) {
            graphics.setColor(Color.BLUE);
            graphics.fillRect(x - 30, y, 85, 10);
            graphics.fillRect(x + 10, y - 15, 10, 15);
            graphics.fillRect(x + 30, y - 20, 15, 20);
            graphics.fillRect(x - 10, y - 15, 10, 15);
            graphics.fillRect(x - 30, y - 20, 15, 20);
            graphics.fillRect(x + 45, y - 15, 10, 15);
        } else if (city.getExplosionTimerMs() > 0) {
            if (cityExplosionImage != null) {
                graphics.drawImage(cityExplosionImage, x - 15, y - 50, 70, 80, null);
            } else {
                graphics.setColor(Color.ORANGE);
                graphics.fillOval(x - 20, y - 40, 45, 45);
            }
        }
    }
}