package a4jedi.test;

import a4jedi.Pixel;
import a4jedi.ColorPixel;

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
				// Red high and red low
				{ 2d, 0d, 0d }, { -1d, 0d, 0d },

				// Green high and green low
				{ 0d, 2d, 0d }, { 0d, -1d, 0d },

				// Blue high and blue low
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

	/*
	 * The formula for lightening and darkening is relative.
	 * 
	 * Thus:
	 * 
	 * Pixel p; p.getIntensity(); // 0.5 p.lighten(0.5).getIntensity(); // 0.75
	 * p.darken(0.5).getIntensity(); // 0.25
	 */

	@Test
	public void testLighten() {
		Pixel pixel = new ColorPixel(0, 0.3, 0.6);

		// intensity = 0.2126 * red + 0.7152 * green + 0.0722 * blue
		assertEquals(0.25788, pixel.getIntensity(), COLOR_DELTA);

		Pixel lighter = pixel.lighten(0.5);
		assertEquals(0.62894, lighter.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testDarken() {
		Pixel pixel = new ColorPixel(0, 0.3, 0.6);

		// intensity = 0.2126 * red + 0.7152 * green + 0.0722 * blue
		assertEquals(0.25788, pixel.getIntensity(), COLOR_DELTA);

		Pixel darker = pixel.darken(0.5);
		assertEquals(0.12894, darker.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Pixel pixel = new ColorPixel(0d, 0.3, 0.6);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				pixel.lighten(intensityValues[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadDarken() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Pixel pixel = new ColorPixel(0d, 0.3, 0.6);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				pixel.darken(intensityValues[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}
}
