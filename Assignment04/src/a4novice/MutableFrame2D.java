package a4novice;

public class MutableFrame2D implements Frame2D {
	static final Pixel DEFAULT_INIT_PIXEL = new ColorPixel(0.0, 0.0, 0.0);

	protected int width;
	protected int height;
	protected Pixel[] pixels;

	public MutableFrame2D(int width, int height) {
		if ((width < 1) || (height < 1)) {
			throw new RuntimeException();
		}
		this.width = width;
		this.height = height;
		pixels = new Pixel[width*height];
		for (int i=0; i<width*height; i++) {
			pixels[i] = DEFAULT_INIT_PIXEL;
		}
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
		return this.pixels[x + (this.width * y)];
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}

		pixels[x + (y * this.width)] = p;
		return this;
	}

	@Override
	public Frame2D lighten(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.setPixel(i, j, this.getPixel(i, j).lighten(factor));
			}
		}
		return this;
	}

	@Override
	public Frame2D darken(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.setPixel(i, j, this.getPixel(i, j).darken(factor));
			}
		}
		return this;
	}


	@Override
	public GrayFrame2D toGrayFrame() {
		GrayFrame2D gray_frame = new MutableGrayFrame2D(getWidth(), getHeight());
		for (int x=0; x < gray_frame.getWidth(); x++) {
			for (int y=0; y < gray_frame.getHeight(); y++) {
				gray_frame.setPixel(x, y, getPixel(x, y));
			}
		}
		return gray_frame;
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
