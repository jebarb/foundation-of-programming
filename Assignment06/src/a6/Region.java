package a6;

public class Region {

	Coordinate upper_left;
	Coordinate lower_right;

	public Region(Coordinate a, Coordinate b) {

		int min_y = a.getY() <= b.getY() ? a.getY() : b.getY();
		int max_y = a.getY() >= b.getY() ? a.getY() : b.getY();
		int min_x = a.getX() <= b.getX() ? a.getX() : b.getX();
		int max_x = a.getX() >= b.getX() ? a.getX() : b.getX();

		upper_left = new Coordinate(min_x, min_y);
		lower_right = new Coordinate(max_x, max_y);
	}

	public Coordinate getUpperLeft() {
		return upper_left;
	}
	
	public Coordinate getLowerRight() {
		return lower_right;
	}
	
	public int getTop() {
		return upper_left.getY();
	}
	
	public int getBottom() {
		return lower_right.getY();
	}
	
	public int getLeft() {
		return upper_left.getX();
	}
	
	public int getRight() {
		return lower_right.getX();
	}

	public Region intersect(Region other) {
		if (other == null) return null;
		
		if (other.getBottom() < getTop() ||
			other.getTop() > getBottom() ||
			other.getLeft() > getRight() ||
			other.getRight() < getLeft()) {
			return null;
		}
		
		int top = other.getTop() >= getTop() ? other.getTop() : getTop();
		int bottom = other.getBottom() <= getBottom() ? other.getBottom() : getBottom();
		int left = other.getLeft() >= getLeft() ? other.getLeft() : getLeft();
		int right = other.getRight() <= getRight() ? other.getRight() : getRight();
		
		return new Region(new Coordinate(left, top), new Coordinate(right, bottom));
	}
	
	public Region union(Region other) {
		if (other == null) return this;
		
		int top = other.getTop() <= getTop() ? other.getTop() : getTop();
		int bottom = other.getBottom() >= getBottom() ? other.getBottom() : getBottom();
		int left = other.getLeft() <= getLeft() ? other.getLeft() : getLeft();
		int right = other.getRight() >= getRight() ? other.getRight() : getRight();
		
		return new Region(new Coordinate(left, top), new Coordinate(right, bottom));
	}
}
