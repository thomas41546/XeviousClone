package entity;

import images.Animation;
import images.AnimationBox;
import images.AnimationController;
import player.Ship;
import singletons.Map;
import singletons.Phase;
import bullet.Bullet;
import bullet.BulletController;
import enemy.Enemy;
import enemy.EnemyController;
import extensions.Vector2D;

public class CollisionEngine {
	private static CollisionEngine instance;

	public static final int MAX_ENTITIES = 1500;
	public static Entity[][][] quadTreeChildrenGroups =
			new Entity[16][4][CollisionEngine.MAX_ENTITIES];

	private CollisionEngine() {}

	public static CollisionEngine Instance() {
		if (CollisionEngine.instance == null)
			CollisionEngine.instance = new CollisionEngine();
		return CollisionEngine.instance;
	}

	private void collideEntities(Entity A, Entity B) {
		if ((A instanceof Bullet) && (B instanceof Enemy)
				&& (A.getOwner() == Entity.Owner.PLAYER) &&
				(A.getDomain() == B.getDomain())) {

			//Due to the way quadtreeCollisions works the same object may
			//may find its way to this routine more than once.
			//The obvious way to ensure it is only handled once is to
			//remove it the enemy from the enemy controller, and then by checking
			//each time  the enemy is in the controller before performing
			//operations on it.
			if (!EnemyController.Instance().getEnemyArray().contains(B))
				return;

			//2x score for boss if you don't die
			if ((Phase.Instance().getState() == Phase.State.BOSS)
					&& (((Enemy) (B)).getEnemyType() == Enemy.EnemyType.BOSS)) {
				Ship.Instance().incrementScore(((Enemy) B).getEnemyValue());
				Phase.Instance().setState(Phase.State.GAMEPLAY);
			}

			Ship.Instance().incrementScore(((Enemy) B).getEnemyValue());

			Vector2D center = B.getCenter();
			if (B.getDomain() == Entity.Domain.GROUND)
				center = new Vector2D(center.x, Map.frameRefMapToScreen(center.y));

			Animation explosion = new Animation(AnimationBox.explosion);
			explosion.setAnimationSize(B.getSize().mul(1.5));
			explosion.setPosition(center.sub(B.getSize().mul(0.7)));
			AnimationController.Instance().add(explosion);

			EnemyController.Instance().remove((Enemy) B);
			B = null;
			return;
		}
		if ((A instanceof Bullet)
				&& (B instanceof Ship)
				&& (((Phase.Instance().getState() == Phase.State.GAMEPLAY)
				|| (Phase.Instance().getState() == Phase.State.BOSS))
				&& (A.getOwner() == Entity.Owner.ENEMY))
				&& (A.getDomain() == B.getDomain())) {
			if (!BulletController.Instance().getBulletArray().contains(A))
				return;

			Animation explosion = new Animation(AnimationBox.explosion);
			explosion.setPosition(Ship.Instance().getCenter().sub(30, 30));
			AnimationController.Instance().add(explosion);

			Ship.Instance().die();
			Phase.Instance().setState(Phase.State.DEATH);
			BulletController.Instance().remove((Bullet) A);
			A = null;
			return;
		}
	}

	private void quadtreeCollisions(Entity[] objects, int length, int max_depth) {
		if ((length <= 5) || (max_depth == 0)) {
			for (int i = 0; i < length - 1; i++)
				for (int j = 0; j < length; j++)
					if ((i != j) && (objects[i] != null) && (objects[j] != null))
						if (objects[i].collidesWith(objects[j]))//FAILS
							this.collideEntities(objects[i], objects[j]);
		}
		else {
			int quad1count = 0;
			int quad2count = 0;
			int quad3count = 0;
			int quad4count = 0;

			Vector2D pivot = new Vector2D(0.0, 0.0);
			for (int i = 0; i < length; i++)
				pivot.add(objects[i].getPosition());
			pivot.mul(1.0 / length);

			for (int i = 0; i < length; i++) {

				if ((objects[i].getPosition().x - objects[i].getRoundedRadius()) < pivot.x) {
					if ((objects[i].getPosition().y - objects[i].getRoundedRadius()) < pivot.y)
						CollisionEngine.quadTreeChildrenGroups[2 - max_depth][0][quad1count++] = objects[i];
					if (objects[i].getPosition().y + objects[i].getRoundedRadius() > pivot.y)
						CollisionEngine.quadTreeChildrenGroups[2 - max_depth][1][quad2count++] = objects[i];
				}

				if (objects[i].getPosition().x + objects[i].getRoundedRadius() > pivot.x) {
					if ((objects[i].getPosition().y - objects[i].getRoundedRadius()) < pivot.y)
						CollisionEngine.quadTreeChildrenGroups[2 - max_depth][2][quad3count++] = objects[i];
					if (objects[i].getPosition().y + objects[i].getRoundedRadius() > pivot.y)
						CollisionEngine.quadTreeChildrenGroups[2 - max_depth][3][quad4count++] = objects[i];
				}
			}
			this.quadtreeCollisions(
					CollisionEngine.quadTreeChildrenGroups[2 - max_depth][0], quad1count,
					max_depth - 1);
			this.quadtreeCollisions(
					CollisionEngine.quadTreeChildrenGroups[2 - max_depth][1], quad2count,
					max_depth - 1);
			this.quadtreeCollisions(
					CollisionEngine.quadTreeChildrenGroups[2 - max_depth][2], quad3count,
					max_depth - 1);
			this.quadtreeCollisions(
					CollisionEngine.quadTreeChildrenGroups[2 - max_depth][3], quad4count,
					max_depth - 1);
		}
	}

	public void worldCollisions() {
		int entitiesIt = 0;
		Entity[] entities = new Entity[
				BulletController.Instance().getBulletArray().size() +
				EnemyController.Instance().getEnemyArray().size() +
				1];
		for (Bullet bullet : BulletController.Instance().getBulletArray())
			entities[entitiesIt++] = bullet;
		for (Enemy enemy : EnemyController.Instance().getEnemyArray())
			entities[entitiesIt++] = enemy;
		entities[entitiesIt++] = Ship.Instance();
		this.quadtreeCollisions(entities, entitiesIt, 2);
	}

}
