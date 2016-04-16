package a4novice;

public class GrayPixel implements Pixel {

	double intensity;
	
	public GrayPixel(double intensity) {
		if (intensity < 0 || intensity > 1.0) {
			throw new RuntimeException("GrayPixel intensity out of bounds");
		}
		this.intensity = intensity;
	}
	
	@Override
	public double getRed() {
		return intensity;
	}

	@Override
	public double getGreen() {
		return intensity;
	}

	@Override
	public double getBlue() {
		return intensity;
	}

	@Override
	public double getIntensity() {
		return intensity;
	}

	@Override
	public Pixel lighten(double factor) {
		check_factor(factor);
		return new GrayPixel((getIntensity() * (1.0 - factor) + factor));
	}

	@Override
	public Pixel darken(double factor) {
		check_factor(factor);
		return new GrayPixel(getIntensity() * (1.0 - factor));
	}
	
	private static boolean check_factor(double factor) {
		if (factor < 0) {
			throw new RuntimeException("Factor can not be less than 0.0");
		} else if (factor > 1.0) {
			throw new RuntimeException("Factor can not be greater than 1.0");
		}
		return true;
	}


}
