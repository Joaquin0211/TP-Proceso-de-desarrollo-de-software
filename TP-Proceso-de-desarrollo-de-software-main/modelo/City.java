package modelo;

import interfaces.IUpdatable;

public class City extends GameObjectStart implements IUpdatable{
    private String name;
    private int health;
    private boolean destroyed;
    private long explosionTimerMs;
    private static final long DURATION = 500;

    public City(String name, int x, int y) {
    	super(x,y);
    	this.name = name;
        this.health = 50;
        this.destroyed = false;
    }

    public void takeDamage(int amount) {
        if (!destroyed) {
            health -= amount;
            if (health <= 0) {
                destroyed = true;
                explosionTimerMs = DURATION;
            }
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

    public long getExplosionTimerMs() {
        return explosionTimerMs;
    }

    public String getName() {
        return name;
    }

    public void reset() {
        this.health = 100;
        this.destroyed = false;
        this.explosionTimerMs = 0;
    }
}
