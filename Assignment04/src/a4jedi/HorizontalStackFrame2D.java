package a4jedi;

public class HorizontalStackFrame2D extends AnyFrame2D {

	private Frame2D left;
	private Frame2D right;
	private int width;
	private int height;

	public HorizontalStackFrame2D(Frame2D left, Frame2D right) {
		if ((left == null) || (right == null)) {
			throw new IllegalArgumentException();
		}
		if (left.getHeight() != right.getHeight()) {
			throw new IllegalFrame2DGeometryException();
		}

		this.width = left.getWidth() + right.getWidth();
		this.height = left.getHeight();
		this.left = left;
		this.right = right;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public Pixel getPixel(int x, int y) {
		check_coordinates(x,y);
		if (x >= this.left.getWidth()) {
			return this.right.getPixel(x - this.left.getWidth(), y);
		} else {
			return this.left.getPixel(x, y);
		}
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}
		if (x >= this.left.getWidth()) {
			this.right.setPixel(x - this.left.getWidth(), y, p);
		} else {
			this.left.setPixel(x, y, p);
		}
		return this;
	}

}