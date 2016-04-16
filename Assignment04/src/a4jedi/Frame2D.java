package a4jedi;

public interface Frame2D {

	int getWidth();
	int getHeight();
	Pixel getPixel(int x, int y);
	Frame2D setPixel(int x, int y, Pixel p);
	Frame2D lighten(double factor);
	Frame2D darken(double factor);
	GrayFrame2D toGrayFrame();
	IndirectFrame2D extract(int xOffset, int yOffset, int width, int height);
	TransformedFrame2D transform(PixelTransformation xform);
}
