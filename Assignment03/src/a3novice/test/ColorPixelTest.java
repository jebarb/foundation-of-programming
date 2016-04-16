package a3novice.test;

import a3novice.Pixel;
import a3novice.ColorPixel;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColorPixelTest {
	private static double COLOR_DELTA = 0.001;

	@Test
	public void testGetters() {
		Pixel pixel = new ColorPixel(0, 0.3, 0.6);
		assertEquals(0d, pixel.getRed(), COLOR_DELTA);
		assertEquals(0.3d, pixel.getGreen(), COLOR_DELTA);
		assertEquals(0.6d, pixel.getBlue(), COLOR_DELTA);

		// intensity = 0.2126 * red + 0.7152 * green + 0.0722 * blue
		assertEquals(0.25788, pixel.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testBoundsCheck() {
		double[][] testColors = {
				// Red too high and red too low
				{ 2d, 0d, 0d }, { -1d, 0d, 0d },

				// Green too high and green too low
				{ 0d, 2d, 0d }, { 0d, -1d, 0d },

				// Blue too high and blue too low
				{ 0d, 0d, 2d }, { 0d, 0d, -1d } };

		boolean[] failures = new boolean[testColors.length];

		for (int i = 0; i < testColors.length; i++) {
			try {
				double[] testDatum = testColors[i];
				new ColorPixel(testDatum[0], testDatum[1], testDatum[2]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}
}
