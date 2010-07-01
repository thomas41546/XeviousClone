package enemy;

import images.Animation;
import images.AnimationBox;

import java.util.Random;

import player.Ship;
import singletons.Map;
import singletons.Screen;
import bullet.Bullet;
import bullet.BulletController;
import entity.Entity;
import extensions.Vector2D;

public class EnemyAI {
	private Enemy parent;

	/*--------------------Special Paths------------------------*/
	public static final double RANDOMC = 666.666;
	public static final Vector2D SHIPPOS = new Vector2D(666, 666);
	/*--------------------Special Paths------------------------*/

	/*--------------------Path Making------------------------*/
	private EnemyAIPathContainer[] paths;
	private boolean finished = false;
	private int pathIterator = 0;

	/* Lines */
	private Vector2D setVelocity = new Vector2D(0, 0);
	private Vector2D endTarget = new Vector2D(0, 0);

	/* Circles */
	private Vector2D center = new Vector2D(0, 0);
	private double radius = 0;
	private double endAngle = 0;
	private double setAngle = 0;
	private double curAngle = 0;

	/* Other */
	private double maxVelocity = 1;

	/*Constants*/
	private static final int minRadius = 40;

	/*--------------------Path Making------------------------*/

	/*--------------------Shooting------------------------*/
	private EnemyAIAttackContainer attackContainer;

	/*--------------------Shooting------------------------*/

	public EnemyAI(double maxVelocity, EnemyAIPathContainer[] paths,
			EnemyAIAttackContainer attackContainer) {
		this.maxVelocity = maxVelocity;
		this.finished = false;
		this.pathIterator = 0;
		this.paths = paths;
		this.attackContainer = attackContainer;
	}

	public EnemyAI(EnemyAI aI) {
		this.maxVelocity = aI.maxVelocity;
		this.finished = false;
		this.pathIterator = 0;
		this.paths = aI.paths;
		this.attackContainer = new EnemyAIAttackContainer(aI.attackContainer);
	}

	public void registerParent(Enemy enemy) {
		this.parent = enemy;
	}

	/*--------------------Shooting------------------------*/

	public Vector2D calcShotVelocity(Vector2D playerPos, Vector2D playerVel,
			Vector2D enemyPos,
			double timesteps) {
		return ((playerPos.sub(enemyPos)).mul(1.0 / timesteps)).add(playerVel);

	}

	public void shoot() {

		if (this.attackContainer.shoot()) {
			Vector2D initialBulletPos = this.parent.getPosition();
			if (this.parent.getDomain() == Entity.Domain.GROUND)
				initialBulletPos = Map.frameRefMapToScreen(initialBulletPos);
			initialBulletPos.add(this.parent.getSize().mul(0.5));
			initialBulletPos.sub(AnimationBox.stdBullet.getWidth() / 2, 0);

			Vector2D bulletVelocity = new Vector2D(0, 0);

			if ((this.attackContainer.getShotType() == EnemyAIAttackContainer.PREDICT_PATH))
				for (int i = 0; i < 10; i++) {
					bulletVelocity = this.calcShotVelocity(
							Ship.Instance().getPosition().add(Ship.Instance().getSize().mul(0.5)),
							Ship.Instance().getVelocity(),
							initialBulletPos, 10 + i * 10);
					if (this.attackContainer.getBulletSpeed() >= bulletVelocity.length())
						break;
				}
			else if (this.attackContainer.getShotType() == EnemyAIAttackContainer.STRAIGHT_PATH)
				bulletVelocity.add(0, this.attackContainer.getBulletSpeed());

			Bullet bullet = new Bullet(
					initialBulletPos,
					bulletVelocity,
					new Animation(AnimationBox.stdBullet),
					Entity.Owner.ENEMY,
					this.attackContainer.getBulletDomain(), Entity.Shape.RECTANGLE);
			if (BulletController.Instance().InsideScreen(bullet)) {
				BulletController.Instance().add(bullet);
				this.attackContainer.useBullet();
			}
		}
	}

	/*--------------------Shooting------------------------*/

	/*--------------------Path Making------------------------*/
	public void loadPath() {
		Random random = new Random(System.currentTimeMillis());
		if ((this.paths != null) && (this.pathIterator < this.paths.length)) {
			this.finished = false;
			if (this.paths[this.pathIterator].getType() == EnemyAIPathContainer.PathType.LINE) {
				Vector2D location = this.parent.getPosition();
				Vector2D target = this.paths[this.pathIterator].getTarget();

				if (target.equals(EnemyAI.SHIPPOS))
					target = Ship.Instance().getPosition();

				if (target.x == EnemyAI.RANDOMC)
					target.x = random.nextDouble() % Screen.getWidth();
				if (target.y == EnemyAI.RANDOMC)
					target.y = random.nextDouble() % Screen.getHeight();

				double dy = (target.y - location.y);
				double dx = (target.x - location.x);
				double m = Math.abs(dy / ((dx != 0) ? dx : dx + 0.001));

				Vector2D increment = new Vector2D(0, 0);
				if (m > 1) {
					if (dx > 0)
						increment.x = this.maxVelocity / m;
					else
						increment.x = -(this.maxVelocity / m);
					if (dy > 0)
						increment.y = this.maxVelocity;
					else
						increment.y = -this.maxVelocity;
				}
				else {
					if (dx > 0)
						increment.x = this.maxVelocity;
					else
						increment.x = -this.maxVelocity;
					if (dy > 0)
						increment.y = this.maxVelocity * m;
					else
						increment.y = -(this.maxVelocity * m);
				}
				this.setVelocity = increment;
				this.endTarget = target;

			}
			else { // CIRCL3 Path
				Vector2D start = this.parent.getPosition();
				Vector2D center = this.paths[this.pathIterator].getCenter();

				if (center.equals(EnemyAI.SHIPPOS))
					center = Ship.Instance().getPosition();
				if (center.x == EnemyAI.RANDOMC)
					center.x = random.nextDouble() % Screen.getWidth();
				if (center.y == EnemyAI.RANDOMC)
					center.y = random.nextDouble() % Screen.getHeight();

				double startAngle = start.angle(center) - Math.PI;

				this.radius =
						(start.distance(center) > EnemyAI.minRadius) ?
						start.distance(center) : EnemyAI.minRadius;
				this.center = center;
				this.curAngle = startAngle;
				this.endAngle = startAngle + this.paths[this.pathIterator].getRotation();
				this.setAngle = Math.acos(
						(this.maxVelocity * this.maxVelocity - 2 * this.radius * this.radius) /
						(-2 * this.radius * this.radius));
				this.setVelocity = new Vector2D(0, 0);
			}
		}
		else {
			this.finished = true;
			this.setVelocity = new Vector2D(0, 0);
		}
	}

	public void caclulateVelocity() { // adjusts this.setVelocity
		if (this.finished || (this.paths == null))
			return;

		if (this.paths[this.pathIterator].getType() == EnemyAIPathContainer.PathType.CIRCLE) {
			this.curAngle += this.setAngle;

			this.setVelocity.x =
					Math.cos(this.curAngle) *
					this.radius +
					this.center.x -
					this.parent.getPosition().x;

			this.setVelocity.y =
					Math.sin(this.curAngle) *
					this.radius +
					this.center.y -
					this.parent.getPosition().y;
		}

	}

	public Vector2D pollVelocity() {
		if (this.finished || (this.paths == null))
			return new Vector2D(0, 0);

		if (this.paths[this.pathIterator].getType() == EnemyAIPathContainer.PathType.LINE) {
			Vector2D diff = new Vector2D(this.parent.getPosition()).sub(this.endTarget);
			if ((Math.abs(diff.x) > this.maxVelocity * 2)
					|| (Math.abs(diff.y) > this.maxVelocity * 2))
				return this.setVelocity;
			else {
				++this.pathIterator;
				this.loadPath();
			}
		}
		else if (this.paths[this.pathIterator].getType() == EnemyAIPathContainer.PathType.CIRCLE)
			if ((Math.abs(this.endAngle - this.curAngle) > Math.PI / 16))
				return this.setVelocity;
			else {
				++this.pathIterator;
				this.loadPath();
			}
		return new Vector2D(0, 0);
	}
	/*--------------------Path Making------------------------*/

}
