package a3jedi;

public class ImmutableFrame2D implements Frame2D {

	private final int width;
	private final int height;
	private final Pixel[] pixels;
	
	public ImmutableFrame2D(int width, int height, Pixel[] pixels) {
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
			throw new RuntimeException("Invalid Pixel");
		}
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		Pixel[] newPixels = this.pixels.clone();
		newPixels[x + (y * this.width)] = p;
		ImmutableFrame2D newFrame = new ImmutableFrame2D(this.width, this.height, newPixels);
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
		GrayPixel[] grayPixels = new GrayPixel[this.pixels.length];
		for (int i = 0; i < this.pixels.length; i++) {
				grayPixels[i] = new GrayPixel(this.pixels[i].getIntensity());
		}
		ImmutableGrayFrame2D frame = new ImmutableGrayFrame2D(this.width, this.height, grayPixels);
		return frame;
	}

}
