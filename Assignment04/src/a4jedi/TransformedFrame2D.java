package a4jedi;

public class TransformedFrame2D extends AnyFrame2D {

	private Frame2D source;
	private PixelTransformation xform;
	private int width;
	private int height;

	public TransformedFrame2D (Frame2D source, PixelTransformation xform) {
		if (source == null || xform == null) {
			throw new IllegalArgumentException();
		}
		this.source = source;
		this.xform = xform;
		this.width = source.getWidth();
		this.height = source.getHeight();
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
		return this.xform.transform(this.source.getPixel(x, y));
	}


	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		check_coordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Null pixel error");
		}
		Frame2D newFrame = new MutableGrayFrame2D(this.getWidth(), this.getHeight());
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (i == x && j == y) {
					newFrame.setPixel(x, y, p);
				} else {
					newFrame.setPixel(i, j, xform.transform(this.source.getPixel(x, y)));
				}
			}
		}
		return newFrame;
	}

	@Override
	public Frame2D lighten(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.source.setPixel(i, j, this.source.getPixel(i, j).lighten(factor));
			}
		}

		return this;
	}

	@Override
	public Frame2D darken(double factor) {
		check_factor(factor);
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.source.setPixel(i, j, this.source.getPixel(i, j).darken(factor));
			}
		}
		return this;
	}
	
}
