package images;

import java.awt.Graphics;
import java.awt.Image;

import singletons.Debug;
import extensions.Vector2D;

public class Sprite {
	protected Image image;

	public Sprite(Image image) {
		this.setSprite(image);
	}

	public void setSprite(Image image) {
		this.image = image;
	}

	public int getWidth() {
		if (this.image.getWidth(null) == -1)
			Debug.error("Sprite:getWidth", "-1");
		return this.image.getWidth(null);
	}

	public int getHeight() {
		if (this.image.getHeight(null) == -1)
			Debug.error("Sprite:getheight", "-1");
		return this.image.getHeight(null);
	}

	public Vector2D getSize() {
		return new Vector2D(this.getWidth(), this.getHeight());
	}

	public Image getImage() {

		return this.image;
	}

	public void draw(Graphics g, int x, int y) {
		g.drawImage(this.image, x, y, null);
	}

	public Sprite getSprite() {
		return this;
	}
}