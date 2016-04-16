package a4jedi;

public class VerticalStackFrame2D extends AnyFrame2D implements Frame2D {

	private Frame2D top;
	private Frame2D bottom;
	private int width;
	private int height;
	
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

}