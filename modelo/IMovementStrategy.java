package modelo;

public interface IMovementStrategy {
	void move(GameObjectTarget object, long deltaMs);
}
