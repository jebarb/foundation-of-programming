package a4novice;

public interface Frame2D {

	int getWidth();
	int getHeight();
	Pixel getPixel(int x, int y);
	Frame2D setPixel(int x, int y, Pixel p);
	Frame2D lighten(double factor);
	Frame2D darken(double factor);
	GrayFrame2D toGrayFrame();
}
