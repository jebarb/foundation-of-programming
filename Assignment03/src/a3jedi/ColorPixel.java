package a3jedi;

import java.text.DecimalFormat;
import java.math.RoundingMode;

public class ColorPixel implements Pixel {
	
	private double red;
	private double green;
	private double blue;

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
	
	public Pixel lighten(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		DecimalFormat d1 = new DecimalFormat("#.#####");
		d1.setRoundingMode(RoundingMode.DOWN);
		Pixel pix = new ColorPixel(Double.parseDouble(d1.format(this.red * (1.0 - factor) + factor)),
				Double.parseDouble(d1.format(this.green * (1.0 - factor) + factor)),
				Double.parseDouble(d1.format(this.blue * (1.0 - factor) + factor)));
		return pix;	
	}
	
	public Pixel darken(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		DecimalFormat d1 = new DecimalFormat("#.#####");
		d1.setRoundingMode(RoundingMode.DOWN);
		Pixel pix = new ColorPixel(Double.parseDouble(d1.format(this.red  * (1.0 - factor))),
				Double.parseDouble(d1.format(this.green * (1.0 - factor))),
				Double.parseDouble(d1.format(this.blue * (1.0 - factor))));
		return pix;		
	}

}
