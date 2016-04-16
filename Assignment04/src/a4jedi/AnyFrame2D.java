package a4jedi;

public abstract class AnyFrame2D implements Frame2D {
	
	abstract public int getWidth();
	abstract public int getHeight();
	abstract public Pixel getPixel(int x, int y);
	abstract public Frame2D setPixel(int x, int y, Pixel p);

	public Frame2D lighten(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				this.setPixel(i, j, this.getPixel(i, j).lighten(factor));
			}
		}
		return this;
	}

	public Frame2D darken(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				this.setPixel(i, j, this.getPixel(i, j).darken(factor));
			}
		}
		return this;
	}

	public GrayFrame2D toGrayFrame() {
		GrayFrame2D gray_frame = new MutableGrayFrame2D(getWidth(), getHeight());
		for (int x=0; x < gray_frame.getWidth(); x++) {
			for (int y=0; y < gray_frame.getHeight(); y++) {
				gray_frame.setPixel(x, y, getPixel(x, y));
			}
		}
		return gray_frame;
	}		

	public IndirectFrame2D extract(int xOffset, int yOffset, int width, int height) {
		IndirectFrame2D frame = new IndirectFrame2DImpl(this, xOffset, yOffset, width, height);
		return frame;
	}

	public TransformedFrame2D transform(PixelTransformation xform) {
		TransformedFrame2D frame = new TransformedFrame2D(this, xform);
		return frame;
	}

	protected boolean check_coordinates(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new RuntimeException("x is out of bounds");
		}
		if (y < 0 || y >= getHeight()) {
			throw new RuntimeException("y is out of bounds");
		}
		return true;
	}

	protected static boolean check_factor(double factor) {
		if (factor < 0) {
			throw new RuntimeException("Factor can not be less than 0.0");
		} else if (factor > 1.0) {
			throw new RuntimeException("Factor can not be greater than 1.0");
		}
		return true;
	}

}
