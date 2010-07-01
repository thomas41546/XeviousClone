package bullet;

import java.awt.Rectangle;
import java.util.ArrayList;

import singletons.Debug;
import singletons.Map;
import singletons.Screen;
import entity.Entity;
import extensions.Circle;

public class BulletController {
	private static BulletController instance;
	private static final ArrayList<Bullet> bulletArray = new ArrayList<Bullet>();

	public static BulletController Instance() {
		if (BulletController.instance == null)
			BulletController.instance = new BulletController();
		return BulletController.instance;
	}

	public void add(Bullet bullet) {
		BulletController.bulletArray.add(bullet);
	}

	public void remove(Bullet bullet) {
		BulletController.bulletArray.remove(bullet);
	}

	public ArrayList<Bullet> getBulletArray() {
		return BulletController.bulletArray;
	}

	public boolean InsideScreen(Bullet bullet) {
		if (bullet.getShape() == Entity.Shape.RECTANGLE) {
			Rectangle erect = bullet.getRect();
			if (bullet.getDomain() == Entity.Domain.GROUND)
				erect.setLocation(erect.x, (int) Map.frameRefMapToScreen(erect.y));
			return Screen.getRect().intersects(erect);
		}
		else if (bullet.getShape() == Entity.Shape.CIRCLE) {
			Circle circ = bullet.getCircle();
			if (bullet.getDomain() == Entity.Domain.GROUND)
				circ.setLocation(circ.getX(), (int) Map.frameRefMapToScreen(circ.getY())); //TODO test circle ground of REF.
			return circ.collidesWith(Screen.getRect());
		}
		Debug.error("InsideScreen", "Fatal Case");
		return false;
	}

	public void iterate() {
		ArrayList<Bullet> BulletsToRemove = new ArrayList<Bullet>();

		for (Bullet bullet : BulletController.bulletArray) {
			if (!this.InsideScreen(bullet)) {
				BulletsToRemove.add(bullet);
				continue;
			}
			bullet.iteratePosition();
		}
		for (Bullet bullet : BulletsToRemove)
			BulletController.bulletArray.remove(bullet);
	}
}
