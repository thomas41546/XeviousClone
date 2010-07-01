package entity;

import images.Animation;
import images.Sprite;

import java.awt.Dimension;
import java.awt.Rectangle;

import singletons.Debug;
import singletons.Map;
import extensions.Circle;
import extensions.Vector2D;

public abstract class Entity {

	public enum Owner {
		ENEMY, PLAYER
	};

	public enum Domain {
		AIR, GROUND
	};

	public enum Shape {
		RECTANGLE, CIRCLE
	};

	protected final Animation animation;
	protected final Domain domain; // air,ground
	protected final Owner owner; // player,enemy
	protected final Shape shape; // rectangle,circle

	protected final int width;
	protected final int height;
	protected final int radius;

	protected Vector2D position;
	protected Vector2D velocity;

	protected boolean collisions;

	public Entity(Vector2D position, Vector2D velocity, Animation animation,
			Owner owner, Domain domain, Shape shape) {

		if ((owner != Entity.Owner.ENEMY) && (owner != Entity.Owner.PLAYER))
			Debug.error("Entity", "Invalid Owner");

		if ((domain != Entity.Domain.AIR) && (domain != Entity.Domain.GROUND))
			Debug.error("Entity", "Invalid Domain");

		if ((shape != Entity.Shape.RECTANGLE) && (shape != Entity.Shape.CIRCLE))
			Debug.error("Entity", "Invalid Shape");

		if ((shape == Entity.Shape.CIRCLE) && (animation.getWidth() != animation.getHeight()))
			Debug.error("Entity", "Shape Circle Requires Width=Height");

		this.collisions = true;
		this.owner = owner;
		this.domain = domain;
		this.shape = shape;
		this.animation = animation;
		this.width = this.animation.getWidth();
		this.height = this.animation.getHeight();
		this.radius = (this.width + this.height) / 4;
		this.setPositionRef(new Vector2D(position));
		this.setVelocity(new Vector2D(velocity));
	}

	public void setCollisionEnabled(boolean collisonsEnabled) {
		this.collisions = collisonsEnabled;
	}

	public boolean getCollisionEnabled() {
		return this.collisions;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public Owner getOwner() {
		return this.owner;
	}

	public Vector2D getSize() {
		return new Vector2D(this.width, this.height);
	}

	public Dimension getSizeDim() {
		return new Dimension(this.width, this.height);
	}

	// sets position according to the proper frame of reference.
	public void setPositionRef(Vector2D position) {
		if (this.getDomain() == Entity.Domain.GROUND)
			position = Map.frameRefScreenToMap(position);
		this.position = position;
	}

	public Vector2D getPosition() {
		return new Vector2D(this.position);
	}

	public Vector2D getCenter() {
		return (new Vector2D(this.position)).add(this.width / 2, this.height / 2);
	}

	public abstract void iteratePosition();

	public double getX() {
		return this.position.x;
	}

	public double getY() {
		return this.position.y;
	}

	public double getHeight() {
		if (this.shape != Entity.Shape.RECTANGLE)
			Debug.error("Entity:getHeight()", "Awkward Call");

		return this.height;
	}

	public double getWidth() {
		if (this.shape != Entity.Shape.RECTANGLE)
			Debug.error("Entity:getWidth()", "Awkward Call");

		return this.width;
	}

	public double getRoundedRadius() {

		return this.radius;
	}

	public double getRadius() {
		if (this.shape != Entity.Shape.CIRCLE)
			Debug.error("Entity:getRadius()", "Awkward Call");
		return this.radius;
	}

	public Rectangle getRect() {
		if (this.shape != Entity.Shape.RECTANGLE)
			Debug.error("Entity:getRect()", "Awkward Call");

		return new Rectangle(
				(int) this.getX(), (int) this.getY(),
				(int) this.getWidth(), (int) this.getHeight());
	}

	public Circle getCircle() {
		if (this.shape != Entity.Shape.CIRCLE)
			Debug.error("Entity:getCircle()", "Awkward Call");

		return new Circle(
				(int) (this.getX()),
				(int) (this.getY()),
				(int) this.getRadius());
	}

	//TODO TEST ALL COLLISION WITH CIRCLES
	public boolean collidesWith(Entity m) {
		if (!this.collisions)
			return false;

		if ((this.getShape() == Entity.Shape.RECTANGLE) && (m.getShape() == Entity.Shape.RECTANGLE))
			return this.getRect().intersects(m.getRect());

		if ((this.getShape() == Entity.Shape.RECTANGLE) && (m.getShape() == Entity.Shape.CIRCLE))
			return m.getCircle().collidesWith(this.getRect());

		if ((this.getShape() == Entity.Shape.CIRCLE) && (m.getShape() == Entity.Shape.RECTANGLE))
			return this.getCircle().collidesWith(m.getRect());

		if ((this.getShape() == Entity.Shape.CIRCLE) && (m.getShape() == Entity.Shape.CIRCLE))
			return this.getCircle().collidesWith(m.getCircle());

		Debug.error("Entity:collidesWith", "fatal case");
		return false;
	}

	public Sprite getSprite() {
		return this.animation.getSprite();
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public Vector2D getVelocity() {
		return this.velocity;
	}

	public Shape getShape() {
		return this.shape;
	}

	public Entity getEntity() {
		return this;
	}

}
