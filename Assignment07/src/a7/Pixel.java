package a7;

public interface Pixel {
	double getRed();
	double getGreen();
	double getBlue();
	double getIntensity();
	Pixel lightenOrDarken(double factor);
    Pixel saturate(double factor);
}
