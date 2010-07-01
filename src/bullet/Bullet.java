package bullet;

import images.Animation;
import entity.Entity;
import extensions.Vector2D;

public class Bullet extends Entity {

	public Bullet(Vector2D position, Vector2D velocity, Animation animation,
			Owner owner, Domain domain, Shape shape) {
		super(position, velocity, animation, owner, domain, shape);
	}

	@Override
	public void iteratePosition() {
		this.position.add(this.velocity);
	}

	@Override
	public void setVelocity(Vector2D velocity) {//TODO set minimum bullet velocity
		super.setVelocity(velocity);
	}
}
