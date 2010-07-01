package singletons;

import images.Animation;
import images.AnimationBox;
import images.AnimationController;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import player.KeyboardInput;
import player.MouseInput;
import player.Ship;
import bullet.Bullet;
import bullet.BulletController;
import enemy.Enemy;
import enemy.EnemyBox;
import enemy.EnemyController;
import entity.CollisionEngine;
import entity.Entity;
import extensions.Circle;
import extensions.Vector2D;

public class Xevious extends Applet implements Runnable {

	private static final long serialVersionUID = -207891751492956378L;
	private static Thread runner;

	private static final long frameDuration = 1000 / 40;

	private Image iBuffer;
	private Graphics gBuffer;

	private long fps = 0;
	private long lasttime = 0;
	private long framecount = 0;
	private long looptime = 0;

	private int flickerEffect = 0;

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(this.iBuffer, 0, 0, this);
	}

	@Override
	public void stop() {
		Xevious.runner = null;
		super.stop();
	}

	@Override
	public void start() {
		if (Xevious.runner == null) {
			Xevious.runner = new Thread(this);
			Xevious.runner.start();
		} else
			super.start();
	}

	@Override
	public void init() {
		this.setBackground(Color.black);
		this.addKeyListener(KeyboardInput.Instance());
		this.addMouseMotionListener(MouseInput.Instance());
		this.addMouseListener(MouseInput.Instance());
		this.setSize(Screen.getDim());
		this.iBuffer = this.createImage(this.getSize().width,
				this.getSize().height);
		this.gBuffer = this.iBuffer.getGraphics();
		this.gBuffer.setColor(new Color(0xFF, 0, 0));
		SoundMaster.Instance().init(this);
		AnimationBox.Instance().init(this);
		EnemyBox.Instance().init();
		Phase.Instance().setState(Phase.State.TITLE);

		Map.Instance().loadStage(1, 0);
		Ship.Instance().setLives(3);
		this.reset();

		super.init();
	}

	private void reset() {
		BulletController.Instance().getBulletArray().clear();
		EnemyController.Instance().getEnemyArray().clear();

		Ship.Instance().setPositionRef(
				new Vector2D(Screen.getWidth() / 2 - Ship.Instance().getWidth()
						/ 2, Screen.getHeight() - Ship.Instance().getHeight()
						- 50));
		Ship.Instance().resetLogic();
	}

	public void drawRectangle(Rectangle rect) {
		this.gBuffer.drawRect(rect.x, rect.y, rect.width, rect.height);
	}

	public void drawBullets() {
		for (Bullet bullet : BulletController.Instance().getBulletArray()) {
			Vector2D bulletpos = bullet.getPosition();

			if (bullet.getDomain() == Entity.Domain.GROUND)
				bulletpos = Map.frameRefMapToScreen(bullet.getPosition());

			this.gBuffer.drawImage(bullet.getSprite().getImage(),
					(int) bulletpos.x, (int) bulletpos.y, this);

			if (Debug.DEBUG)
				if ((bullet.getShape() == Entity.Shape.RECTANGLE)) {
					Rectangle bulletRect = bullet.getRect();
					if (bullet.getDomain() == Entity.Domain.GROUND)
						bulletRect.y = (int) bulletpos.y;
					this.drawRectangle(bulletRect);
				} else if ((bullet.getShape() == Entity.Shape.CIRCLE)) {
					Circle circ = bullet.getCircle();
					if (bullet.getDomain() == Entity.Domain.GROUND)
						circ.setLocation(Map.frameRefMapToScreen(circ
								.getPosition()));
					this.drawCircle(circ);
				}

		}
	}

	public void drawEnemies() {
		for (Enemy enemy : EnemyController.Instance().getEnemyArray()) {
			Vector2D enemyPos = enemy.getPosition();
			if (enemy.getDomain() == Entity.Domain.AIR)
				this.gBuffer.drawImage(enemy.getSprite().getImage(),
						(int) enemyPos.x, (int) enemyPos.y, this);
			else
				this.gBuffer.drawImage(enemy.getSprite().getImage(),
						(int) enemyPos.x, (int) Map
								.frameRefMapToScreen(enemyPos.y), this);

			if (Debug.DEBUG)
				if (enemy.getShape() == Entity.Shape.RECTANGLE) {
					Rectangle enemyRect = enemy.getRect();
					if (enemy.getDomain() == Entity.Domain.GROUND) {
						enemyPos = Map.frameRefMapToScreen(enemy.getPosition());
						enemyRect.y = (int) enemyPos.y;
					}
					this.drawRectangle(enemyRect);
				} else {
					Circle ec = enemy.getCircle();
					if (enemy.getDomain() == Entity.Domain.GROUND)
						ec.setLocation(ec.getX(), Map.frameRefMapToScreen(ec
								.getY()));
					this.drawCircle(ec);
				}
		}
	}

	public void drawAnimations() {
		for (Animation animation : AnimationController.Instance()
				.getAnimationArray())
			if (animation.getAnimationSize() == null)
				this.gBuffer.drawImage(animation.getImage(), (int) animation
						.getPositon().x, (int) animation.getPositon().y, this);
			else
				this.gBuffer.drawImage(animation.getImage(), (int) animation
						.getPositon().x, (int) animation.getPositon().y,
						(int) animation.getAnimationSize().x, (int) animation
								.getAnimationSize().y, this);
	}

	public void drawShip() {
		this.gBuffer.drawImage(Ship.Instance().getSprite().getImage(),
				(int) Ship.Instance().getX(), (int) Ship.Instance().getY(),
				this);
		this.gBuffer.drawImage(Ship.Instance().getTargetSprite().getImage(),
				(int) Ship.Instance().getX() + 10,
				(int) Ship.Instance().getY() - 160, this);

		if (Debug.DEBUG)
			this.drawRectangle(Ship.Instance().getRect());

	}

	private void drawCircle(Circle circle) {
		this.gBuffer.drawOval((int) circle.getX(), (int) circle.getY(), circle
				.getRadius() * 2, circle.getRadius() * 2);
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void updateShipFromKeyboard() {
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_UP))
			Ship.Instance().moveUp();
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_LEFT))
			Ship.Instance().moveLeft();
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_RIGHT))
			Ship.Instance().moveRight();
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_DOWN))
			Ship.Instance().moveDown();
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_Z))
			Ship.Instance().shootAir();
		if (KeyboardInput.Instance().keyDown(KeyEvent.VK_X))
			Ship.Instance().shootGround();
	}

	public void run() {
		while (true) {
			this.sleep(Xevious.frameDuration - this.looptime);
			this.looptime = System.currentTimeMillis();

			/*----Keyboard Input Handling----*/
			KeyboardInput.Instance().poll();
			if ((Phase.Instance().getState() == Phase.State.GAMEPLAY)
					|| (Phase.Instance().getState() == Phase.State.INTRO)
					|| (Phase.Instance().getState() == Phase.State.BOSS))
				this.updateShipFromKeyboard();

			if (KeyboardInput.Instance().keyDown(KeyEvent.VK_D)) {
				Debug.DEBUG = !Debug.DEBUG;
				this.sleep(50);
				KeyboardInput.Instance().flush();
			}
			if (KeyboardInput.Instance().keyDown(KeyEvent.VK_Q)
					|| KeyboardInput.Instance().keyDown(KeyEvent.VK_ESCAPE))
				System.exit(0);

			if (KeyboardInput.Instance().keyDown(KeyEvent.VK_P)) {
				SoundMaster.Instance().stopall();
				Phase.Instance().freezePhaseChangleTimeMillis();
				KeyboardInput.Instance().flush();
				this.sleep(50);
				while (!KeyboardInput.Instance().keyDown(KeyEvent.VK_P)) {
					this.sleep(50);
					KeyboardInput.Instance().poll();
				}
				this.sleep(50);
				KeyboardInput.Instance().flush();
				Phase.Instance().unfreezePhaseChangleTimeMillis();
			}
			/*----Keyboard Input Handling----*/

			switch (Phase.Instance().getState()) {

			case GAMEPLAY:
				Ship.Instance().doLogic();
				Ship.Instance().iteratePosition();
				Map.Instance().spawnEnemiesFromPool();

				BulletController.Instance().iterate();
				EnemyController.Instance().iterate();
				CollisionEngine.Instance().worldCollisions();
				Map.Instance().iterateVerticalOffset();
				Map.Instance().drawMap(this.gBuffer, this.getSize(), this);
				this.drawEnemies();
				this.drawBullets();
				this.drawShip();
				this.drawAnimations();
				AnimationController.Instance().iterate();
				break;

			case BOSS:
				Ship.Instance().doLogic();
				Ship.Instance().iteratePosition();
				Map.Instance().spawnEnemiesFromPool();

				BulletController.Instance().iterate();
				EnemyController.Instance().iterate();
				CollisionEngine.Instance().worldCollisions();
				if ((Phase.Instance().getPhaseChangeElaspedMillis() < 4000)
						|| (Phase.Instance().getPhaseChangeElaspedMillis() > 15 * 1000))
					Map.Instance().iterateVerticalOffset();
				Map.Instance().drawMap(this.gBuffer, this.getSize(), this);
				this.drawEnemies();
				this.drawBullets();
				this.drawShip();
				this.drawAnimations();
				AnimationController.Instance().iterate();
				if ((Phase.Instance().getPhaseChangeElaspedMillis() > 20 * 1000)
						|| (EnemyController.Instance().contains(
								Enemy.EnemyType.BOSS) == 0))
					Phase.Instance().setState(Phase.State.GAMEPLAY);
				break;

			case DEATH: // called from CollisionEngine
				if (Phase.Instance().getPhaseChangeElaspedMillis() < 1500) {
					BulletController.Instance().iterate();
					EnemyController.Instance().iterate();
					CollisionEngine.Instance().worldCollisions();
					Map.Instance().drawMap(this.gBuffer, this.getSize(), this);
					this.drawEnemies();
					this.drawBullets();
					this.drawAnimations();
					AnimationController.Instance().iterate();
				} else if (Ship.Instance().getLives() > 0) {
					Phase.Instance().setState(Phase.State.INTRO);
					Ship.Instance().setPositionRef(
							new Vector2D(Screen.getWidth() / 2
									- Ship.Instance().getWidth() / 2, Screen
									.getHeight()
									- Ship.Instance().getHeight() - 50));
				} else
					Phase.Instance().setState(Phase.State.GAMEOVER);
				break;

			case INTRO:
				if (Phase.Instance().getPhaseChangeElaspedMillis() <= 3000) {
					Ship.Instance().doLogic();
					Ship.Instance().iteratePosition();
					Map.Instance().spawnEnemiesFromPool();

					BulletController.Instance().iterate();
					EnemyController.Instance().iterate();
					CollisionEngine.Instance().worldCollisions();
					Map.Instance().iterateVerticalOffset();
					Map.Instance().drawMap(this.gBuffer, this.getSize(), this);
					this.drawEnemies();
					this.drawBullets();

					if (this.flickerEffect == 0) {
						this.drawShip();
						this.flickerEffect = 2;
					} else
						this.flickerEffect -= 1;

					this.drawAnimations();
					AnimationController.Instance().iterate();
				} else
					Phase.Instance().setState(Phase.State.GAMEPLAY);
				break;

			case GAMEOVER:
				this.gBuffer.clearRect(0, 0, (int) Screen.getWidth(),
						(int) Screen.getHeight());
				this.gBuffer.drawString("Game Over",
						(int) Screen.getWidth() / 2 - 50, (int) Screen
								.getHeight() / 2);
				this.gBuffer.drawString("Press any key to play again",
						(int) Screen.getWidth() / 2 - 90, (int) Screen
								.getHeight() / 2 + 15);
				this.repaint();

				KeyboardInput.Instance().flush();
				this.sleep(50);
				while (!KeyboardInput.Instance().anyKeyDown()) {
					this.sleep(50);
					KeyboardInput.Instance().poll();
				}
				this.sleep(50);

				KeyboardInput.Instance().flush();
				Map.Instance().loadStage(1, 0);
				Ship.Instance().setVelocity(new Vector2D(0, 0));
				Ship.Instance().setLastLevelScore(0);
				Ship.Instance().resetScore();
				Ship.Instance().setLives(3);
				this.reset();

				Phase.Instance().setState(Phase.State.TITLE);
				break;

			case TITLE:
				this.gBuffer.setColor(new Color(0xFF, 0xFF, 0xFF));
				this.gBuffer.clearRect(0, 0, (int) Screen.getWidth(),
						(int) Screen.getHeight());
				this.gBuffer.drawString("Introduction",
						(int) Screen.getWidth() / 2 - 50, (int) Screen
								.getHeight() / 2);
				this.repaint();
				this.gBuffer.setColor(new Color(0xFF, 0, 0));

				KeyboardInput.Instance().flush();
				this.sleep(50);
				while (!KeyboardInput.Instance().anyKeyDown()) {
					this.sleep(50);
					KeyboardInput.Instance().poll();
				}
				this.sleep(50);
				KeyboardInput.Instance().flush();
				Phase.Instance().setState(Phase.State.INTRO);
				break;

			case LEVELCHANGE: // called from Map
				Ship.Instance().doLogic();
				Ship.Instance().setPositionRef(
						new Vector2D(Ship.Instance().getX(), Ship.Instance()
								.getY() - 5));
				Ship.Instance().iteratePosition();
				BulletController.Instance().iterate();
				EnemyController.Instance().iterate();
				Map.Instance().drawMap(this.gBuffer, this.getSize(), this);
				this.drawEnemies();
				this.drawBullets();
				this.drawShip();
				this.drawAnimations();
				AnimationController.Instance().iterate();

				int scoreRating = (Ship.Instance().getScore() - Ship.Instance()
						.getLastLevelScore()) / 1000;
				String scoreMsg = new String();
				switch (scoreRating) {
				case (0):
					scoreMsg = "Amature";
					break;
				case (1):
					scoreMsg = "Water Boy";
					break;
				case (2):
					scoreMsg = "Sheeple";
					break;
				case (3):
					scoreMsg = "Ship Chops";
					break;
				case (4):
					scoreMsg = "Intermediate";
					break;
				case (5):
					scoreMsg = "L33T";
					break;
				case (6):
					scoreMsg = "Hax0r";
					break;
				case (7):
					scoreMsg = "Astronaut";
					break;
				default:
					scoreMsg = "Dude";
					break;
				}
				this.gBuffer.drawString("Good Job: " + scoreMsg.toString(),
						(int) (Screen.getWidth() / 2 - 20), 20);

				if (Phase.Instance().getPhaseChangeElaspedMillis() > 5000) {
					Map.Instance().loadStage(
							Map.Instance().getActiveStage() + 1, 0);
					Ship.Instance().setVelocity(new Vector2D(0, 0));
					Ship.Instance().setLastLevelScore(
							Ship.Instance().getScore());
					this.reset();
					Phase.Instance().setState(Phase.State.INTRO);
				}
				break;

			}

			if (Debug.DEBUG) {
				int yincrement = 15;
				this.gBuffer.drawString("Fps: " + String.valueOf(this.fps), 5,
						yincrement);
				yincrement += 15;
				this.gBuffer.drawString("Bullets: "
						+ String.valueOf(BulletController.Instance()
								.getBulletArray().size()), 5, yincrement);
				yincrement += 15;
				this.gBuffer.drawString("Enemies: "
						+ String.valueOf(EnemyController.Instance()
								.getEnemyArray().size()), 5, yincrement);
				yincrement += 15;
				this.gBuffer.drawString("Entities: "
						+ String.valueOf(EnemyController.Instance()
								.getEnemyArray().size()
								+ BulletController.Instance().getBulletArray()
										.size() + 1), 5, yincrement);
				yincrement += 15;
				this.gBuffer.drawString("MapY: "
						+ String.valueOf(Map.Instance().getVerticalOffset()),
						5, yincrement);
				yincrement += 15;
				this.gBuffer.drawString("Score: "
						+ String.valueOf(Ship.Instance().getScore()), 5,
						yincrement);
				yincrement += 15;
				this.gBuffer.drawString("Lives: "
						+ String.valueOf(Ship.Instance().getLives()), 5,
						yincrement);

				yincrement += 15;
				this.gBuffer.drawString("MSPosition: "
						+ String.valueOf(MouseInput.Instance().getPosition().x)
						+ ", "
						+ String.valueOf(MouseInput.Instance().getPosition().y
								+ Map.Instance().getVerticalOffset()), 5,
						yincrement);

				yincrement += 15;
				String phase = "NULL";
				if (Phase.Instance().getState() == Phase.State.TITLE)
					phase = "TITLE";
				if (Phase.Instance().getState() == Phase.State.DEATH)
					phase = "DEATH";
				if (Phase.Instance().getState() == Phase.State.GAMEOVER)
					phase = "GAMEOVER";
				if (Phase.Instance().getState() == Phase.State.GAMEPLAY)
					phase = "GAMEPLAY";
				if (Phase.Instance().getState() == Phase.State.INTRO)
					phase = "INTRO";
				if (Phase.Instance().getState() == Phase.State.BOSS)
					phase = "BOSS";
				if (Phase.Instance().getState() == Phase.State.LEVELCHANGE)
					phase = "LEVELCHANGE";

				this.gBuffer.drawString("Phase: " + phase, 5, yincrement);

				++this.framecount;
				if (System.currentTimeMillis() - this.lasttime > 250.0) {
					this.lasttime = System.currentTimeMillis();
					this.fps = this.framecount * 4;
					this.framecount = 0;
				}

			}
			this.repaint();
			this.looptime = System.currentTimeMillis() - this.looptime;
			if (this.looptime > Xevious.frameDuration)
				this.looptime = Xevious.frameDuration;
		}
	}
}