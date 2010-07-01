package player;

import images.Animation;
import images.AnimationBox;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Date;

import singletons.Screen;
import singletons.SoundMaster;
import bullet.Bullet;
import bullet.BulletController;
import entity.Entity;
import extensions.Vector2D;

public class Ship extends Entity {
	private static Ship instance;

	private int lives;
	private int score;
	private int lastLevelScore;

	private long timeLastAirShot;
	public int bombInitialY;
	public Bullet bombBullet;

	public static final long AIR_SHOT_DELAY = 90;// ms
	public static final double MAX_VELOCITY = 6;
	public static final double VELOCITY_INCREMENT = 1.6;
	public static final double FRICTION_COE = 0.90;
	public static final int AIR_BULLET_SPEED = 20;
	public static final int GROUND_BULLET_SPEED = 4;
	public static final int GROUND_BULLET_DISTANCE = 140;
	public static final Dimension BULLET_SIZE = new Dimension(14, 26);

	private Ship() {
		super(new Vector2D(0, 0),
				new Vector2D(0, 0),
				new Animation(AnimationBox.playerShip),
				Entity.Owner.PLAYER,
				Entity.Domain.AIR,
				Entity.Shape.RECTANGLE);

		this.setLives(3);
		this.resetScore();
		this.resetLogic();
	}

	public void resetLogic() {
		this.timeLastAirShot = (new Date()).getTime();
		this.bombInitialY = 0;
		this.bombBullet = null;

	}

	public static Ship Instance() {
		if (Ship.instance == null)
			Ship.instance = new Ship();
		return Ship.instance;
	}

	public void doLogic() {
		if (this.bombBullet != null)
			if ((this.bombInitialY - this.bombBullet.getPosition().y > Ship.GROUND_BULLET_DISTANCE)
					|| !BulletController.Instance().getBulletArray().contains(this.bombBullet)) {
				BulletController.Instance().remove(this.bombBullet);
				this.bombBullet = null;
				this.bombInitialY = 0;
			}
			else if (!this.bombBullet.getCollisionEnabled()
					&& (this.bombInitialY - this.bombBullet.getPosition().y > Ship.GROUND_BULLET_DISTANCE - 5))
				this.bombBullet.setCollisionEnabled(true);

	}

	public void shootGround() {
		if ((this.bombBullet == null)) {
			this.bombBullet = new Bullet(
					new Vector2D(this.getX() + 20, this.getY() - 15),
					new Vector2D(0, -Ship.GROUND_BULLET_SPEED),
					new Animation(AnimationBox.bulletRed),
					Entity.Owner.PLAYER,
					Entity.Domain.GROUND, Entity.Shape.RECTANGLE);
			this.bombBullet.setCollisionEnabled(false);
			BulletController.Instance().add(this.bombBullet);
			this.bombInitialY = (int) this.bombBullet.getY();
		}
	}

	public void shootAir() {
		if ((new Date()).getTime() - this.timeLastAirShot > Ship.AIR_SHOT_DELAY) {
			BulletController.Instance().add(new Bullet(
					new Vector2D(this.getX(), this.getY() - 15),
					new Vector2D(0, -Ship.AIR_BULLET_SPEED),
					new Animation(AnimationBox.stdBullet),
					Entity.Owner.PLAYER,
					Entity.Domain.AIR, Entity.Shape.RECTANGLE));
			BulletController.Instance().add(new Bullet(
					new Vector2D(this.getX() + 40, this.getY() - 15),
					new Vector2D(0, -Ship.AIR_BULLET_SPEED),
					new Animation(AnimationBox.stdBullet),
					Entity.Owner.PLAYER,
					Entity.Domain.AIR, Entity.Shape.RECTANGLE));

			this.timeLastAirShot = (new Date()).getTime();

			SoundMaster.Instance().play(SoundMaster.Sound.SHOOT);
		}
	}

	public void moveUp() {
		this.setVelocity(
				new Vector2D(
				this.getVelocity().x,
				Math.max(this.getVelocity().y - Ship.VELOCITY_INCREMENT, -Ship.MAX_VELOCITY)));
	}

	public void moveDown() {
		this.setVelocity(
				new Vector2D(
				this.getVelocity().x,
				Math.min(this.getVelocity().y + Ship.VELOCITY_INCREMENT, Ship.MAX_VELOCITY)));
	}

	public void moveLeft() {
		this.setVelocity(
				new Vector2D(
				Math.max(this.getVelocity().x - Ship.VELOCITY_INCREMENT, -Ship.MAX_VELOCITY),
				this.getVelocity().y));
	}

	public void moveRight() {
		this.setVelocity(
				new Vector2D(
				Math.min(this.getVelocity().x + Ship.VELOCITY_INCREMENT, Ship.MAX_VELOCITY),
				this.getVelocity().y));
	}

	@Override
	public void iteratePosition() {
		this.setVelocity(this.getVelocity().mul(Ship.FRICTION_COE));

		Rectangle futureRect = new Rectangle((int) this.getPosition().add(this.getVelocity()).x,
				(int) this.getPosition().add(this.getVelocity()).y,
				(int) this.getWidth(), (int) this.getHeight());

		if ((futureRect.x < 0) || (futureRect.x > Screen.getWidth() - this.getWidth()))
			this.setVelocity(new Vector2D(0, this.getVelocity().y));
		if ((futureRect.y < 0) || (futureRect.y > Screen.getHeight() - this.getHeight()))
			this.setVelocity(new Vector2D(this.getVelocity().x, 0));

		if (this.getVelocity().abs() > 1.0)
			this.position.add(this.velocity);
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {
		return this.lives;
	}

	public void die() {
		if ((this.bombBullet != null)
				&& BulletController.Instance().getBulletArray().contains(this.bombBullet))
			BulletController.Instance().remove(this.bombBullet);
		this.resetLogic();
		this.lives--;
	}

	public Animation getTargetSprite() {
		if (this.bombBullet != null)
			return new Animation(AnimationBox.targetRed);
		else
			return new Animation(AnimationBox.target);
	}

	public void resetScore() {
		this.score = 0;
		this.setLastLevelScore(0);
	}

	public void incrementScore(int scoreToIncrement) {
		this.score += scoreToIncrement;
	}

	public int getScore() {
		return this.score;
	}

	public void setLastLevelScore(int lastLevelScore) {
		this.lastLevelScore = lastLevelScore;
	}

	public int getLastLevelScore() {
		return this.lastLevelScore;
	}

}
