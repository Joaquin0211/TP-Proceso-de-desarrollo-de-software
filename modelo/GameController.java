package modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {
    private final List<Misil> enemyMissiles;
    private final List<PlayerMisil> playerMissiles;
    private final List<Explosion> explosions;
    private final List<City> cities;
    private int score;
    private int missileCount;

    public GameController(List<City> cities) {
        this.cities = cities;
        this.enemyMissiles = new ArrayList<>();
        this.playerMissiles = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.score = 0;
        this.missileCount = 0;
    }

    public List<Misil> getEnemyMissiles() {
        return Collections.unmodifiableList(enemyMissiles);
    }

    public List<PlayerMisil> getPlayerMissiles() {
        return Collections.unmodifiableList(playerMissiles);
    }

    public List<Explosion> getExplosions() {
        return Collections.unmodifiableList(explosions);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int value) {
        score += value;
    }

    public void addEnemyMissile(int startX, int endY, int targetX, int targetY) {
        enemyMissiles.add(GameFactory.createEnemyMissile(startX, endY, targetX, targetY));
        missileCount++;
    }

    public void addPlayerMissile(int x, int y, int targetX, int targetY) {
        playerMissiles.add(GameFactory.createPlayerMissile(x, y, targetX, targetY));
    }

    public void addExplosion(int x, int y) {
        explosions.add(GameFactory.createExplosion(x, y));
    }

    public boolean allCitiesDestroyed() {
        for (City city : cities) {
            if (!city.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public void updateEnemyMissiles(long deltaMs) {
        enemyMissiles.forEach(m -> m.update(deltaMs));
    }

    public void updatePlayerMissiles(long deltaMs) {
        playerMissiles.forEach(m -> m.update(deltaMs));
    }

    public void updateExplosions(long deltaMs) {
        explosions.forEach(e -> e.update(deltaMs));
    }

    public void updateCities(long deltaMs) {
        cities.forEach(city -> city.update(deltaMs));
    }

    public void handleEnemyCityImpact() {
        for (Misil misil : enemyMissiles) {
            for (City city : cities) {
                if (city.isHitBy(misil.getX(), misil.getY(), 25)) {
                    city.takeDamage(100);
                    misil.markForRemoval();
                }
            }
        }

        enemyMissiles.removeIf(Misil::isMarkedForRemoval);
    }

    public void handlePlayerEnemyCollision() {
        List<Misil> remEnemy = new ArrayList<>();
        List<PlayerMisil> remPlayer = new ArrayList<>();

        for (PlayerMisil pm : playerMissiles) {
            for (Misil em : enemyMissiles) {
                double dx = pm.getX() - em.getX();
                double dy = pm.getY() - em.getY();
                if (dx * dx + dy * dy <= 225) {
                    addExplosion(em.getX(), em.getY());
                    remPlayer.add(pm);
                    remEnemy.add(em);
                    addScore(10);
                }
            }
        }

        playerMissiles.removeAll(remPlayer);
        enemyMissiles.removeAll(remEnemy);
    }

    public void handleTargetReachedPlayerShots() {
        List<PlayerMisil> remPlayer = new ArrayList<>();

        for (PlayerMisil pm : playerMissiles) {
            if (pm.hasReachedTarget()) {
                addExplosion(pm.getTargetX(), pm.getTargetY());
                remPlayer.add(pm);
            }
        }

        playerMissiles.removeAll(remPlayer);
    }

    public void handleBlastCollisions() {
        List<Misil> hitByBlast = new ArrayList<>();

        for (Explosion ex : explosions) {
            for (Misil em : enemyMissiles) {
                if (ex.hits(em)) {
                    hitByBlast.add(em);
                    addScore(10);
                }
            }
        }

        enemyMissiles.removeAll(hitByBlast);
    }

    public void cleanupFinishedObjects() {
        enemyMissiles.removeIf(Misil::hasReachedTarget);
        playerMissiles.removeIf(PlayerMisil::hasReachedTarget);
        explosions.removeIf(Explosion::isFinished);
    }

    public void reset(int scoreValue) {
        enemyMissiles.clear();
        playerMissiles.clear();
        explosions.clear();
        missileCount = 0;
        score = scoreValue;
        for (City city : cities) {
            city.reset();
        }
    }

    public int getMissileCount() {
        return missileCount;
    }

    public void setMissileCount(int value) {
        missileCount = value;
    }

    public void setScore(int value) {
        score = value;
    }
}
