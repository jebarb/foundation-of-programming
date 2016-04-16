package a4adept;

public class VerticalStackFrame2D implements Frame2D {

	static final Pixel DEFAULT_INIT_PIXEL = new ColorPixel(0.0, 0.0, 0.0);

	protected int width;
	protected int height;
	protected Frame2D top;
	protected Frame2D bottom;
	
	public VerticalStackFrame2D(Frame2D top, Frame2D bottom) {
		if ((bottom == null) || (top == null)) {
			throw new IllegalArgumentException();
		}
		if (top.getWidth() != bottom.getWidth()) {
			throw new IllegalFrame2DGeometryException();
		}

		this.width = top.getWidth();
		this.height = top.getHeight() + bottom.getHeight();
		this.top = top;
		this.bottom = bottom;
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
		if (y >= this.top.getHeight()) {
			return this.bottom.getPixel(x, y - this.top.getHeight());
		} else {
			return this.top.getPixel(x, y);
		}
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}
		if (y >= this.top.getHeight()) {
			this.bottom.setPixel(x, y - this.top.getHeight(), p);
		} else {
			this.top.setPixel(x, y, p);
		}
		return this;
	}
	
	@Override
	public Frame2D lighten(double factor) {
		check_factor(factor);
		this.top.lighten(factor);
		this.bottom.lighten(factor);
		return this;
	}

	@Override
	public Frame2D darken(double factor) {
		check_factor(factor);
		this.top.darken(factor);
		this.bottom.darken(factor);
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