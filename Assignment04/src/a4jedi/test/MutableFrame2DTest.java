package a4jedi.test;

import a4jedi.Pixel;
import a4jedi.ColorPixel;
import a4jedi.GrayPixel;
import a4jedi.Frame2D;
import a4jedi.GrayFrame2D;
import a4jedi.MutableFrame2D;

import a4jedi.IllegalFrame2DGeometryException;
import a4jedi.IndirectFrame2D;
import a4jedi.AnyFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class MutableFrame2DTest {
	@Test
	public void testDefaultGetters() {
		Frame2D frame = new MutableFrame2D(1, 2);
		Pixel blackPixel = new ColorPixel(0d, 0d, 0d);

		assertEquals(1, frame.getWidth());
		assertEquals(2, frame.getHeight());

		for (int x = 0; x < 1; x++) {
			for (int y = 0; y < 2; y++) {
				Utils.checkPixelEquality(blackPixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testMutableGetters() {
		Frame2D frame = new MutableFrame2D(1, 2);
		Pixel assign = new ColorPixel(0d, 0.3, 0.6);

		Frame2D subframe = frame.setPixel(0, 1, assign);
		Utils.checkPixelEquality(subframe.getPixel(0, 1), assign);
	}

	@Test
	public void testBadConstructor() {
		int[][] testDimensions = { { -1, 1 }, // Negative width
				{ 1, -1 }, // Negative height,
				{ 0, 1 }, // Zero width
				{ 1, 0 }, // Zero height
		};
		boolean[] failures = new boolean[testDimensions.length];

		for (int i = 0; i < testDimensions.length; i++) {
			int dimensions[] = testDimensions[i];
			try {
				new MutableFrame2D(dimensions[0], dimensions[1]);
			} catch (IllegalFrame2DGeometryException exn) {
				assertEquals(exn.getMessage(), "Illegal geometry");
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

		Frame2D frame = new MutableFrame2D(2, 2);
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
		Frame2D frame = new MutableFrame2D(2, 2);
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
		Utils.checkPixelEquality(frame.getPixel(0, 0), new ColorPixel(0d, 0d, 0d));
	}

	@Test
	public void testLighten() {
		Pixel[][] pixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3) },
				{ new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) } };

		// Lighter by a scale of 50%
		Pixel[][] lighterPixels = {
				{ new ColorPixel(0.5, 0.5, 0.5),
						new ColorPixel(0.65, 0.65, 0.65) },
				{ new ColorPixel(0.8, 0.8, 0.8),
						new ColorPixel(0.95, 0.95, 0.95) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, pixels[x][y]);
			}
		}

		Frame2D lightFrame = frame.lighten(0.5);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Utils.checkPixelEquality(lightFrame.getPixel(x, y),
						lighterPixels[x][y]);
			}
		}
	}

	@Test
	public void testDarken() {
		Pixel[][] pixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3) },
				{ new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) } };

		// Darker by a scale of 50%
		Pixel[][] darkerPixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.15, 0.15, 0.15) },
				{ new ColorPixel(0.3, 0.3, 0.3),
						new ColorPixel(0.45, 0.45, 0.45) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, pixels[x][y]);
			}
		}

		Frame2D darkFrame = frame.darken(0.5);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Utils.checkPixelEquality(darkFrame.getPixel(x, y), darkerPixels[x][y]);
			}
		}
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D frame = new MutableFrame2D(1, 1);
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

		Frame2D frame = new MutableFrame2D(1, 1);
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
		Pixel[][] colorPixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3) },
				{ new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) } };

		Pixel[][] grayPixels = { { new GrayPixel(0d), new GrayPixel(0.3) },
				{ new GrayPixel(0.6), new GrayPixel(0.9) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, colorPixels[x][y]);
			}
		}

		GrayFrame2D grayFrame = frame.toGrayFrame();
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Utils.checkGrayEquality(grayPixels[x][y], grayFrame.getPixel(x, y));
			}
		}
	}
	
	@Test
	public void testExtract() {
		Frame2D frame = new MutableFrame2D(5, 5);
		IndirectFrame2D iframe = frame.extract(1, 1, 3, 3);
		
		// Ensure that the indirect frame points to the source by twiddling
		// some pixels
		Pixel colored = new ColorPixel(0.3, 0.6, 0.9);
		iframe.setPixel(0, 0, colored);
		Utils.checkPixelEquality(frame.getPixel(1, 1), colored);
	}
	
	@Test
	public void testCastToAnyFrame() {
		Frame2D source = new MutableFrame2D(5, 5);
		
		// Eclipse doesn't like bare casts, so we have to shut it up
		AnyFrame2D thing = (AnyFrame2D)source;
		thing.getWidth();
	}
}
