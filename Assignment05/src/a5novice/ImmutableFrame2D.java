package a5novice;

public class ImmutableFrame2D extends AnyFrame2D {

	private Pixel[][] pixels;
	
	public ImmutableFrame2D(Pixel[][] pixels) {
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
	
	public ImmutableFrame2D(Pixel[][] pixels, int x, int y, Pixel p) {
		this(pixels);
		if (p == null) {
			throw new IllegalArgumentException("Parameter p is null");
		}
		checkGet(x, y);
		this.pixels[x][y] = p;
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
		checkGet(x, y);
		return pixels[x][y];
	}
	
	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		checkSet(x, y);
		if (p == null) {
			throw new IllegalArgumentException("Pixel is null");
		}
		return new ImmutableFrame2D(pixels, x, y, p);
	}

}
