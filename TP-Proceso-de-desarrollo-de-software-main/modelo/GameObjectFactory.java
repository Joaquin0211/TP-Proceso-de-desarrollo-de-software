package modelo;

public interface GameObjectFactory {
    City createCity(String name, int x, int y);

    Misil createEnemyMissile(int x, int y, int targetX, int targetY);

    PlayerMisil createPlayerMissile(int x, int y, int targetX, int targetY);

    Explosion createExplosion(int x, int y);
}