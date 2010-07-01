package images;

import java.util.ArrayList;

public class AnimationController {
	private static AnimationController instance = new AnimationController();
	private static final ArrayList<Animation> animationArray = new ArrayList<Animation>();

	public static AnimationController Instance() {
		return AnimationController.instance;
	}

	public void add(Animation animation) {
		AnimationController.animationArray.add(animation);
	}

	public void remove(Animation animation) {
		AnimationController.animationArray.remove(animation);
	}

	public ArrayList<Animation> getAnimationArray() {
		return AnimationController.animationArray;
	}

	public void iterate() {
		ArrayList<Animation> animationToRemove = new ArrayList<Animation>();
		for (Animation animation : AnimationController.animationArray)
			if ((animation.getLoops() >= animation.getMaxLoops()) && (animation.getMaxLoops() > 0))
				animationToRemove.add(animation);
		for (Animation animation : animationToRemove)
			AnimationController.animationArray.remove(animation);

	}
}
