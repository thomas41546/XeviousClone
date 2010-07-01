package images;

import java.awt.Image;
import java.util.ArrayList;

import singletons.Debug;
import extensions.Vector2D;

public class Animation extends Sprite {
	private ArrayList<Image> Images;
	private final int frameRate;
	private final int maxLoops;
	private Vector2D position;
	private Vector2D animationSize;

	private int loops;
	private long lastTime;
	private int index;

	public Animation(Image image, int frameRate, Vector2D position, int maxLoops) {
		super(image);
		this.index = 0;
		this.loops = 0;
		this.lastTime = System.currentTimeMillis();
		this.maxLoops = maxLoops;
		this.animationSize = null;

		this.frameRate = frameRate;
		this.Images = new ArrayList<Image>();
		this.Images.add(image);
		this.position = new Vector2D(position);

	}

	public Animation(Image image, int frameRate, int maxLoops) {
		super(image);
		this.frameRate = frameRate;
		this.index = 0;
		this.loops = 0;
		this.lastTime = System.currentTimeMillis();
		this.maxLoops = maxLoops;
		this.animationSize = null;

		this.Images = new ArrayList<Image>();
		this.Images.add(image);
		this.position = new Vector2D(0, 0);
	}

	public Animation(Image image, int frameRate) {
		super(image);
		this.frameRate = frameRate;
		this.index = 0;
		this.loops = 0;
		this.lastTime = System.currentTimeMillis();
		this.maxLoops = 0;
		this.animationSize = null;

		this.Images = new ArrayList<Image>();
		this.Images.add(image);
		this.position = new Vector2D(0, 0);
	}

	public Animation(Image image) {
		super(image);
		this.frameRate = 0;
		this.index = 0;
		this.loops = 0;
		this.lastTime = System.currentTimeMillis();
		this.maxLoops = 0;
		this.animationSize = null;

		this.Images = new ArrayList<Image>();
		this.Images.add(image);
		this.position = new Vector2D(0, 0);
	}

	public Animation(Animation clone) {
		super(clone.image);
		this.index = 0;
		this.loops = 0;
		this.animationSize = clone.animationSize;
		this.lastTime = System.currentTimeMillis();
		this.maxLoops = clone.maxLoops;
		this.frameRate = clone.frameRate;
		this.Images = clone.Images;
		this.position = clone.position;
	}

	public void addImage(Image image) {
		if (this.frameRate == 0)
			Debug.error("Animation addImage", "framerate == 0");
		this.Images.add(image);
	}

	public int getFrameRate() {
		return this.frameRate;
	}

	public int getMaxLoops() {
		return this.maxLoops;
	}

	public int getLoops() {
		return this.loops;
	}

	public Vector2D getPositon() {
		return this.position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	@Override
	public Image getImage() {
		Image current = this.image;
		this.iterate();
		return current;
	}

	private void iterate() {
		if ((System.currentTimeMillis() - this.lastTime > this.frameRate) && (this.frameRate > 0)
				&& ((this.getLoops() < this.getMaxLoops()) || (this.getMaxLoops() == 0))) {
			this.lastTime = System.currentTimeMillis();
			if (this.index >= this.Images.size()) {
				this.index = 0;
				++this.loops;
			}
			this.setSprite(this.Images.get(this.index++));
		}
	}

	public void forceIteratation() {
		this.lastTime = System.currentTimeMillis();
		if (this.index >= this.Images.size()) {
			this.index = 0;
			++this.loops;
		}
		this.setSprite(this.Images.get(this.index++));
	}

	public void setAnimationSize(Vector2D animationSize) {
		this.animationSize = animationSize;
	}

	public Vector2D getAnimationSize() {
		return this.animationSize;
	}

	/**
	 * @return      If animationSize equals NULL it returns the value of getSize from its super class,
	 * 				otherwise it returns animationSize.
	 */
	@Override
	public Vector2D getSize() {
		if (this.animationSize != null)
			return this.animationSize;
		else
			return super.getSize();
	}

}
