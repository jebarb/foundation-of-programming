package a4adept.test;
import a4adept.Pixel;

import static org.junit.Assert.*;

public class Utils {
	static double COLOR_DELTA = 0.001d;
	
	/**
	 * Returns true only if all inputs are also true.
	 * 
	 * Note: this is vacuously true if the input array is empty
	 */
	public static boolean all(boolean[] values) {
		// This short-circuits over the values - the first false value returns
		// false overall
		for (int i = 0; i < values.length; i++) {
			if (!values[i])
				return false;
		}
		return true;
	}
	
	/**
	 * A helper to check to assert equality between two pixels.
	 */
	public static void checkPixelEquality(Pixel target, Pixel actual) {
		assertEquals(target.getRed(), actual.getRed(), COLOR_DELTA);
		assertEquals(target.getGreen(), actual.getGreen(), COLOR_DELTA);
		assertEquals(target.getBlue(), actual.getBlue(), COLOR_DELTA);
	}

	/**
	 * A helper to see if one pixel is the gray version of another.
	 */
	public static void checkGrayEquality(Pixel gray, Pixel color) {
		assertEquals(color.getIntensity(), gray.getRed(), COLOR_DELTA);
		assertEquals(color.getIntensity(), gray.getGreen(), COLOR_DELTA);
		assertEquals(color.getIntensity(), gray.getBlue(), COLOR_DELTA);
	}

}
