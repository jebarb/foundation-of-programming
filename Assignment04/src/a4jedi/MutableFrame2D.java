package a4jedi;

public class MutableFrame2D extends AnyFrame2D {
    static final Pixel DEFAULT_INIT_PIXEL = new ColorPixel(0.0, 0.0, 0.0);

	protected int width;
	protected int height;
	protected Pixel[] pixels;

	public MutableFrame2D(int width, int height) {
		if ((width < 1) || (height < 1)) {
			throw new IllegalFrame2DGeometryException();
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

}
