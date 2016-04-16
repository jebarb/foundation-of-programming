package a7;

public class MutableFrame2D extends AnyFrame2D {

	Pixel[][] pixels;
	
	
	public MutableFrame2D(int width, int height) {
		this(width, height, new ColorPixel(0,0,0));
	}
	
	public MutableFrame2D(int width, int height, Pixel init_color) {
		if (width < 1 || height < 1) {
			throw new IllegalFrame2DGeometryException();
		}
		if (init_color == null) {
			throw new IllegalArgumentException("Initial pixel is null");
		}
		
		pixels = new Pixel[width][height];
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				pixels[x][y] = init_color;
			}
		}
	}
	
	public MutableFrame2D(Pixel[][] pixels) {
		if ((pixels.length < 1) || (pixels[0].length < 1)) {
			throw new IllegalFrame2DGeometryException();
		}
		int width = pixels.length;
		int height = pixels[0].length;
		
		
		this.pixels = new Pixel[width][height];
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				if (pixels[x][y] == null) {
					throw new IllegalArgumentException("Pixels contains null");
				} else {
					this.pixels[x][y] = pixels[x][y];
				}
			}
		}
	}
	
	@Override
	public int getWidth() {
		return pixels.length;
	}

	@Override
	public int getHeight() {
		return pixels[0].length;
	}

	@Override
	public Pixel getPixel(int x, int y) {
		if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Coordinates out of range");
		}
		return pixels[x][y];
	}
	
	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Coordinates out of range");
		}
		if (p == null) {
			throw new IllegalArgumentException("Pixel value is null");
		}
		pixels[x][y] = p;
		return this;
	}
}
