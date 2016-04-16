package a3jedi.test;

import a3jedi.Pixel;
import a3jedi.ColorPixel;
import a3jedi.GrayPixel;
import a3jedi.Frame2D;
import a3jedi.GrayFrame2D;
import a3jedi.ImmutableFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImmutableFrame2DTest {
	private static double COLOR_DELTA = 0.001;
	private static Pixel BLACK = new ColorPixel(0d, 0d, 0d);

	/**
	 * A helper to check to assert equality between two pixels.
	 */
	private void checkPixelEquality(Pixel target, Pixel actual) {
		assertEquals(target.getRed(), actual.getRed(), COLOR_DELTA);
		assertEquals(target.getGreen(), actual.getGreen(), COLOR_DELTA);
		assertEquals(target.getBlue(), actual.getBlue(), COLOR_DELTA);
	}

	/**
	 * A helper to see if one pixel is the gray version of another.
	 */
	private void checkGrayEquality(Pixel gray, Pixel color) {
		assertEquals(color.getIntensity(), gray.getRed(), COLOR_DELTA);
		assertEquals(color.getIntensity(), gray.getGreen(), COLOR_DELTA);
		assertEquals(color.getIntensity(), gray.getBlue(), COLOR_DELTA);
	}

	@Test
	public void testDefaultGetters() {
		Frame2D frame = new ImmutableFrame2D(1, 2,
				new Pixel[] { BLACK, BLACK });
		
		assertEquals(1, frame.getWidth());
		assertEquals(2, frame.getHeight());

		for (int x = 0; x < 1; x++) {
			for (int y = 0; y < 2; y++) {
				checkPixelEquality(BLACK, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testBadConstructorDims() {
		int[][] testDimensions = { { -1, 1 }, // Negative width
				{ 1, -1 }, // Negative height,
				{ 0, 1 }, // Zero width
				{ 1, 0 }, // Zero height
		};

		// The contents of this shouldn't matter, since the constrcutor should
		// check the input width and height before it checks the input array
		Pixel[] pixels = {};

		boolean[] failures = new boolean[testDimensions.length];

		for (int i = 0; i < testDimensions.length; i++) {
			int dimensions[] = testDimensions[i];
			try {
				new ImmutableFrame2D(dimensions[0], dimensions[1], pixels);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testArrayConstructor() {
		// Ensure that the array constructor processes its pixels in the right
		// order
		Pixel[] pixels = { new ColorPixel(0d, 0d, 0d),
				new ColorPixel(0.1, 0.1, 0.1), new ColorPixel(0.2, 0.2, 0.2),
				new ColorPixel(0.3, 0.3, 0.3) };

		Frame2D frame = new ImmutableFrame2D(2, 2, pixels);
		checkPixelEquality(pixels[0], frame.getPixel(0, 0));
		checkPixelEquality(pixels[1], frame.getPixel(1, 0));
		checkPixelEquality(pixels[2], frame.getPixel(0, 1));
		checkPixelEquality(pixels[3], frame.getPixel(1, 1));
	}

	@Test
	public void testBadArrayConstructor() {
		// Ensure that invalid arrays (bad dimensions, containing null, etc.)
		// throw exceptions
		Pixel[][] testPixelArrays = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.1, 0.1, 0.1),
						new ColorPixel(0.2, 0.2, 0.2) }, // Too few

				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.1, 0.1, 0.1),
						new ColorPixel(0.2, 0.2, 0.2),
						new ColorPixel(0.3, 0.3, 0.3),
						new ColorPixel(0.4, 0.4, 0.4) }, // Too many

				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.1, 0.1, 0.1),
						new ColorPixel(0.2, 0.2, 0.2), null }, // Containing
																// null

				null // null overall
		};

		boolean[] failures = new boolean[testPixelArrays.length];

		for (int i = 0; i < testPixelArrays.length; i++) {
			Pixel[] pixels = testPixelArrays[i];
			try {
				new ImmutableFrame2D(2, 2, pixels);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadGetPixel() {
		int[][] testCoords = { { -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		Frame2D frame = new ImmutableFrame2D(2, 2, 
				new Pixel[] { BLACK, BLACK, BLACK, BLACK });

		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				frame.getPixel(coords[0], coords[1]);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadSetPixel() {
		int[][] testCoords = { { -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		Pixel okPixel = new ColorPixel(0d, 0.3, 0.6);
		Frame2D frame = new ImmutableFrame2D(2, 2,
				new Pixel[] { BLACK, BLACK, BLACK, BLACK });
		
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				frame.setPixel(coords[0], coords[1], okPixel);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));

		// In addition to invalid position tests, ensure that null pixels
		// throw, and do not change the pixel in the original frame (only
		// a concern since we're using ImmutableFrame2D)
		boolean badPixelThrows = false;

		try {
			frame.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		checkPixelEquality(frame.getPixel(0, 0), BLACK);
	}

	@Test
	public void testLighten() {
		Pixel[] flatPixels = {
				new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3),
				new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) };

		// Lighter by a scale of 50%
		Pixel[][] lighterPixels = {
				{ new ColorPixel(0.5, 0.5, 0.5), new ColorPixel(0.65, 0.65, 0.65) },
				{ new ColorPixel(0.8, 0.8, 0.8), new ColorPixel(0.95, 0.95, 0.95) } };

		Frame2D frame = new ImmutableFrame2D(2, 2, flatPixels);

		Frame2D lightFrame = frame.lighten(0.5);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				checkPixelEquality(lightFrame.getPixel(x, y),
						lighterPixels[y][x]);
			}
		}

		// Ensure the original frame was unchanged
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Pixel sourcePixel = flatPixels[x + y * frame.getWidth()];
				checkPixelEquality(sourcePixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testDarken() {
		Pixel[] flatPixels = { 
				new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3), 
				new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) };

		// Darker by a scale of 50%
		Pixel[][] darkerPixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.15, 0.15, 0.15) },
				{ new ColorPixel(0.3, 0.3, 0.3), new ColorPixel(0.45, 0.45, 0.45) } };

		Frame2D frame = new ImmutableFrame2D(2, 2, flatPixels);
		Frame2D darkFrame = frame.darken(0.5);

		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				checkPixelEquality(darkFrame.getPixel(x, y), darkerPixels[y][x]);
			}
		}

		// Ensure the original frame was unchanged
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Pixel sourcePixel = flatPixels[x + y * frame.getWidth()];
				checkPixelEquality(sourcePixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D frame = new ImmutableFrame2D(1, 1, new Pixel[] { BLACK });

		for (int i = 0; i < intensityValues.length; i++) {
			try {
				frame.lighten(intensityValues[i]);
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

		Frame2D frame = new ImmutableFrame2D(1, 1, new Pixel[] { BLACK });
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				frame.darken(intensityValues[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testToGrayFrame() {
		Pixel[] flatColorPixels = {
				new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3),
				new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) };

		Pixel[][] grayPixels = { 
				{ new GrayPixel(0d), new GrayPixel(0.3) },
				{ new GrayPixel(0.6), new GrayPixel(0.9) } };

		Frame2D frame = new ImmutableFrame2D(2, 2, flatColorPixels);

		GrayFrame2D grayFrame = frame.toGrayFrame();
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				checkGrayEquality(grayPixels[y][x], grayFrame.getPixel(x, y));
			}
		}

		// Ensure that the original frame was unchanged
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Pixel sourcePixel = flatColorPixels[x + y * frame.getWidth() ];
				checkPixelEquality(sourcePixel, frame.getPixel(x, y));
			}
		}
	}
}
