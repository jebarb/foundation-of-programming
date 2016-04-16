package a4jedi;

public class IndirectFrame2DImpl extends AnyFrame2D implements IndirectFrame2D {

	static final Pixel DEFAULT_INIT_PIXEL = new ColorPixel(0.0, 0.0, 0.0);

	private Frame2D source;
	private int xOffset;
	private int yOffset;
	private int width;
	private int height;

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

}
