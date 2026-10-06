package modelo;

public class DefaultGameObjectFactory implements GameObjectFactory {
    @Override
    public City createCity(String name, int x, int y) {
        return new City(name, x, y);
    }

    @Override
    public Misil createEnemyMissile(int x, int y, int targetX, int targetY) {
        return new Misil(x, y, targetX, targetY);
    }

    @Override
    public PlayerMisil createPlayerMissile(int x, int y, int targetX, int targetY) {
        return new PlayerMisil(x, y, targetX, targetY);
    }

    @Override
    public Explosion createExplosion(int x, int y) {
        return new Explosion(x, y);
    }
}