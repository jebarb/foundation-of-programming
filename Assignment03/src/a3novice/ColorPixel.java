package a3novice;

public class ColorPixel implements Pixel {
	
	protected double red;
	protected double green;
	protected double blue;
	private final static double EPSILON = 0.00001;

	public ColorPixel(double red, double green, double blue) {
		//TODO: Epsilon comparison
		if (red < 0 || green < 0 || blue < 0 || 
				red > 1 || green > 1 || blue > 1) {
			throw new RuntimeException("Color value out of bounds");
		}
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public double getRed() {
		return this.red;
	}
	
	public double getGreen() {
		return this.green;
	}
	
	public double getBlue() {
		return this.blue;
	}
	
	public double getIntensity() {
		return 0.2126 * this.red + 0.7152 * this.green + 0.0722 * this.blue;
	}

}
