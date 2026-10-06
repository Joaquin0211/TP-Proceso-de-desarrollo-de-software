package modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import interfaces.GameAudio;
import interfaces.ScoreRepository;

public class MissileComand extends JPanel implements ActionListener, MouseListener {
    private static final long SHOT_COOLDOWN_MS = 500;

    private final ScoreRepository scoreRepository;
    private final GameObjectFactory objectFactory;
    private final GameAudio gameAudio;
    private final GameSession session;
    private final GameRenderer renderer;
    private final GameOverDialog gameOverDialog;
    private Timer timer;
    private long lastTime;
    private long lastShotTime;

    public MissileComand() {
        this(new ScoreManager(), new DefaultGameObjectFactory(), new SwingGameAudio());
    }

    public MissileComand(ScoreRepository scoreRepository) {
        this(scoreRepository, new DefaultGameObjectFactory(), new SwingGameAudio());
    }

    public MissileComand(ScoreRepository scoreRepository, GameObjectFactory objectFactory, GameAudio gameAudio) {
        this.scoreRepository = scoreRepository;
        this.objectFactory = objectFactory;
        this.gameAudio = gameAudio;
        this.session = new GameSession(objectFactory, gameAudio);
        this.renderer = new GameRenderer();
        this.gameOverDialog = new GameOverDialog(scoreRepository);

        setBackground(Color.BLACK);
        setFocusable(true);
        addMouseListener(this);

        lastTime = System.currentTimeMillis();
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
        gameAudio.startBackgroundMusic();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.draw((Graphics2D) g, session);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.currentTimeMillis();
        long delta = now - lastTime;
        lastTime = now;
        session.update(delta, getWidth(), getHeight());
        repaint();

        if (session.isGameOver()) {
          timer.stop();
          showGameOverDialog();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= SHOT_COOLDOWN_MS) {
            session.fireAt(e.getX(), e.getY());
            lastShotTime = now;
            gameAudio.playShot();
        }
    }

    private void resetGame() {
        session.reset();
        lastTime = System.currentTimeMillis();
        timer.start();
    }

    private void showGameOverDialog() {
        GameOverDialog.Action action = gameOverDialog.show(this, session.getScore());
        if (action == GameOverDialog.Action.RESTART) {
            resetGame();
        } else if (action == GameOverDialog.Action.RETURN_TO_MENU) {
            gameAudio.stopBackgroundMusic();
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.dispose();
            }
            new MainMenu(scoreRepository, objectFactory, gameAudio);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
