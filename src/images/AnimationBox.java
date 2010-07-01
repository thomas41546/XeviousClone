package images;

import java.applet.Applet;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import extensions.Vector2D;

public class AnimationBox {
	private static AnimationBox instance = new AnimationBox();

	public static Animation explosion;
	public static Animation digits;
	public static Animation stdBullet;

	public static Animation playerShip;
	public static Animation bulletRed;
	public static Animation target;
	public static Animation targetRed;

	public static Animation techCircle;
	public static Animation greenShip;
	public static Animation blackShip;

	public static AnimationBox Instance() {
		return AnimationBox.instance;
	}

	public void init(Applet app) {

		/* Explosion */
		Image explosionImage = SpriteStore.Instance().getSprite(
				"explosion17.png").getImage();
		Image[] explosionArray = new Image[25];
		for (int y = 0; y < 5; y++)
			for (int x = 0; x < 5; x++)
				explosionArray[x + y * 5] = app
						.createImage(new FilteredImageSource(explosionImage
								.getSource(), new CropImageFilter(x * 64,
								y * 64, 64, 64)));
		AnimationBox.explosion = new Animation(explosionArray[0], 20,
				new Vector2D(200, 200), 1);
		for (int i = 0; i < 24; i++)
			AnimationBox.explosion.addImage(explosionArray[i + 1]);
		/* Explosion */

		/* Digits */
		Image digitImage = SpriteStore.Instance().getSprite("digits.png")
				.getImage();
		Image[] digitArray = new Image[12];
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 4; x++)
				digitArray[x + y * 4] = app
						.createImage(new FilteredImageSource(digitImage
								.getSource(), new CropImageFilter(x * 75,
								y * 100, 75, 100)));
		AnimationBox.digits = new Animation(digitArray[0], -1, 0);
		for (int i = 1; i < 12; i++)
			AnimationBox.digits.addImage(digitArray[i]);
		/* Digits */

		AnimationBox.stdBullet = new Animation(SpriteStore.Instance()
				.getSprite("bullet.png").getImage());

		AnimationBox.playerShip = new Animation(SpriteStore.Instance()
				.getSprite("ship.png").getImage(), 500);

		AnimationBox.playerShip.addImage(SpriteStore.Instance().getSprite(
				"ship2.png").getImage());

		AnimationBox.bulletRed = new Animation(SpriteStore.Instance()
				.getSprite("bulletred.png").getImage());

		AnimationBox.target = new Animation(SpriteStore.Instance().getSprite(
				"target.png").getImage());

		AnimationBox.targetRed = new Animation(SpriteStore.Instance()
				.getSprite("targetred.png").getImage());

		AnimationBox.techCircle = new Animation(SpriteStore.Instance()
				.getSprite("techcircle.png").getImage());

		AnimationBox.greenShip = new Animation(SpriteStore.Instance()
				.getSprite("enemy.png").getImage());

		AnimationBox.blackShip = new Animation(SpriteStore.Instance()
				.getSprite("enemy2.png").getImage());
	}
}
