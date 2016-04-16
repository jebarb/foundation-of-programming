package a4adept;

public class MutableGrayFrame2D extends MutableFrame2D implements GrayFrame2D {
static final Pixel DEFAULT_INIT_PIXEL = new GrayPixel(0.0);
	
	public MutableGrayFrame2D(int width, int height) {
		super(width, height);
		for (int i=0; i<width*height; i++) {
			pixels[i] = DEFAULT_INIT_PIXEL;
		}
	}	

	@Override
	public Pixel getPixel(int x, int y) {
		check_coordinates(x,y);
		Pixel p = new GrayPixel(pixels[x + (y * this.width)].getIntensity());
		return p;
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}
		pixels[x + (y * this.width)] = new GrayPixel(p.getIntensity());
		return this;
	}
	
	@Override
	public GrayPixel getGrayPixel(int x, int y) {
		check_coordinates(x,y);
		GrayPixel p = new GrayPixel(this.getPixel(x, y).getIntensity());
		return p;
	}

	@Override
	public GrayFrame2D setGrayPixel(int x, int y, GrayPixel p) {
		check_coordinates(x,y);
		this.setPixel(x, y, new ColorPixel(p.getIntensity(), p.getIntensity(), p.getIntensity()));
		return this;
	}
	
	@Override
	public GrayFrame2D toGrayFrame() {
		return this;
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

}