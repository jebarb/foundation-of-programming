package a4jedi;

public interface Pixel {
	double getRed();
	double getGreen();
	double getBlue();
	double getIntensity();
	Pixel lighten(double factor);
	Pixel darken(double factor);
}
