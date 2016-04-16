package a4adept;

public class IndirectFrame2DImpl implements IndirectFrame2D {

	static final Pixel DEFAULT_INIT_PIXEL = new ColorPixel(0.0, 0.0, 0.0);

	protected Frame2D source;
	protected int xOffset;
	protected int yOffset;
	protected int width;
	protected int height;

	public IndirectFrame2DImpl(Frame2D source, int xOffset, int yOffset, int width, int height) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		if ((width < 1) || (height < 1) || (xOffset + width > source.getWidth()) || 
				(yOffset + height > source.getHeight()) || (xOffset < 0) || (yOffset < 0)) {
			throw new IllegalFrame2DGeometryException();
		}
		this.source = source;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
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
		return this.source.getPixel(x + xOffset, y + yOffset);
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}

		this.source.setPixel(x + xOffset, y + yOffset, p);
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
		GrayFrame2D gray_frame = new MutableGrayFrame2D(this.getWidth(), this.getHeight());
		for (int x=0; x < this.getWidth(); x++) {
			for (int y=0; y < this.getHeight(); y++) {
				gray_frame.setPixel(x, y, this.getPixel(x, y));
			}
		}
		return gray_frame;
	}		

	@Override
	public Frame2D getSource() {
		return this.source;
	}

	@Override
	public int getXOffset() {
		return this.xOffset;
	}

	@Override
	public int getYOffset() {
		return this.yOffset;
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
