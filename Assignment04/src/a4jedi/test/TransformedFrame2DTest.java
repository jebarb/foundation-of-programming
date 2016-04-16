package a4jedi.test;

import a4jedi.Pixel;
import a4jedi.ColorPixel;
import a4jedi.GrayPixel;
import a4jedi.Frame2D;
import a4jedi.GrayFrame2D;
import a4jedi.MutableFrame2D;

import a4jedi.IndirectFrame2D;
import a4jedi.AnyFrame2D;
import a4jedi.TransformedFrame2D;
import a4jedi.PixelTransformation;
import a4jedi.Threshold;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransformedFrame2DTest {
	// The transforms are tested elsewhere, so we don't need to use any more
	// than one here
	private static PixelTransformation TRANSFORM = new Threshold(0.5);
	private static Pixel BLACK = new ColorPixel(0d, 0d, 0d);
	
	@Test
	public void testDefaultGetters() {
		Frame2D frame = new MutableFrame2D(1, 2);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);

		assertEquals(1, transformed.getWidth());
		assertEquals(2, transformed.getHeight());

		for (int x = 0; x < 1; x++) {
			for (int y = 0; y < 2; y++) {
				Utils.checkPixelEquality(BLACK, transformed.getPixel(x, y));
			}
		}
	}

	@Test
	public void testTransformedGetters() {
		Frame2D frame = new MutableFrame2D(1, 2);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);

		// Pixels changed in the frame below should bubble back up to the 
		// transformed frame
		frame.setPixel(0, 0, new GrayPixel(0.3d));
		Utils.checkPixelEquality(transformed.getPixel(0, 0), new GrayPixel(0d));
		
		frame.setPixel(0, 1, new GrayPixel(0.6d));
		Utils.checkPixelEquality(transformed.getPixel(0, 1), new GrayPixel(1d));
		
		// Equal intensities are rounded down
		frame.setPixel(0, 1, new GrayPixel(0.5d));
		Utils.checkPixelEquality(transformed.getPixel(0, 1), new GrayPixel(0d));
	}
	
	@Test
	public void testMutableGetters() {
		Frame2D frame = new MutableFrame2D(1, 2);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		
		// Pixels changed in the transformed frame itself should remain 
		// untransformed
		transformed = transformed.setPixel(0, 0, new GrayPixel(0.5d));
		Utils.checkPixelEquality(transformed.getPixel(0, 0), new GrayPixel(0.5d));
	}

	@Test
	public void testBadConstructor() {
		boolean nullFrameFails = false;
		try {
			new TransformedFrame2D(null, TRANSFORM);
		} catch (IllegalArgumentException exn) {
			nullFrameFails = true;
		}
		
		boolean nullXFormFails = false;
		try {
			new TransformedFrame2D(new MutableFrame2D(1, 2), null);
		} catch (IllegalArgumentException exn) {
			nullXFormFails = true;
		}
		
		assertTrue(nullFrameFails);
		assertTrue(nullXFormFails);
	}

	@Test
	public void testBadGetPixel() {
		int[][] testCoords = { { -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		Frame2D frame = new MutableFrame2D(1, 2);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				transformed.getPixel(coords[0], coords[1]);
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
		Frame2D frame = new MutableFrame2D(1, 2);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				transformed.setPixel(coords[0], coords[1], okPixel);
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
			transformed.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		Utils.checkPixelEquality(transformed.getPixel(0, 0), new ColorPixel(0d, 0d, 0d));
	}

	@Test
	public void testLighten() {
		Pixel[][] pixels = {
				{ new GrayPixel(0d), new GrayPixel(0.2) },
				{ new GrayPixel(0.4), new GrayPixel(0.6) } };

		// Lighter by a scale of 50%, and transformed by Threshold
		Pixel[][] lighterPixels = {
				{ new GrayPixel(0d), new GrayPixel(1d) },
				{ new GrayPixel(1d), new GrayPixel(1d) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, pixels[x][y]);
			}
		}
		
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		Frame2D lightFrame = transformed.lighten(0.5);
		
		for (int x = 0; x < transformed.getWidth(); x++) {
			for (int y = 0; y < transformed.getHeight(); y++) {
				Utils.checkGrayEquality(lightFrame.getPixel(x, y),
						lighterPixels[x][y]);
			}
		}
	}

	@Test
	public void testDarken() {
		Pixel[][] pixels = {
				{ new GrayPixel(0d), new GrayPixel(0.2) },
				{ new GrayPixel(0.4), new GrayPixel(0.6) } };

		// Darker by a scale of 50%, and transformed by Threshold
		Pixel[][] darkerPixels = {
				{ new GrayPixel(0d), new GrayPixel(0d) },
				{ new GrayPixel(0d), new GrayPixel(0d) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, pixels[x][y]);
			}
		}
		
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		Frame2D darkFrame = transformed.darken(0.5);
		for (int x = 0; x < transformed.getWidth(); x++) {
			for (int y = 0; y < transformed.getHeight(); y++) {
				Utils.checkGrayEquality(darkFrame.getPixel(x, y), darkerPixels[x][y]);
			}
		}
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D frame = new MutableFrame2D(1, 1);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				transformed.lighten(intensityValues[i]);
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
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				transformed.darken(intensityValues[i]);
			} catch (RuntimeException ex) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testToGrayFrame() {
		Pixel[][] colorPixels = {
				{ new GrayPixel(0d), new GrayPixel(0.3) },
				{ new GrayPixel(0.5), new GrayPixel(0.9) } };

		Pixel[][] grayPixels = { 
				{ new GrayPixel(0d), new GrayPixel(0d) },
				{ new GrayPixel(0d), new GrayPixel(1d) } };

		Frame2D frame = new MutableFrame2D(2, 2);
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				frame = frame.setPixel(x, y, colorPixels[x][y]);
			}
		}

		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		GrayFrame2D grayFrame = transformed.toGrayFrame();
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				Utils.checkGrayEquality(grayPixels[x][y], grayFrame.getPixel(x, y));
			}
		}
	}
	
	@Test
	public void testExtract() {
		Frame2D frame = new MutableFrame2D(5, 5);
		Frame2D transformed = new TransformedFrame2D(frame, TRANSFORM);
		IndirectFrame2D iframe = transformed.extract(1, 1, 3, 3);
		
		// Ensure that the indirect frame points to the source by twiddling
		// some pixels in the bottom-most frame (not iframe, not transformed,
		// but frame) and seeing if they bubble up
		Pixel newpixel = new GrayPixel(0.6d);
		frame.setPixel(1, 1, newpixel);
		Utils.checkPixelEquality(iframe.getPixel(0, 0), new GrayPixel(1d));
	}
	
	@Test
	public void testCastToAnyFrame() {
		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D transformed = new TransformedFrame2D(source, TRANSFORM);
		
		// Eclipse doesn't like bare casts, so we have to shut it up
		AnyFrame2D thing = (AnyFrame2D)transformed;
		thing.getWidth();
	}
}
