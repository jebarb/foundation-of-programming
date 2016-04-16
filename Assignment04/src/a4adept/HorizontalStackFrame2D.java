package a4adept;

public class HorizontalStackFrame2D implements Frame2D {

	protected int width;
	protected int height;
	protected Frame2D left;
	protected Frame2D right;

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

	@Override
	public Frame2D lighten(double factor) {
		check_factor(factor);
		this.left.lighten(factor);
		this.right.lighten(factor);
		return this;
	}

	@Override
	public Frame2D darken(double factor) {
		check_factor(factor);
		this.left.darken(factor);
		this.right.darken(factor);
		return this;
	}


	@Override
	public GrayFrame2D toGrayFrame() {
		GrayFrame2D gray_frame = new MutableGrayFrame2D(getWidth(), getHeight());
		for (int x=0; x< getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				gray_frame.setPixel(x, y, getPixel(x, y));
			}
		}
		return gray_frame;
	}		

	public IndirectFrame2D extract(int xOffset, int yOffset, int width, int height) {
		IndirectFrame2D frame = new IndirectFrame2DImpl(this, xOffset, yOffset, width, height);
		return frame;
	}

	private boolean check_coordinates(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new RuntimeException("x is out of bounds");
		}
		if (y < 0 || y >= getHeight()) {
			throw new RuntimeException("y is out of bounds");
		}
		return true;
	}

	private static boolean check_factor(double factor) {
		if (factor < 0) {
			throw new RuntimeException("Factor can not be less than 0.0");
		} else if (factor > 1.0) {
			throw new RuntimeException("Factor can not be greater than 1.0");
		}
		return true;
	}


}