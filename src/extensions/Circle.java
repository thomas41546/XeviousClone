package extensions;

import java.awt.Point;
import java.awt.Rectangle;

public class Circle extends Point {

	private static final long serialVersionUID = 1L;
	private int radius;

	private int x;
	private int y;

	public Circle() {
		super();
		this.radius = 1;
	}

	public Circle(int cx, int cy, int r) {
		super(cx, cy);
		this.radius = r;
	}

	public Circle(Circle circ) {
		super(circ.x, circ.y);
		this.radius = circ.radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return this.radius;
	}

	public double getCX() {
		return this.getX() + this.getRadius();
	}

	public double getCY() {
		return this.getY() + this.getRadius();
	}

	public Vector2D getCenter() {
		return new Vector2D(this.getX(), this.getY()).add(this.getRadius(), this.getRadius());
	}

	public Vector2D getPosition() {
		return new Vector2D(this.getX(), this.getY());
	}

	public void setLocation(Vector2D pos) {
		this.setLocation(pos.x, pos.y);
	}

	public boolean collidesWith(Rectangle rect) {
		Vector2D circleDistance = new Vector2D(
				Math.abs(this.getCenter().x - rect.getX() - rect.getWidth() / 2),
				Math.abs(this.getCenter().y - rect.getY() - rect.getHeight() / 2)
				);
		if ((circleDistance.x > (rect.getWidth() / 2 + this.getRadius())) ||
				(circleDistance.y > (rect.getHeight() / 2 + this.getRadius())))
			return false;

		if ((circleDistance.x <= (rect.getWidth() / 2)) ||
				(circleDistance.y <= (rect.getHeight() / 2)))
			return true;

		return (Math.sqrt(
				Math.pow((circleDistance.x - rect.width / 2), 2) +
				Math.pow((circleDistance.y - rect.height / 2), 2)
				) <= this.getRadius());
	}

	public boolean collidesWith(Circle circle) {
		return (this.getCenter().distance(circle.getCenter()) < this.getRadius()
				+ circle.getRadius());
	}
}
