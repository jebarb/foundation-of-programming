package a5adept;
import java.util.Iterator;

public interface Frame2D extends Iterable<Pixel> {

	int getWidth();
	int getHeight();
	Pixel getPixel(int x, int y);
	Frame2D setPixel(int x, int y, Pixel p);
	Frame2D lighten(double factor);
	Frame2D darken(double factor);
	IndirectFrame2D extract(int xoff, int yoff, int width, int height);
    Pixel getPixel(Coordinate c);
    Frame2D setPixel(Coordinate c, Pixel p);
    IndirectFrame2D extract(Coordinate corner_a, Coordinate corner_b);
    Iterator<Pixel> iterator();
    Iterator<Pixel> sample(int init_x, int init_y, int dx, int dy);
    Iterator<IndirectFrame2D> window(int window_width, int window_height);
    Iterator<IndirectFrame2D> tile(int tile_width, int tile_height);
}
