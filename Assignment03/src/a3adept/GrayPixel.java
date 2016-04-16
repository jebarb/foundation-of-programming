package a3adept;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GrayPixel implements Pixel {
	
	private double intensity;

	public GrayPixel(double intensity) {
		if (intensity < 0 || intensity > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		this.intensity = intensity;
	}
	
	public double getRed() {
		return this.intensity;
	}
	
	public double getGreen() {
		return this.intensity;
	}
	
	public double getBlue() {
		return this.intensity;
	}
	
	public double getIntensity() {
		return this.intensity;
	}
	
	public Pixel lighten(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		DecimalFormat d1 = new DecimalFormat("#.#####");
		d1.setRoundingMode(RoundingMode.DOWN);
		GrayPixel pix = new GrayPixel(Double.parseDouble(d1.format(this.getIntensity() * (1.0 - factor) + factor)));
		return pix;
	}
	
	public Pixel darken(double factor) {
		if (factor < 0 || factor > 1) {
			throw new RuntimeException("Intensity value out of bounds");
		}
		DecimalFormat d1 = new DecimalFormat("#.#####");
		d1.setRoundingMode(RoundingMode.DOWN);
		GrayPixel pix = new GrayPixel(Double.parseDouble(d1.format(this.getIntensity() * (1.0 - factor))));
		return pix;		
	}
	
}
