package a3novice;

import java.util.ArrayList;

public class MutableFrame2D implements Frame2D {
	
	private int width;
	private int height;
	private ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
	
	public MutableFrame2D(int width, int height) {
		this.width = width;
		this.height = height;
		if (width < 1 || height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
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
		if ((x < 0) || (x > width - 1) || (y < 0 || y > height - 1)) {
			throw new RuntimeException("Index out of bounds");
		}
		return this.pixels.get(x).get(y);
	}
	
	public Frame2D setPixel(int x, int y, Pixel p) {
		if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
			throw new RuntimeException("Index out of bounds");
		}
		pixels.get(x).set(y, p);	
		return this;
	}


}
