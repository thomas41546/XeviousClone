package enemy;

import images.Animation;
import singletons.Debug;
import singletons.Phase;
import entity.Entity;
import extensions.Vector2D;

public class Enemy extends Entity {
	public enum EnemyType {
		GREENSHIP, BLACKSHIP, BLUEGROUND, BOSS
	};

	private EnemyAI AI;
	private EnemyType enemyType;

	public Enemy(EnemyAI AI, Animation animation, Vector2D position, Owner owner, Domain domain,
			Shape shape, EnemyType enemytype) {
		super(position, new Vector2D(0, 0), animation, owner, domain, shape);
		this.setEnemyType(enemytype);
		this.setAI(AI);
	}

	public Enemy(Enemy enemy) {
		super(enemy.position, new Vector2D(0, 0), enemy.animation, enemy.owner, enemy.domain,
				enemy.shape);
		this.setEnemyType(enemy.getEnemyType());
		this.setAI(new EnemyAI(enemy.AI));
	}

	public void iterateAI() {
		if (this.getAI() != null) {
			this.getAI().caclulateVelocity();
			this.setVelocity(this.getAI().pollVelocity());
			if ((Phase.Instance().getState() != Phase.State.DEATH))
				this.getAI().shoot();
		}
	}

	public void setAI(EnemyAI ai) {
		this.AI = ai;
		if (this.getAI() != null) {
			this.getAI().registerParent(this);
			this.getAI().loadPath();
		}
	}

	private EnemyAI getAI() {
		return this.AI;
	}

	public void refreshAI() {
		if (this.getAI() != null)
			this.getAI().loadPath();
	}

	@Override
	public void setPositionRef(Vector2D position) {
		this.refreshAI();
		super.setPositionRef(position);
	}

	@Override
	public void iteratePosition() {
		this.position.add(this.velocity);

	}

	private void setEnemyType(EnemyType enemyType) {
		this.enemyType = enemyType;
	}

	public EnemyType getEnemyType() {
		return this.enemyType;
	}

	public int getEnemyValue() {
		if (this.getEnemyType() == Enemy.EnemyType.BLACKSHIP)
			return 20;
		if (this.getEnemyType() == Enemy.EnemyType.GREENSHIP)
			return 10;
		if (this.getEnemyType() == Enemy.EnemyType.BLUEGROUND)
			return 20;
		if (this.getEnemyType() == Enemy.EnemyType.BOSS)
			return 500;

		Debug.error("getEnemyValue", "additional case");
		return 0;
	}
}
