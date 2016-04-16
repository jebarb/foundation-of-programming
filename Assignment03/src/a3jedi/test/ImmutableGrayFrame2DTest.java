package a3jedi.test;

import a3jedi.Pixel;
import a3jedi.ColorPixel;
import a3jedi.GrayPixel;

import a3jedi.Frame2D;
import a3jedi.GrayFrame2D;
import a3jedi.ImmutableGrayFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImmutableGrayFrame2DTest {
	private static double COLOR_DELTA = 0.001;
	private static GrayPixel BLACK = new GrayPixel(0d);

	/**
	 * 
	 * A helper to see if one pixel is the gray version of another.
	 */
	private void checkGrayEquality(Pixel gray, Pixel color) {
		assertEquals(gray + " != " + color, color.getIntensity(),
				gray.getRed(), COLOR_DELTA);
		assertEquals(gray + " != " + color, color.getIntensity(),
				gray.getGreen(), COLOR_DELTA);
		assertEquals(gray + " != " + color, color.getIntensity(),
				gray.getBlue(), COLOR_DELTA);
	}

	@Test
	public void testDefaultGetters() {
		GrayFrame2D frame = new ImmutableGrayFrame2D(1, 2,
				new GrayPixel[] { BLACK, BLACK });
		
		assertEquals(1, frame.getWidth());
		assertEquals(2, frame.getHeight());

		for (int x = 0; x < 1; x++) {
			for (int y = 0; y < 2; y++) {
				checkGrayEquality(BLACK, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testImmutableColorGetters() {
		GrayFrame2D frame = new ImmutableGrayFrame2D(1, 2,
				new GrayPixel[] { BLACK, BLACK });
		Pixel assign = new ColorPixel(0d, 0.3, 0.6);

		Frame2D subframe = frame.setPixel(0, 1, assign);
		checkGrayEquality(subframe.getPixel(0, 1), assign);

		// Ensure that the old frame wasn't changed
		checkGrayEquality(BLACK, frame.getPixel(0, 1));
	}

	@Test
	public void testImmutableGrayGetters() {
		GrayFrame2D frame = new ImmutableGrayFrame2D(1, 2,
				new GrayPixel[] { BLACK, BLACK });
		Pixel assign = new GrayPixel(0.5);

		Frame2D subframe = frame.setPixel(0, 1, assign);
		checkGrayEquality(subframe.getPixel(0, 1), assign);

		// Ensure that the old frame wasn't changed
		checkGrayEquality(BLACK, frame.getPixel(0, 1));
	}

	@Test
	public void testGrayImmutableGrayGetters() {
		GrayFrame2D frame = new ImmutableGrayFrame2D(1, 2,
				new GrayPixel[] { BLACK, BLACK });
		GrayPixel assign = new GrayPixel(0.5);

		GrayFrame2D subframe = frame.setGrayPixel(0, 1, assign);
		checkGrayEquality(subframe.getPixel(0, 1), assign);
		checkGrayEquality(subframe.getGrayPixel(0, 1), assign);

		// Ensure that the original wasn't changedj
		checkGrayEquality(BLACK, frame.getPixel(0, 1));
		checkGrayEquality(BLACK, frame.getGrayPixel(0, 1));
	}

	@Test
	public void testBadConstructor() {
		int[][] testDimensions = { { -1, 1 }, // Negative width
				{ 1, -1 }, // Negative height,
				{ 0, 1 }, // Zero width
				{ 1, 0 }, // Zero height
		};
		// This shouldn't matter, since we're testing for bad widths and heights
		GrayPixel[] grayPixels = { };

		boolean[] failures = new boolean[testDimensions.length];

		for (int i = 0; i < testDimensions.length; i++) {
			int dimensions[] = testDimensions[i];
			try {
				new ImmutableGrayFrame2D(dimensions[0], dimensions[1], grayPixels);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}
	
	@Test
	public void testArrayConstructor() {
		GrayPixel[] pixels = { new GrayPixel(0d), new GrayPixel(0.1), 
				      		   new GrayPixel(0.2), new GrayPixel(0.3) };
		
		Frame2D frame = new ImmutableGrayFrame2D(2, 2, pixels);
		checkGrayEquality(pixels[0], frame.getPixel(0, 0));
		checkGrayEquality(pixels[1], frame.getPixel(1, 0));
		checkGrayEquality(pixels[2], frame.getPixel(0, 1));
		checkGrayEquality(pixels[3], frame.getPixel(1, 1));
	}

	@Test
	public void testBadArrayConstructor() {
		GrayPixel[][] testPixelArrays = {
				{ new GrayPixel(0d), new GrayPixel(0.1), 
				  new GrayPixel(0.2) }, // Too few
				  
				{ new GrayPixel(0d), new GrayPixel(0.1), 
				  new GrayPixel(0.2), new GrayPixel(0.3), 
				  new GrayPixel(0.4) }, // Too many
				  
				{ new GrayPixel(0d), new GrayPixel(0.1), 
				  new GrayPixel(0.2), null }, // Containing null
				  
				null // null overall
		};

		boolean[] failures = new boolean[testPixelArrays.length];

		for (int i = 0; i < testPixelArrays.length; i++) {
			GrayPixel[] pixels = testPixelArrays[i];
			try {
				new ImmutableGrayFrame2D(2, 2, pixels);
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

		Frame2D frame = new ImmutableGrayFrame2D(2, 2,
				new GrayPixel[] { BLACK, BLACK, BLACK, BLACK });
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
	public void testBadGetGrayPixel() {
		int[][] testCoords = { { -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		GrayFrame2D frame = new ImmutableGrayFrame2D(2, 2,
				new GrayPixel[] { BLACK, BLACK, BLACK, BLACK });
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				frame.getGrayPixel(coords[0], coords[1]);
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
		Frame2D frame = new ImmutableGrayFrame2D(2, 2,
				new GrayPixel[] { BLACK, BLACK, BLACK, BLACK });
		
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
		// a concern since we're using MutableFrame2D)
		boolean badPixelThrows = false;

		try {
			frame.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		checkGrayEquality(frame.getPixel(0, 0), BLACK);
	}

	@Test
	public void testBadSetGrayPixel() {
		int[][] testCoords = { { -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		GrayPixel okPixel = new GrayPixel(0.5d);
		GrayFrame2D frame = new ImmutableGrayFrame2D(2, 2,
				new GrayPixel[] { BLACK, BLACK, BLACK, BLACK });
		
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				frame.setGrayPixel(coords[0], coords[1], okPixel);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));

		// In addition to invalid position tests, ensure that null pixels
		// throw, and do not change the pixel in the original frame (only
		// a concern since we're using MutableFrame2D)
		boolean badPixelThrows = false;

		try {
			frame.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		checkGrayEquality(frame.getPixel(0, 0), new GrayPixel(0d));
	}

	@Test
	public void testLighten() {
		GrayPixel[] flatPixels = { 
				new GrayPixel(0d), new GrayPixel(0.3),
				new GrayPixel(0.6), new GrayPixel(0.9) };

		// Lighter by a scale of 50%
		Pixel[][] lighterPixels = {
				{ new GrayPixel(0.5), new GrayPixel(0.65) },
				{ new GrayPixel(0.8), new GrayPixel(0.95) } };

		Frame2D frame = new ImmutableGrayFrame2D(2, 2, flatPixels);

		Frame2D lightFrame = frame.lighten(0.5);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				checkGrayEquality(lightFrame.getPixel(x, y),
						lighterPixels[y][x]);
			}
		}

		// Ensure the original frame was unchanged
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GrayPixel sourcePixel = flatPixels[x + y * frame.getWidth()];
				checkGrayEquality(sourcePixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testDarken() {
		GrayPixel[] flatPixels = { 
				new GrayPixel(0d), new GrayPixel(0.3),
				new GrayPixel(0.6), new GrayPixel(0.9) };

		// Darker by a scale of 50%
		Pixel[][] darkerPixels = { { new GrayPixel(0d), new GrayPixel(0.15) },
				{ new GrayPixel(0.3), new GrayPixel(0.45) } };

		Frame2D frame = new ImmutableGrayFrame2D(2, 2, flatPixels);

		Frame2D darkFrame = frame.darken(0.5);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				checkGrayEquality(darkFrame.getPixel(x, y), darkerPixels[y][x]);
			}
		}

		// Ensure the original frame was unchanged
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GrayPixel sourcePixel = flatPixels[x + y * frame.getWidth()];
				checkGrayEquality(sourcePixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D frame = new ImmutableGrayFrame2D(1, 1,
				new GrayPixel[] { BLACK });
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

		Frame2D frame = new ImmutableGrayFrame2D(1, 1,
				new GrayPixel[] { BLACK });
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
		GrayPixel[] flatPixels = { 
				new GrayPixel(0d), new GrayPixel(0.3),
				new GrayPixel(0.6), new GrayPixel(0.9) };

		Frame2D frame = new ImmutableGrayFrame2D(2, 2, flatPixels);

		GrayFrame2D grayFrame = frame.toGrayFrame();
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GrayPixel sourcePixel = flatPixels[x + y * frame.getWidth()];
				checkGrayEquality(sourcePixel, grayFrame.getPixel(x, y));
			}
		}
	}
}
