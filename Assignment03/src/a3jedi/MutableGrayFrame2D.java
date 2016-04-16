package a3jedi;

import java.util.ArrayList;


public class MutableGrayFrame2D implements GrayFrame2D {

	private int width;
	private int height;
	private ArrayList<ArrayList<GrayPixel>> pixels = new ArrayList<ArrayList<GrayPixel>>();
	
	public MutableGrayFrame2D(int width, int height) {
		if (width < 1 || height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
		this.width = width;
		this.height = height;
		for (int i = 0; i < height; i++) {
			this.pixels.add(new ArrayList<GrayPixel>());
			for (int j = 0; j < width; j++)
				this.pixels.get(i).add(new GrayPixel(0.0));
		}		
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}

	public Pixel getPixel(int x, int y) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		double d = this.pixels.get(x).get(y).getIntensity();
		Pixel p = new ColorPixel(d, d, d);
		return p;
	}
	
	public Frame2D setPixel(int x, int y, Pixel p) {
		if (p == null) {
			throw new RuntimeException("Pixel reference is null");
		}
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		this.pixels.get(y).set(x, new GrayPixel(p.getIntensity()));
		return this;
	}

	public GrayPixel getGrayPixel(int x, int y) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		return this.pixels.get(y).get(x);
	}

	@Override
	public GrayFrame2D setGrayPixel(int x, int y, GrayPixel p) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		this.setPixel(y, x, new GrayPixel(p.getIntensity()));
		return this;
	}
	
	public Frame2D lighten(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.setPixel(i, j, this.getGrayPixel(i, j).lighten(factor));
			}
		}
		return this;
	}
	
	public Frame2D darken(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.setPixel(i, j, this.getGrayPixel(i, j).darken(factor));
			}
		}
		return this;
	}
	
	public GrayFrame2D toGrayFrame() {
		return this;
	}

}
