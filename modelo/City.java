package modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

import interfaces.IUpdatable;

public class City extends GameObjectStart implements IUpdatable{
    private String name;
    private int health;
    private boolean destroyed;
    private Image explosionGif;
    private long explosionTimerMs;
    private static final long DURATION = 500;	//Duracion (ms) en la explosion 
    private URL urlImagen = City.class.getResource("/imagenes/explosion.gif");
    private Clip[] explosionClips = new Clip[2];
    private boolean soundPlayed = false;

    public City(String name, int x, int y) {
    	super(x,y);
    	this.name = name;
        this.health = 50;
        this.destroyed = false;
        
        //Inicializamos la imagen
        try {
        	URL urlImagen = getClass().getResource("/resources/explosion.gif");
        	if(urlImagen != null) {
        		explosionGif = new ImageIcon(urlImagen).getImage();
        	}
        } catch(Exception e){
        	System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
        
        //Inicializamos los audios de explosion
        String[] paths = { 
        		"/sounds/8bit-explosion1.wav", "/sounds/8bit-explosion2-low-resonant.wav" 
        };
        for (int i = 0; i < explosionClips.length; i++) {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(
                    getClass().getResource(paths[i])
                );
                explosionClips[i] = AudioSystem.getClip();
                explosionClips[i].open(ais);
            } catch (Exception e) {
                System.err.println("Error cargando sonido " + paths[i] + ": " + e);
            }
        }

    }

    public void takeDamage(int amount) {
        if (!destroyed) {
            health -= amount;
            if (health <= 0) {
                destroyed = true;
                explosionTimerMs = DURATION;
                playRandomExplosion();
            }
        }
    }
    
    private void playRandomExplosion() {
        if (soundPlayed) 
        	return; 
        int idx = ThreadLocalRandom.current().nextInt(explosionClips.length);
        Clip clip = explosionClips[idx];
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            soundPlayed = true;
        }
    }
    
    public void update(long deltaMs) {
    	if (destroyed && explosionTimerMs > 0) {
    		explosionTimerMs -= deltaMs;
    	}
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void reset() {
        this.health = 100;
        this.destroyed = false;
        soundPlayed = false;
        this.explosionTimerMs = 0;
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.setColor(Color.BLUE);
            g.fillRect(x - 30, y, 85, 10);		//Dibuja la Base
            g.fillRect(x + 10, y - 15, 10, 15); //Edificio pequeño
            g.fillRect(x + 30, y - 20, 15, 20); //Edificio grande
            g.fillRect(x - 10, y - 15, 10, 15); //Edificio pequeño
            g.fillRect(x - 30, y - 20, 15, 20); //Edificio grande
            g.fillRect(x + 45, y - 15, 10, 15); //Edificio pequeño
        }else if (explosionTimerMs > 0) {
            g.drawImage(explosionGif, x - 15, y - 50, 70, 80, null);
        }
    }
}
/*
 * Esta Clase se encarga del diseño y ciertos comportamientos ante eventos de una ciudad de nuestro juego
 * inspirado en Missile Command, tratamos de hacer una replica mas sencilla del juego que involucre un diseño
 * 8Bits con temporizadores en milisegundos, para mostrar imagenes y ciertos efectos de sonido que aparezcan
 * mediante un suceso como recibir daño de los misiles enemigo
 * */
