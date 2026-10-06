package modelo;

public class DirectMovementStrategy implements IMovementStrategy {
	private final double speed;

	public DirectMovementStrategy(double speed) {
		if (speed <= 0) {
			throw new IllegalArgumentException("La velocidad debe ser mayor que cero");
		}
		this.speed = speed;
	}

	@Override
	public void move(GameObjectTarget object, long deltaMs) {
		double dx = object.targetX - object.x;
		double dy = object.targetY - object.y;
		double distance = Math.hypot(dx, dy);
		if (distance == 0) {
			return;
		}

		double step = speed * deltaMs / 1000.0;
		if (step >= distance) {
			object.x = object.targetX;
			object.y = object.targetY;
			return;
		}

		object.x += dx / distance * step;
		object.y += dy / distance * step;
	}
}
