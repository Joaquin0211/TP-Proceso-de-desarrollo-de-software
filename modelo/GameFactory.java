package modelo;

public class GameFactory {

    public static City createCity(String name, int x, int y) {
        return new City(name, x, y);
    }

    public static Misil createEnemyMissile(int x, int y, int targetX, int targetY) {
        return new Misil(x, y, targetX, targetY);
    }

    public static PlayerMisil createPlayerMissile(int x, int y, int targetX, int targetY) {
        return new PlayerMisil(x, y, targetX, targetY);
    }

    public static Explosion createExplosion(int x, int y) {
        return new Explosion(x, y);
    }
}
