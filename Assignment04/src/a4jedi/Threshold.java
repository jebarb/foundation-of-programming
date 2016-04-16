package a4jedi;

public class Threshold implements PixelTransformation {
	
	private double threshold;
	
	public Threshold (double threshold) {
		this.threshold = threshold;
	}

	@Override
	public Pixel transform(Pixel p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		Pixel newPix;
		if (p.getIntensity() > threshold) {
			newPix = new ColorPixel(1, 1, 1);
		} else {
			newPix = new ColorPixel(0, 0, 0);
		}
		return newPix;
	}

}
