package extensions;

public class Vector2D {
	public double x = 0.0;
	public double y = 0.0;

	public Vector2D() {
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	// BASIC ASSIGNMENT METHODS
	// -------------------------------
	//
	void setTo(Vector2D vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	void setTo(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return new String("[" + (int) this.x + "," + (int) this.y + "]");
	}

	public double length() {
		return Math.sqrt((this.x * this.x) + (this.y * this.y));
	}

	public Vector2D normalize() {
		double len = this.length();

		if (len != 0.0) {
			this.x /= len;
			this.y /= len;
		}
		else {
			this.x = 0.0;
			this.y = 0.0;
		}

		return new Vector2D(this);
	}

	// 2. Addition methods
	// -------------------
	//
	public Vector2D add(Vector2D vec) {
		this.x += vec.x;
		this.y += vec.y;

		return new Vector2D(this);
	}

	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return new Vector2D(this);
	}

	// 3. Subtraction methods
	// -----------------------
	//
	public Vector2D sub(Vector2D vec) {
		this.x -= vec.x;
		this.y -= vec.y;

		return new Vector2D(this);
	}

	public Vector2D sub(double x, double y) {
		this.x -= x;
		this.y -= y;

		return new Vector2D(this);
	}

	// 4. Multiplication methods
	// -------------------------
	//
	public Vector2D mul(Vector2D vec) {
		this.x *= vec.x;
		this.y *= vec.y;

		return new Vector2D(this);
	}

	public Vector2D mul(double x, double y) {
		this.x += x;
		this.y += y;

		return new Vector2D(this);
	}

	public Vector2D mul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;

		return new Vector2D(this);
	}

	public double abs() {
		return Math.abs(this.x) + Math.abs(this.y);
	}

	// Dot Product
	public double dot(Vector2D vec) {
		return (this.x * vec.x + this.y * vec.y);
	}

	// Angle Between Two Vectors Relative to the Horizontal Axis
	public double angle(Vector2D vec) {
		return Math.atan2(vec.y - this.y, vec.x - this.x);
	}

	public double distance(Vector2D vec) {
		return Math.sqrt((this.x - vec.x) * (this.x - vec.x) + (this.y - vec.y) * (this.y - vec.y));
	}

}
