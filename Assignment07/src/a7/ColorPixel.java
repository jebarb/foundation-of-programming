package a7;


public class ColorPixel implements Pixel {

	private double red;
	private double green;
	private double blue;
	
	public ColorPixel(double r, double g, double b) {
		if (r < 0.0 || r > 1.0 || g < 0.0 || g > 1.0 || b < 0.0 || b > 1.0) {
			throw new IllegalArgumentException("One or more color components out of range");
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
    public Pixel lightenOrDarken(double factor) {
        check_factor(factor);
        if (factor > 0) {
            return new ColorPixel(lighten_component(getRed(), factor),
                    lighten_component(getGreen(), factor),
                    lighten_component(getBlue(), factor));
        } else if (factor < 0) {
            return new ColorPixel(darken_component(getRed(), factor),
                    darken_component(getGreen(), factor),
                    darken_component(getBlue(), factor));
        } else {
            return this;
        }
    }

    private static double lighten_component(double current, double factor) {
        return (current * (1.0 - factor) + factor);
    }

    private static double darken_component(double current, double factor) {
        return (current * (1.0 + factor));
    }

    @Override
    public Pixel saturate(double factor) {
        check_factor(factor);
        if (factor > 0) {
            return this.saturate_positive(factor);
        } else if (factor < 0) {
            return this.saturate_negative(factor);
        } else {
            return this;
        }
    }

    private Pixel saturate_negative(double factor) {
        return new ColorPixel(saturate_component_negative(this.getRed(), this.getIntensity(), factor),
                saturate_component_negative(this.getGreen(), this.getIntensity(), factor),
                saturate_component_negative(this.getBlue(), this.getIntensity(), factor));
    }

    private static double saturate_component_negative(double current, double intensity, double factor) {
        return current*(1.0+factor)-intensity*factor;
    }

    private Pixel saturate_positive(double factor) {
        double max = Math.max(this.getBlue(), Math.max(this.getRed(), this.getGreen()));
        double gain = (max+((1.0-max)*factor))/max;
        return new ColorPixel(getRed()*gain, getGreen()*gain, getBlue()*gain);
    }



    private static void check_factor(double factor) {
        if (factor < -1.0) {
            throw new RuntimeException("Factor can not be less than -1");
        } else if (factor > 1.0) {
            throw new RuntimeException("Factor can not be greater than 1");
        }
    }

}
