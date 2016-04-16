package a4novice.test;

import a4novice.Pixel;
import a4novice.GrayPixel;

import static org.junit.Assert.*;

import org.junit.Test;

public class GrayPixelTest {
	private static double COLOR_DELTA = 0.001;

	@Test
	public void testGetters() {
		Pixel pixel = new GrayPixel(0.5);
		assertEquals(0.5, pixel.getRed(), COLOR_DELTA);
		assertEquals(0.5, pixel.getGreen(), COLOR_DELTA);
		assertEquals(0.5, pixel.getBlue(), COLOR_DELTA);
		assertEquals(0.5, pixel.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testBoundsCheck() {
		double[] testIntensity = { -1d, 2d };
		boolean[] failures = new boolean[testIntensity.length];

		for (int i = 0; i < testIntensity.length; i++) {
			try {
				new GrayPixel(testIntensity[i]);
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
		Pixel pixel = new GrayPixel(0.5);
		assertEquals(0.5, pixel.getIntensity(), COLOR_DELTA);

		Pixel lighter = pixel.lighten(0.5);
		assertEquals(0.75, lighter.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testDarken() {
		Pixel pixel = new GrayPixel(0.5);
		assertEquals(0.5, pixel.getIntensity(), COLOR_DELTA);

		Pixel darker = pixel.darken(0.5);
		assertEquals(0.25, darker.getIntensity(), COLOR_DELTA);
	}

	@Test
	public void testBadLighten() {
		double[] testIntensity = { -1d, 2d };
		boolean[] failures = new boolean[testIntensity.length];

		Pixel pixel = new GrayPixel(0.5);
		for (int i = 0; i < testIntensity.length; i++) {
			try {
				pixel.lighten(testIntensity[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadDarken() {
		double[] testIntensity = { -1d, 2d };
		boolean[] failures = new boolean[testIntensity.length];

		Pixel pixel = new GrayPixel(0.5);
		for (int i = 0; i < testIntensity.length; i++) {
			try {
				pixel.darken(testIntensity[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}
}
