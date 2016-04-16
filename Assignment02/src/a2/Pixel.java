package a2;

public class Pixel {
	static private char[] char_map = {'#', 'E', 'X', 'O', '\\', '/', '+', '-', '|', ' '};
	static final int MAX_VALUE = 99;
	
	private int intensity;
	
	// Constructor
	// val: intensity value for the new Pixel object
	public Pixel(int val){
		intensity = (val < 0) ? 0 : ((val > MAX_VALUE) ? MAX_VALUE : val);
	}
	
	// getIntensity returns the intensity value of the Pixel
	public int getIntensity() {
		return intensity;
	}

	// asChar returns the appropriate character associated with the
	// Pixel's intensity value
	public char asChar() {
		return char_map[getIntensity()/10];
	}
}
