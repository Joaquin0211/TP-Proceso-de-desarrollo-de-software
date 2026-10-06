package modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import interfaces.GameAudio;
import interfaces.IReachTarget;
import interfaces.IUpdatable;

public class GameSession {
    private static final int MAX_ENEMY_MISSILES = 100;
    private static final int COLLISION_RADIUS = 15;
    private static final int BASE_X = 400;
    private static final int BASE_Y = 500;

    private final GameObjectFactory objectFactory;
    private final GameAudio gameAudio;
    private final Random random;
    private final List<Misil> enemyMissiles = new ArrayList<>();
    private final List<PlayerMisil> counterMissiles = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();

    private long missileSpawnClock;
    private int missileCount;
    private int score;

    public GameSession(GameObjectFactory objectFactory, GameAudio gameAudio) {
        this(objectFactory, gameAudio, new Random());
    }

    public GameSession(GameObjectFactory objectFactory, GameAudio gameAudio, Random random) {
        if (objectFactory == null || gameAudio == null || random == null) {
            throw new IllegalArgumentException("Las dependencias de la partida son obligatorias");
        }
        this.objectFactory = objectFactory;
        this.gameAudio = gameAudio;
        this.random = random;

        cities.add(objectFactory.createCity("San Francisco", 100, 520));
        cities.add(objectFactory.createCity("Santa Bárbara", 250, 520));
        cities.add(objectFactory.createCity("Los Ángeles", 550, 520));
        cities.add(objectFactory.createCity("San Diego", 700, 520));
    }

    public void update(long deltaMs, int width, int height) {
        long elapsedMs = Math.max(0, deltaMs);
        missileSpawnClock += elapsedMs;

        if (missileSpawnClock > 1000 && missileCount < MAX_ENEMY_MISSILES) {
            int safeWidth = Math.max(width, 1);
            int safeHeight = Math.max(height, 1);
            enemyMissiles.add(objectFactory.createEnemyMissile(
                    random.nextInt(safeWidth), 0,
                    random.nextInt(safeWidth), safeHeight));
            missileCount++;
            missileSpawnClock = 0;
        }

        updateObjects(elapsedMs);
        resolveDirectCollisions();
        createExplosionsAtPlayerTargets();
        resolveBlastCollisions();
        resolveCityImpacts();
        removeFinishedObjects();
    }

    private void updateObjects(long deltaMs) {
        updateAll(enemyMissiles, deltaMs);
        updateAll(counterMissiles, deltaMs);
        updateAll(explosions, deltaMs);
        updateAll(cities, deltaMs);
    }

    private void updateAll(List<? extends IUpdatable> objects, long deltaMs) {
        for (IUpdatable object : objects) {
            object.update(deltaMs);
        }
    }

    private void resolveDirectCollisions() {
        List<Misil> enemiesToRemove = new ArrayList<>();
        List<PlayerMisil> playerMissilesToRemove = new ArrayList<>();

        for (PlayerMisil playerMissile : counterMissiles) {
            for (Misil enemyMissile : enemyMissiles) {
                double dx = playerMissile.getX() - enemyMissile.getX();
                double dy = playerMissile.getY() - enemyMissile.getY();
                if (dx * dx + dy * dy <= COLLISION_RADIUS * COLLISION_RADIUS) {
                    explosions.add(objectFactory.createExplosion(enemyMissile.getX(), enemyMissile.getY()));
                    playerMissilesToRemove.add(playerMissile);
                    enemiesToRemove.add(enemyMissile);
                    score += 10;
                }
            }
        }

        counterMissiles.removeAll(playerMissilesToRemove);
        enemyMissiles.removeAll(enemiesToRemove);
    }

    private void createExplosionsAtPlayerTargets() {
        List<PlayerMisil> reachedTargets = new ArrayList<>();
        for (PlayerMisil missile : counterMissiles) {
            if (missile.hasReachedTarget()) {
                explosions.add(objectFactory.createExplosion(missile.getTargetX(), missile.getTargetY()));
                reachedTargets.add(missile);
            }
        }
        counterMissiles.removeAll(reachedTargets);
    }

    private void resolveBlastCollisions() {
        List<Misil> enemiesHitByBlast = new ArrayList<>();
        for (Explosion explosion : explosions) {
            for (Misil enemyMissile : enemyMissiles) {
                if (explosion.hits(enemyMissile)) {
                    enemiesHitByBlast.add(enemyMissile);
                    score += 10;
                }
            }
        }
        enemyMissiles.removeAll(enemiesHitByBlast);
    }

    private void resolveCityImpacts() {
        for (Misil enemyMissile : enemyMissiles) {
            for (City city : cities) {
                if (!city.isDestroyed()
                        && Point.distance(enemyMissile.getX(), enemyMissile.getY(), city.getX(), city.getY()) < 25) {
                    city.takeDamage(100);
                    enemyMissile.markForRemoval();
                    if (city.isDestroyed()) {
                        gameAudio.playCityExplosion();
                    }
                }
            }
        }
    }

    private void removeFinishedObjects() {
        enemyMissiles.removeIf(Misil::isMarkedForRemoval);
        enemyMissiles.removeIf(IReachTarget::hasReachedTarget);
        counterMissiles.removeIf(IReachTarget::hasReachedTarget);
        explosions.removeIf(Explosion::isFinished);
    }

    public void fireAt(int targetX, int targetY) {
        counterMissiles.add(objectFactory.createPlayerMissile(BASE_X, BASE_Y, targetX, targetY));
    }

    public void reset() {
        enemyMissiles.clear();
        counterMissiles.clear();
        explosions.clear();
        missileCount = 0;
        score = 0;
        missileSpawnClock = 0;
        for (City city : cities) {
            city.reset();
        }
    }

    public boolean isGameOver() {
        for (City city : cities) {
            if (!city.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public int getScore() {
        return score;
    }

    public List<Misil> getEnemyMissiles() {
        return Collections.unmodifiableList(enemyMissiles);
    }

    public List<PlayerMisil> getCounterMissiles() {
        return Collections.unmodifiableList(counterMissiles);
    }

    public List<City> getCities() {
        return Collections.unmodifiableList(cities);
    }

    public List<Explosion> getExplosions() {
        return Collections.unmodifiableList(explosions);
    }
}