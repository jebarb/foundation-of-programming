package a2;

public class Picture {

	private static final int INIT_PIXEL_INTENSITY = 99;
	
	private Pixel[][] pixels;
	
	// Constructor
	// width : width of new Picture object in pixels
	// height : height of new Picture object in pixels
	public Picture(int width, int height) {
		if (width < 1 || height < 1) {
			throw new RuntimeException("Illegal width or height");
		}
		
		pixels = new Pixel[width][height];
		
		Pixel init_pixel = new Pixel(INIT_PIXEL_INTENSITY);
		
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				pixels[x][y] = init_pixel;
			}
		}
	}
	
	// getWidth returns the width of the Picture object in pixels
	public int getWidth() {
		return pixels.length;
	}
	
	// getHeight returns the height of the Picture object in pixels
	public int getHeight() {
		return pixels[0].length;
	}
	
	// setPixel sets the position (x,y) of the Picture to the Pixel object p
	public void setPixel(int x, int y, Pixel p) {
		checkCoordinates(x,y);
		if (p == null) {
			throw new RuntimeException("Pixel is null");
		}
		pixels[x][y] = p;
	}
	
	// getPixel retrieves the Pixel object at position (x,y) of the Picture
	public Pixel getPixel(int x, int y) {
		checkCoordinates(x,y);
		return pixels[x][y];
	}

	// checkCoordinates is a private internal helper function that
	// checks that a position (x,y) is within the Picture
	private void checkCoordinates(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new RuntimeException("value " + x + "for x is out of bounds");
		}
		if (y < 0 || y >= getHeight()) {
			throw new RuntimeException("value " + y + "for y is out of bounds");
		}
	}
}
