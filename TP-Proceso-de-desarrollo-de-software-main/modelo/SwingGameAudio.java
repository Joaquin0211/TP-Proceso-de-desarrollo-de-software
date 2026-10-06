package modelo;

import java.util.concurrent.ThreadLocalRandom;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import interfaces.GameAudio;

public class SwingGameAudio implements GameAudio {
    private final Clip backgroundMusic;
    private final Clip shotSound;
    private final Clip[] cityExplosionSounds;

    public SwingGameAudio() {
        backgroundMusic = loadClip("/sounds/8bit-music-for-game.wav");
        shotSound = loadClip("/sounds/8bit-laser.wav");
        cityExplosionSounds = new Clip[] {
            loadClip("/sounds/8bit-explosion1.wav"),
            loadClip("/sounds/8bit-explosion2-low-resonant.wav")
        };
    }

    @Override
    public void startBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    @Override
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.setFramePosition(0);
        }
    }

    @Override
    public void playShot() {
        playFromStart(shotSound);
    }

    @Override
    public void playCityExplosion() {
        Clip sound = cityExplosionSounds[ThreadLocalRandom.current().nextInt(cityExplosionSounds.length)];
        playFromStart(sound);
    }

    private Clip loadClip(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            System.err.println("Error al cargar sonido " + path + ": " + e.getMessage());
            return null;
        }
    }

    private void playFromStart(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }
}