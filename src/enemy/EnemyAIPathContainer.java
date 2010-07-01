package enemy;

import extensions.Vector2D;

public class EnemyAIPathContainer { // Container for Line Paths and Circular Paths

	public enum PathType {
		LINE, CIRCLE
	};

	private PathType pathType;

	private Vector2D target;
	private Vector2D center;
	private double ccwRadians;

	public EnemyAIPathContainer(Vector2D target) {
		this.pathType = EnemyAIPathContainer.PathType.LINE;
		this.target = target;
	}

	public EnemyAIPathContainer() {
		this.pathType = EnemyAIPathContainer.PathType.LINE;
		this.target = EnemyAI.SHIPPOS;
	}

	public EnemyAIPathContainer(Vector2D center, double ccwRadians) {
		this.pathType = EnemyAIPathContainer.PathType.CIRCLE;
		this.center = center;
		this.ccwRadians = ccwRadians;
	}

	public Vector2D getTarget() {
		if (this.pathType == EnemyAIPathContainer.PathType.LINE)
			return this.target;

		System.err.println("Error: EnemyAIPath,getTarget()");
		return new Vector2D(0, 0);
	}

	public Vector2D getCenter() {
		if (this.pathType == EnemyAIPathContainer.PathType.CIRCLE)
			return this.center;
		System.err.println("Error: EnemyAIPath,getCenter()");
		return new Vector2D(0, 0);
	}

	public double getRotation() {
		if (this.pathType == EnemyAIPathContainer.PathType.CIRCLE)
			return this.ccwRadians;
		System.err.println("Error: EnemyAIPath,getRotation()");
		return 0;

	}

	public PathType getType() {
		return this.pathType;
	}

}
