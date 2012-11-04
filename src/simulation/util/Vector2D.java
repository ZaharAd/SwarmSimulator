package simulation.util;

public class Vector2D {

	public double x = 0;
	public double y = 0;
	
	public Vector2D() {}
	
	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(int x, int y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}
	
	public void subtract(int x, int y) {
		this.x = this.x - x;
		this.y = this.y - y;
	}
	
	public void divide(int divisor) {
		this.x = x/divisor;
		this.y = y/divisor;
	}
	
	public void multiply(int x, int y) {
		this.x = this.x * x;
		this.y = this.y * y;
	}

	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
	}
	
	public int getX() {
		return (int) Math.round(x);
	}
	
	public int getY() {
		return (int) Math.round(y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2D other = (Vector2D) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	public String toString() {
		return "[" + x + "," + y + "]";
	}

	public double distance(double x2, double y2) {
		return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
	}
	
	public static void main(String[] args) {
		Vector2D vec = new Vector2D();
		System.out.println(vec.distance(3, 4) == 5.0 ? "OK" : "FAIL");
	}
	
}
