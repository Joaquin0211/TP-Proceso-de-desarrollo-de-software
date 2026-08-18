package modelo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MissileComand extends JPanel implements ActionListener, MouseListener {
    ArrayList<Misil> enemyMissiles;
    ArrayList<PlayerMisil> counterMissiles;
    ArrayList<City> cities;
    ArrayList<Explosion> explosions;

    private int missileSpawnClock = 0;
    private int maxMisil;
    private int misilCount;
    private int score;
    private long lastShotTime = 0;
    private static final long SHOT_COOLDOWN_MS = 500;
    private Clip bgmClip;
    private Clip shootClip;

    private Timer timer;
    private long lastTime;
    private final int baseX = 400, baseY = 500;
    public static final int COLLISION_RADIUS = 15;

    public MissileComand() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener(this);

        enemyMissiles = new ArrayList<>();
        counterMissiles = new ArrayList<>();
        cities = new ArrayList<>();
        explosions = new ArrayList<>();

        score = 0;
        misilCount = 0;
        maxMisil = 100;

        cities.add(new City("San Francisco", 100, 520));
        cities.add(new City("Santa Bárbara", 250, 520));
        cities.add(new City("Los Ángeles", 550, 520));
        cities.add(new City("San Diego", 700, 520));

        lastTime = System.currentTimeMillis();
        timer = new Timer(16, this); // ~60 FPS
        timer.start();

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/8bit-laser.wav"));
            shootClip = AudioSystem.getClip();
            shootClip.open(ais);
        } catch (Exception e) {
            System.err.println("Error al cargar sonido de disparo: " + e);
        }

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/8bit-music-for-game.wav"));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(ais);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {
            System.err.println("Error cargando BGM: " + e);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.GREEN);
        g2.fillRect(baseX - 25, baseY, 50, 30);

        for (PlayerMisil cm : counterMissiles) {
            cm.draw(g2, baseX, baseY);
        }

        for (Misil m : enemyMissiles) {
            m.draw(g2);
        }

        for (Explosion ex : explosions) {
            ex.draw(g2);
        }

        for (City city : cities) {
            city.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.drawString("Puntaje: " + score, 10, 20);
        
        
    }

    public void actionPerformed(ActionEvent e) {
    	long now    = System.currentTimeMillis();
        long delta  = now - lastTime;
        lastTime   = now;
        missileSpawnClock += delta;

        // 1) Generar nuevos misiles enemigos
        if (missileSpawnClock > 1000 && misilCount < maxMisil) {
            int startX = (int)(Math.random()*getWidth());
            int targetX = (int)(Math.random()*getWidth());
            enemyMissiles.add(new Misil(startX, 0, targetX, getHeight()));
            misilCount++;
            missileSpawnClock = 0;
        }

        // 2) Mover todos los objetos
        enemyMissiles .forEach(m -> m.update(delta));
        counterMissiles.forEach(cm-> cm.update(delta));
        explosions     .forEach(ex-> ex.update(delta));
        cities         .forEach(c -> c.update(delta));

        // 3) Detectar colisiones directo Misil vs PlayerMisil
        List<Misil> emToRemove = new ArrayList<>();
        List<PlayerMisil> pmToRemove = new ArrayList<>();

        final double collisionThresh = 8;  // prueba distintos valores (6–12)
        for (PlayerMisil pm : counterMissiles) {
          for (Misil em : enemyMissiles) {
            double dx = pm.getX() - em.getX();
            double dy = pm.getY() - em.getY();
            if (dx*dx + dy*dy <= COLLISION_RADIUS*COLLISION_RADIUS) {

              // 3.1) Creá la explosión justo en la posición del misil enemigo
              explosions.add(new Explosion((int)em.getX(), (int)em.getY()));
              pmToRemove.add(pm);
              emToRemove.add(em);
              score += 10;
            }
          }
        }
     // Explosión automática al llegar al destino
        List<PlayerMisil> toRemove = new ArrayList<>();
        for (PlayerMisil pm : counterMissiles) {
            if (pm.hasReachedTarget()) {
                explosions.add(new Explosion(pm.getTargetX(), pm.getTargetY()));
                toRemove.add(pm);
            }
        }
        counterMissiles.removeAll(toRemove);
        enemyMissiles.removeAll(emToRemove);

        // 4) Colisión explosión vs misiles remanentes (ondas expansivas)
        List<Misil> hitByBlast = new ArrayList<>();
        for (Explosion ex : explosions) {
          for (Misil em : enemyMissiles) {
            if (ex.hits(em)) {
              hitByBlast.add(em);
              score += 10;
            }
          }
        }
        enemyMissiles.removeAll(hitByBlast);

        // 5) Impacto de misiles sobre ciudades
        for (Misil em : enemyMissiles) {
          for (City city : cities) {
            if (!city.isDestroyed() &&
                Point.distance(em.getX(), em.getY(), city.getX(), city.getY()) < 25) {
              city.takeDamage(100);
              em.markForRemoval();  // marca el misil
            }
          }
        }
        enemyMissiles.removeIf(Misil::isMarkedForRemoval);

        // 6) Limpiar objetos acabados
        enemyMissiles.removeIf(Misil::hasReachedTarget);
        counterMissiles.removeIf(PlayerMisil::hasReachedTarget);
        explosions.removeIf(Explosion::isFinished);

        repaint();

        if (allCitiesDestroyed()) {
          timer.stop();
          showGameOverDialog();
        }
    }

    public void mousePressed(MouseEvent e) {
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= SHOT_COOLDOWN_MS) {
            counterMissiles.add(new PlayerMisil(baseX, baseY, e.getX(), e.getY()));
            lastShotTime = now;

            if (shootClip != null) {
                if (shootClip.isRunning()) shootClip.stop();
                shootClip.setFramePosition(0);
                shootClip.start();
            }
        } else {
            // Opcional: podés reproducir un sonido de error o feedback visual
            // System.out.println("Disparo bloqueado por cooldown");
        }
    }

    private boolean allCitiesDestroyed() {
        for (City city : cities) {
            if (!city.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        enemyMissiles.clear();
        counterMissiles.clear();
        explosions.clear();
        misilCount = 0;
        score = 0;
        missileSpawnClock = 0;
        for (City city : cities) {
            city.reset();
        }
        timer.start();
    }

    private void showGameOverDialog() {
        List<ScoreEntry> scores = ScoreManager.loadScores();

        boolean entraAlTop = scores.size() < 10 || score > scores.get(scores.size() - 1).getScore();

        if (entraAlTop) {
            String nombre;
            do {
                nombre = JOptionPane.showInputDialog(this, "¡Nuevo récord!\nIngresá tu nombre (3 letras):", "Nuevo Puntaje", JOptionPane.PLAIN_MESSAGE);
                if (nombre == null) return;
            } while (nombre.trim().length() != 3);
            nombre = nombre.trim().toUpperCase();
            scores.add(new ScoreEntry(nombre, score));
            ScoreManager.saveScores(scores);
        }

        int option = JOptionPane.showOptionDialog(
                this,
                "¡Game Over!\nTu puntaje final: " + score,
                "Fin del juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Jugar de nuevo", "Salir al menú"},
                "Jugar de nuevo"
        );

        if (option == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            new MainMenu();
        }
    }

    // Unused
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
/*
 * La clase se enfoca en el diseño por pantalla de las entidades y el comportamiento ante todo 
 * tipo de eventos por pantalla. Los distintos comportamientos a remarcar son:
 * 
 * - Creacion de listas para guardar misiles, ciudades y explosiones.
 * 
 * - Generacion de ciudades con posiciones fijas.
 * 
 * - Configuracion de sonidos (disparo y música de fondo).
 * 
 * - Inicio de un Timer que simula el bucle principal del juego (en 60 FPS).
 * 
 * - Generación de misiles enemigos cada cierto tiempo, apareciendo un nuevo misil 
 * desde un punto aleatorio del borde superior.
 * 
 * - Actualización de objetos en sus estados.
 * 
 * - Colisiones del los misiles del jugador vs enemigos, eliminandose ambos al impactar y sumando puntaje.
 * 
 * - Explosiones que afectan a otros misiles, pudiendo eliminar misiles cercanos por "onda expansiva".
 * 
 * - Colision de impactos que dañan a las ciudades.
 * 
 * - Final de juego cuando todas las ciudades fueron destruidas, deteniendo el juego y mostrando 
 * un diálogo de Game Over.
 * 
 * - CoolDown de 500 milisegundos al disparar un misil con el mouse 
 * */
