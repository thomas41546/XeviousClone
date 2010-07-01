package enemy;

import images.Animation;
import images.AnimationBox;
import entity.Entity;
import extensions.Vector2D;

public class EnemyBox {
	private static EnemyBox instance = new EnemyBox();

	public static Enemy greenShipStraight;
	public static Enemy greenShipAround;

	public static Enemy blackShipStraight;

	public static Enemy blueTechGroundShooter;

	public static Enemy sampleBoss; //TODO remove sampleBoss

	public static EnemyBox Instance() {
		return EnemyBox.instance;
	}

	public void init() {
		EnemyAIAttackContainer attackBossStyleA = new EnemyAIAttackContainer(20, 500, 12,
				Entity.Domain.AIR, EnemyAIAttackContainer.PREDICT_PATH);

		EnemyAIAttackContainer attackAirPredictFast = new EnemyAIAttackContainer(3, 400, 11,
				Entity.Domain.AIR, EnemyAIAttackContainer.PREDICT_PATH);

		EnemyAIAttackContainer attackAirPredictNormal = new EnemyAIAttackContainer(4, 500, 10,
				Entity.Domain.AIR, EnemyAIAttackContainer.PREDICT_PATH);

		EnemyAIAttackContainer attackAirPredictSlow = new EnemyAIAttackContainer(5, 1000, 11,
				Entity.Domain.AIR, EnemyAIAttackContainer.PREDICT_PATH);

		EnemyAIPathContainer[] straightDown = new EnemyAIPathContainer[1];
		straightDown[0] = new EnemyAIPathContainer(new Vector2D(500, 10000));

		EnemyAIPathContainer[] circleGoDown = new EnemyAIPathContainer[3];
		circleGoDown[0] = new EnemyAIPathContainer(); //goto player ship position
		circleGoDown[1] = new EnemyAIPathContainer(
				EnemyAI.SHIPPOS,
				Math.PI);
		circleGoDown[2] = new EnemyAIPathContainer(new Vector2D(EnemyAI.RANDOMC, 2000));

		EnemyBox.greenShipStraight = new Enemy(
				new EnemyAI(5, straightDown, new EnemyAIAttackContainer(attackAirPredictNormal)),
				new Animation(AnimationBox.greenShip),
				new Vector2D(0, 0),
				Entity.Owner.ENEMY,
				Entity.Domain.AIR,
				Entity.Shape.RECTANGLE,
				Enemy.EnemyType.GREENSHIP);

		EnemyBox.greenShipAround = new Enemy(
				new EnemyAI(5, circleGoDown, new EnemyAIAttackContainer(attackAirPredictSlow)),
				new Animation(AnimationBox.greenShip),
				new Vector2D(0, 0),
				Entity.Owner.ENEMY,
				Entity.Domain.AIR,
				Entity.Shape.RECTANGLE,
				Enemy.EnemyType.GREENSHIP);

		EnemyBox.blueTechGroundShooter = new Enemy(
				new EnemyAI(0, null, new EnemyAIAttackContainer(attackAirPredictSlow)),
				new Animation(AnimationBox.techCircle),
				new Vector2D(0, 0),
				Entity.Owner.ENEMY,
				Entity.Domain.GROUND,
				Entity.Shape.CIRCLE,
				Enemy.EnemyType.BLUEGROUND);

		EnemyBox.blackShipStraight = new Enemy(
				new EnemyAI(8, straightDown, new EnemyAIAttackContainer(attackAirPredictFast)),
				new Animation(AnimationBox.blackShip),
				new Vector2D(0, 0),
				Entity.Owner.ENEMY,
				Entity.Domain.AIR,
				Entity.Shape.RECTANGLE,
				Enemy.EnemyType.BLACKSHIP);

		EnemyBox.sampleBoss = new Enemy(
				new EnemyAI(0, null, new EnemyAIAttackContainer(attackBossStyleA)),
				new Animation(AnimationBox.techCircle),
				new Vector2D(0, 0),
				Entity.Owner.ENEMY,
				Entity.Domain.GROUND,
				Entity.Shape.CIRCLE,
				Enemy.EnemyType.BOSS);

	}
}
