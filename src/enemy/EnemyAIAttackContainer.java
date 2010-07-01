package enemy;

import entity.Entity.Domain;

public class EnemyAIAttackContainer {

	public static final int PREDICT_PATH = 1;
	public static final int STRAIGHT_PATH = 2;

	private double bulletSpeed;

	private int bulletAmmo;
	private Domain bulletDomain;

	private long bulletLastShotTime;
	private int bulletDelay;

	private int shotType;

	public EnemyAIAttackContainer(
			int bulletAmmo, int bulletDelayMS, double bulletSpeed, Domain bulletDomain, int shotType) {

		this.bulletLastShotTime = System.currentTimeMillis() + bulletDelayMS;
		this.bulletAmmo = bulletAmmo;
		this.bulletDelay = bulletDelayMS;
		this.bulletSpeed = bulletSpeed;
		this.bulletDomain = bulletDomain;
		this.shotType = shotType;
	}

	public EnemyAIAttackContainer(EnemyAIAttackContainer clone) {
		this.bulletLastShotTime = System.currentTimeMillis() + clone.bulletDelay;
		this.bulletAmmo = clone.bulletAmmo;
		this.bulletDelay = clone.bulletDelay;
		this.bulletSpeed = clone.bulletSpeed;
		this.bulletDomain = clone.bulletDomain;
		this.shotType = clone.shotType;
	}

	public boolean hasAmmo() {
		return (this.bulletAmmo > 0);
	}

	public void useBullet() {
		--this.bulletAmmo;
	}

	public boolean shoot() {
		if ((System.currentTimeMillis() - this.bulletLastShotTime > this.bulletDelay)
				&& this.hasAmmo()) {
			this.bulletLastShotTime = System.currentTimeMillis();
			return true;
		}
		else
			return false;
	}

	public double getBulletSpeed() {
		return this.bulletSpeed;
	}

	public int getShotType() {
		return this.shotType;
	}

	public Domain getBulletDomain() {
		return this.bulletDomain;
	}

}
