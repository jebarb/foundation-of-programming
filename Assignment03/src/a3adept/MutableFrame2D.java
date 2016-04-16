package a3adept;

import java.util.ArrayList;

public class MutableFrame2D implements Frame2D {
	
	private int width;
	private int height;
	private ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
	
	public MutableFrame2D(int width, int height) {
		if (width < 1 || height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
		this.width = width;
		this.height = height;
		for (int i = 0; i < height; i++) {
			this.pixels.add(new ArrayList<Pixel>());
			for (int j = 0; j < width; j++)
				this.pixels.get(i).add(new ColorPixel(0.0, 0.0, 0.0));
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
		return this.pixels.get(y).get(x);
	}
	
	public Frame2D setPixel(int x, int y, Pixel p) {
		if (p == null) {
			throw new RuntimeException("Pixel reference is null");
		}
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		this.pixels.get(y).set(x, p);
		return this;
	}
	
	public Frame2D lighten(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				this.setPixel(i, j, this.getPixel(i, j).lighten(factor));
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
				this.setPixel(i, j, this.getPixel(i, j).darken(factor));
			}
		}
		return this;
	}
	
	public GrayFrame2D toGrayFrame() {
		MutableGrayFrame2D frame = new MutableGrayFrame2D(this.height, this.width);
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				frame.setPixel(i, j, new GrayPixel(this.getPixel(i, j).getIntensity()));
			}
		}
		return frame;
	}


}
