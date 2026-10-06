package modelo;

public class LinearMovementStrategy implements IMovementStrategy {
	private final double speed;

	public LinearMovementStrategy(double speed) {
		if (speed <= 0) {
			throw new IllegalArgumentException("La velocidad debe ser mayor que cero");
		}
		this.speed = speed;
	}

	@Override
	public void move(GameObjectTarget object, long deltaMs) {
		double distance = Math.hypot(object.targetX - object.startX, object.targetY - object.startY);
		if (distance == 0) {
			return;
		}

		double deltaSec = deltaMs / 1000.0;
		object.x += (object.targetX - object.startX) / distance * speed * deltaSec;
		object.y += (object.targetY - object.startY) / distance * speed * deltaSec;
	}
}
