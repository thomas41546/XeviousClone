package singletons;

import java.awt.Dimension;
import java.awt.Rectangle;

import extensions.Vector2D;

public class Screen {
	private static final Dimension Dim = new Dimension(650, 750);

	public static double getWidth() {
		return Screen.Dim.getWidth();
	}

	public static double getHeight() {
		return Screen.Dim.getHeight();
	}

	public static Rectangle getRect() {
		return new Rectangle(0, 0, (int) Screen.getWidth(), (int) Screen.getHeight());
	}

	public static Dimension getDim() {
		return new Dimension(Screen.Dim);
	}

	public static Vector2D getDimVector2D() {
		return new Vector2D(Screen.getWidth(), Screen.getHeight());
	}
}
