package a4jedi;

public class ColorPixel implements Pixel {

	private double red;
	private double green;
	private double blue;
	
	public ColorPixel(double r, double g, double b) {
		if (r < 0.0 || r > 1.0 || g < 0.0 || g > 1.0 || b < 0.0 || b > 1.0) {
			throw new RuntimeException("One or more color components out of range");
		}
		
		red = r;
		green = g;
		blue = b;
	}
	
	@Override
	public double getRed() {
		return red;
	}

	@Override
	public double getGreen() {
		return green;
	}

	@Override
	public double getBlue() {
		return blue;
	}

	@Override
	public double getIntensity() {
		return (0.2126 * getRed() + 0.7152 * getGreen() + 0.0722 * getBlue());
	}

	@Override
	public Pixel lighten(double factor) {
		check_factor(factor);
		return new ColorPixel(lighten_component(getRed(), factor),
					     lighten_component(getGreen(), factor),
					     lighten_component(getBlue(), factor));
	}

	@Override
	public Pixel darken(double factor) {
		check_factor(factor);
		return new ColorPixel(darken_component(getRed(), factor),
					     darken_component(getGreen(), factor),
					     darken_component(getBlue(), factor));
	}

	private static double lighten_component(double current, double factor) {
		return (current * (1.0 - factor) + factor);
	}

	private static double darken_component(double current, double factor) {
		return (current * (1.0 - factor));
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
