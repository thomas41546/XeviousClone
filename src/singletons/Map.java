package singletons;

import images.Sprite;
import images.SpriteStore;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.text.DecimalFormat;
import java.util.ArrayList;

import enemy.Enemy;
import enemy.EnemyBox;
import enemy.EnemyController;
import entity.Entity;
import extensions.Vector2D;

public class Map {
	private static Map instance;
	private Sprite sprite;
	private int VerticalOffset;
	private int activeStage;

	private ArrayList<Enemy> enemyPool;

	private Map() {
		this.enemyPool = new ArrayList<Enemy>();

	}

	public static Map Instance() {
		if (Map.instance == null)
			Map.instance = new Map();
		return Map.instance;
	}

	private boolean enemySpawnTime(Enemy enemy, int referencePoint) {
		return (((enemy.getShape() == Entity.Shape.CIRCLE) && (referencePoint < enemy.getY()
				+ enemy.getRadius() * 2)) || ((enemy.getShape() == Entity.Shape.RECTANGLE) && (referencePoint < enemy
				.getY()
				+ enemy.getHeight())));
	}

	public void loadStage(int stage, int startVerticalOffset) {
		Enemy dummy;

		this.setVerticalOffset(startVerticalOffset);

		this.sprite = SpriteStore.Instance()
				.getSprite(String.format("maps/%s.png", new DecimalFormat("00").format(stage)));

		this.enemyPool.clear();

		switch (stage) {

			case (1):
				dummy = new Enemy(EnemyBox.greenShipStraight);
				dummy.setPositionRef(new Vector2D(200, -200));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.greenShipStraight);
				dummy.setPositionRef(new Vector2D(500, -200));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.greenShipAround);
				dummy.setPositionRef(new Vector2D(300, -400));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.blueTechGroundShooter);
				dummy.setPositionRef(new Vector2D(275, -320));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.blackShipStraight);
				dummy.setPositionRef(new Vector2D(400, -600));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.blackShipStraight);
				dummy.setPositionRef(new Vector2D(300, -650));
				this.enemyPool.add(dummy);

				dummy = new Enemy(EnemyBox.blackShipStraight);
				dummy.setPositionRef(new Vector2D(500, -750));
				this.enemyPool.add(dummy);

				//BOSS, spawn at least 1 screen away from any other enemies
				dummy = new Enemy(EnemyBox.sampleBoss);
				dummy.setPositionRef(new Vector2D(275, -1050));
				this.enemyPool.add(dummy);
				break;

			case (2):
				//BOSS, spawn at least 1 screen away from any other enemies
				dummy = new Enemy(EnemyBox.sampleBoss);
				dummy.setPositionRef(new Vector2D(275, -400));
				this.enemyPool.add(dummy);
				break;
		}
		this.activeStage = stage;
		this.VerticalOffset = 0;

		ArrayList<Enemy> EnemysToRemoveFromPool = new ArrayList<Enemy>();
		for (Enemy enemy : this.enemyPool)
			if (this.enemySpawnTime(enemy, startVerticalOffset))
				EnemysToRemoveFromPool.add(enemy);
		for (Enemy enemy : EnemysToRemoveFromPool)
			this.enemyPool.remove(enemy);
	}

	public void spawnEnemiesFromPool() {
		ArrayList<Enemy> EnemysToRemoveFromPool = new ArrayList<Enemy>();
		for (Enemy enemy : this.enemyPool)
			if (this.enemySpawnTime(enemy, this.getVerticalOffset())) {
				enemy.setPositionRef(new Vector2D(enemy.getX(), enemy.getY()
						- this.VerticalOffset));
				enemy.refreshAI();

				if (enemy.getEnemyType() == Enemy.EnemyType.BOSS)
					Phase.Instance().setState(Phase.State.BOSS);

				EnemyController.Instance().add(enemy);
				EnemysToRemoveFromPool.add(enemy);
			}

		for (Enemy enemy : EnemysToRemoveFromPool)
			this.enemyPool.remove(enemy);
	}

	public void iterateVerticalOffset() {
		this.setVerticalOffset(this.getVerticalOffset() - 1);
	}

	public void drawMap(Graphics g, Dimension dim, ImageObserver io) {
		int yOffset = (int) ((
				(Screen.getWidth() / this.sprite.getWidth()) * this.sprite.getHeight())
				- (int) Screen.getHeight() + this.VerticalOffset);

		if (yOffset < 0) {
			yOffset = 0;
			if (Phase.Instance().getState() != Phase.State.LEVELCHANGE)
				Phase.Instance().setState(Phase.State.LEVELCHANGE);
		}

		g.clearRect(0, 0, dim.width, dim.height);
		g.drawImage(this.sprite.getImage(), 0,
				-yOffset,
				(int) Screen.getWidth(),
				(int) ((Screen.getWidth() / this.sprite.getWidth())
				* this.sprite.getHeight()),
				io);
	}

	private void setVerticalOffset(int verticalOffset) {
		if (verticalOffset < 0)
			this.VerticalOffset = verticalOffset;
		else
			this.VerticalOffset = 0;
	}

	public int getVerticalOffset() {
		return this.VerticalOffset;
	}

	public static Vector2D frameRefScreenToMap(Vector2D screenpos) {
		Vector2D mappos = new Vector2D(screenpos);
		mappos.y += Map.Instance().getVerticalOffset();
		return mappos;
	}

	public static Vector2D frameRefMapToScreen(Vector2D mappos) {
		Vector2D screenpos = new Vector2D(mappos);
		screenpos.y -= Map.Instance().getVerticalOffset();
		return screenpos;
	}

	public static double frameRefMapToScreen(double y) {
		return y - Map.Instance().getVerticalOffset();
	}

	public int getActiveStage() {
		return this.activeStage;
	}

}
