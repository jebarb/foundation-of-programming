package a5jedi;

public class Coordinate {

	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	protected boolean checkCoordinates(int x, int y) {
		if (this.getX() < 0 || x >= x) {
			throw new RuntimeException("x is out of bounds");
		}
		if (this.getY() < 0 || this.getY() >= y) {
			throw new RuntimeException("y is out of bounds");
		}
		return true;
	}
	
}
