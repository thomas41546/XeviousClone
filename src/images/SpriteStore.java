package images;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import singletons.Debug;

public class SpriteStore {
	private static SpriteStore instance = new SpriteStore();
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	public static SpriteStore Instance() {
		return SpriteStore.instance;
	}

	public Sprite getSprite(String ref) {
		if (this.sprites.get(ref) != null)
			return this.sprites.get(ref);

		BufferedImage sourceImage = null;

		try {
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null)
				Debug.error("getSprite", "Can't find ref: " + ref);
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			Debug.error("getSprite", "Failed to load: " + ref);
		}

		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),
				sourceImage.getHeight(), Transparency.BITMASK);
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		Sprite sprite = new Sprite(image);
		this.sprites.put(ref, sprite);
		return sprite;
	}

}