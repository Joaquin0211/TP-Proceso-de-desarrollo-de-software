package modelo;

import interfaces.IDamageable;

public class CityDamageAdapter implements IDamageable {
    private final City city;

    public CityDamageAdapter(City city) {
        this.city = city;
    }

    @Override
    public void receiveDamage(int amount) {
        city.takeDamage(amount);
    }
}
