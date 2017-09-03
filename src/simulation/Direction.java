package simulation;

public class Direction {

	private int angle;

	
	/**
	 * Creates a direction (angle)
	 * @param unit from 0.0 to 1.0, with 0.0 being 0 degrees, 1.0 being 360 degrees
	 */
	public Direction(double unit) {
		int unitAngle = (int) (360.0*unit);
		if (unitAngle >= 360) throw new RuntimeException("Unit too big: " + unitAngle);
		this.angle = unitAngle;
	}
	
	/**
	 * Creates a direction (angle)
	 * @param angle in degrees. Will be converted to an angle between 0 and 360 degrees.
	 */
	public Direction(int angle) {
		this.angle = sanitizeAngle(angle);
	}
	
	public void turn(int byAngle) {
		this.angle = sanitizeAngle(byAngle+angle);
	}
	
	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = sanitizeAngle(angle);
	}

	private int sanitizeAngle(int angle) {
		return angle < 0 ? 360 + angle%360 : angle%360;
	}
	
	public String toString() {
		return Integer.toString(angle);
	}

}
