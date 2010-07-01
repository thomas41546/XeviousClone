package enemy;

import java.awt.Rectangle;
import java.util.ArrayList;

import singletons.Map;
import singletons.Screen;
import entity.Entity;
import extensions.Circle;

public class EnemyController {
	private static EnemyController instance;
	private static final ArrayList<Enemy> enemyArray = new ArrayList<Enemy>();

	public static EnemyController Instance() {
		if (EnemyController.instance == null)
			EnemyController.instance = new EnemyController();
		return EnemyController.instance;
	}

	public void add(Enemy enemy) {
		EnemyController.enemyArray.add(enemy);
	}

	public void remove(Enemy enemy) {
		EnemyController.enemyArray.remove(enemy);
	}

	public int contains(Enemy.EnemyType enemyType) {
		int count = 0;
		for (Enemy enemy : EnemyController.enemyArray)
			if (enemy.getEnemyType() == enemyType)
				++count;
		return count;
	}

	public ArrayList<Enemy> getEnemyArray() {
		return EnemyController.enemyArray;
	}

	public boolean InsideScreen(Enemy enemy) {
		if (enemy.getShape() == Entity.Shape.RECTANGLE) {
			Rectangle erect = enemy.getRect();
			if (enemy.getDomain() == Entity.Domain.GROUND)
				erect.setLocation(erect.x, (int) Map.frameRefMapToScreen(erect.y));
			return Screen.getRect().intersects(erect);
		}
		else { //CIRCLE
			Circle ecircle = enemy.getCircle();
			if (enemy.getDomain() == Entity.Domain.GROUND)
				ecircle.setLocation(ecircle.getX(), (int) Map.frameRefMapToScreen(ecircle.getY()));
			return ecircle.collidesWith(Screen.getRect());
		}
	}

	public void iterate() {
		ArrayList<Enemy> EnemysToRemove = new ArrayList<Enemy>();

		for (Enemy enemy : EnemyController.enemyArray) {
			enemy.iterateAI();

			if (!this.InsideScreen(enemy)) {
				EnemysToRemove.add(enemy);
				continue;
			}
			enemy.iteratePosition();
		}
		for (Enemy enemy : EnemysToRemove)
			EnemyController.enemyArray.remove(enemy);
	}
}
