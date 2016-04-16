package a3jedi;

public class ImmutableGrayFrame2D implements GrayFrame2D {

	private final int width;
	private final int height;
	private final GrayPixel[] pixels;
	
	public ImmutableGrayFrame2D(int width, int height, GrayPixel[] pixels) {
		if (pixels == null || pixels.length < 1 || pixels.length != width * height) {
			throw new RuntimeException("Invalid Pixel array");
		}
		if (width < 1 || height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
		for (Pixel p: pixels) {
			if (p == null) {
				throw new RuntimeException("Invalid Pixel in array");
			}
		}
		this.height = height;
		this.width = width;
		this.pixels = pixels.clone();
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
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		return this.pixels[x + (this.width * y)];
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		if (p == null) {
			throw new RuntimeException("Pixel reference is null");
		}
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		GrayPixel[] newPixels = this.pixels.clone();
		newPixels[x + (y * this.width)] = new GrayPixel(p.getIntensity());
		ImmutableGrayFrame2D newFrame = new ImmutableGrayFrame2D(width, height, newPixels);
		return newFrame;
	}

	@Override
	public Frame2D lighten(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		Pixel[] newPix = this.pixels.clone();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				newPix[i + (j * this.width)] = this.getPixel(i, j).lighten(factor);
			}
		}
		ImmutableFrame2D newFrame = new ImmutableFrame2D(this.width, this.height, newPix);
		return newFrame;
	}

	@Override
	public Frame2D darken(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		Pixel[] newPix = this.pixels.clone();
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				newPix[i + (j * this.width)] = this.getPixel(i, j).darken(factor);
			}
		}
		ImmutableFrame2D newFrame = new ImmutableFrame2D(this.width, this.height, newPix);
		return newFrame;
	}

	@Override
	public GrayFrame2D toGrayFrame() {
		return this;
	}

	@Override
	public GrayPixel getGrayPixel(int x, int y) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		return this.pixels[x + (this.width * y)];
	}

	@Override
	public GrayFrame2D setGrayPixel(int x, int y, GrayPixel p) {
		if (p == null) {
			throw new RuntimeException("Pixel reference is null");
		}
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		GrayPixel[] newPixels = this.pixels.clone();
		newPixels[x + (y * this.width)] = new GrayPixel(p.getIntensity());
		ImmutableGrayFrame2D newFrame = new ImmutableGrayFrame2D(width, height, newPixels);
		return newFrame;
	}

}
