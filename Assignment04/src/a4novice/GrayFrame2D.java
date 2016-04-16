package a4novice;

public interface GrayFrame2D extends Frame2D {
	GrayPixel getGrayPixel(int x, int y);
	GrayFrame2D setGrayPixel(int x, int y, GrayPixel p);
}
