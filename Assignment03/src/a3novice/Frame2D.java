package a3novice;

public interface Frame2D {
	
	int getWidth();
	int getHeight();
	Pixel getPixel(int x, int y);
	Frame2D setPixel(int x, int y, Pixel p);

}
