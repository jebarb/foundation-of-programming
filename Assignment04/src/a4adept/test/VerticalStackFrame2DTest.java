package a4adept.test;

import a4adept.Pixel;
import a4adept.ColorPixel;
import a4adept.GrayPixel;
import a4adept.Frame2D;
import a4adept.GrayFrame2D;
import a4adept.MutableFrame2D;

import a4adept.IllegalFrame2DGeometryException; 
import a4adept.IndirectFrame2D;
import a4adept.VerticalStackFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class VerticalStackFrame2DTest {
	@Test
	public void testDefaultGetters() {
		Frame2D top = new MutableFrame2D(2, 3);
		Frame2D bottom = new MutableFrame2D(2, 5);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);

		assertEquals(2, stacked.getWidth());
		assertEquals(8, stacked.getHeight());
		
		Utils.checkPixelEquality(stacked.getPixel(0, 0), top.getPixel(0, 0));
		Utils.checkPixelEquality(stacked.getPixel(0, 3), bottom.getPixel(0, 0));
	}

	@Test
	public void testMutableGetters() {
		Frame2D top = new MutableFrame2D(2, 3);
		Frame2D bottom = new MutableFrame2D(2, 5);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		
		Pixel assign = new ColorPixel(0d, 0.3, 0.6);
		
		Frame2D substacked = stacked.setPixel(0,  0, assign);
		Utils.checkPixelEquality(substacked.getPixel(0, 0), assign);
		Utils.checkPixelEquality(top.getPixel(0, 0), assign);
		
		substacked = substacked.setPixel(0, 3, assign);
		Utils.checkPixelEquality(substacked.getPixel(0, 3), assign);
		Utils.checkPixelEquality(bottom.getPixel(0, 0), assign);
	}
	
	@Test
	public void testBadConstructor() {
		Frame2D frame2x2 = new MutableFrame2D(2, 2);
		Frame2D frame3x3 = new MutableFrame2D(3, 3);
		
		// 3 = 2 null tests + 1 incompat. dimensions tests
		boolean[] failures = new boolean[3];
		
		try {
			new VerticalStackFrame2D(null, frame2x2);
		} catch (IllegalArgumentException exn) {
			failures[0] = true;
		}
		
		try {
			new VerticalStackFrame2D(frame2x2, null);
		} catch (IllegalArgumentException exn) {
			failures[1] = true;
		}

		try {
			new VerticalStackFrame2D(frame2x2, frame3x3);
		} catch (IllegalFrame2DGeometryException exn) {
			failures[2] = true;
		}

		assertTrue(Utils.all(failures));
	}
	
	@Test
	public void testBadGetPixel() {
		int[][] testCoords = { 
				{ -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		Frame2D top = new MutableFrame2D(2, 3);
		Frame2D bottom = new MutableFrame2D(2, 5);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				stacked.getPixel(coords[0], coords[1]);
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
		Frame2D top = new MutableFrame2D(2, 3);
		Frame2D bottom = new MutableFrame2D(2, 5);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				stacked.setPixel(coords[0], coords[1], okPixel);
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
			stacked.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		Utils.checkPixelEquality(stacked.getPixel(0, 0), new ColorPixel(0d, 0d, 0d));
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

		// The top and bottom have to add up to 4 tiles, since that
		// is the size of the pixel array
		Frame2D top = new MutableFrame2D(2, 1);
		Frame2D bottom = new MutableFrame2D(2, 1);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
				stacked = stacked.setPixel(x, y, pixels[x][y]);
			}
		}

		Frame2D lightFrame = stacked.lighten(0.5);
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
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

		Frame2D top = new MutableFrame2D(2, 1);
		Frame2D bottom = new MutableFrame2D(2, 1);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
				stacked = stacked.setPixel(x, y, pixels[x][y]);
			}
		}

		Frame2D darkFrame = stacked.darken(0.5);
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
				Utils.checkPixelEquality(darkFrame.getPixel(x, y), darkerPixels[x][y]);
			}
		}
	}

	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D top = new MutableFrame2D(1, 2);
		Frame2D bottom = new MutableFrame2D(1, 2);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				stacked.lighten(intensityValues[i]);
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

		Frame2D top = new MutableFrame2D(1, 2);
		Frame2D bottom = new MutableFrame2D(1, 2);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				stacked.darken(intensityValues[i]);
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

		Frame2D top = new MutableFrame2D(2, 1);
		Frame2D bottom = new MutableFrame2D(2, 1);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
				stacked = stacked.setPixel(x, y, colorPixels[x][y]);
			}
		}

		GrayFrame2D grayFrame = stacked.toGrayFrame();
		for (int x = 0; x < stacked.getWidth(); x++) {
			for (int y = 0; y < stacked.getHeight(); y++) {
				Utils.checkGrayEquality(grayPixels[x][y], grayFrame.getPixel(x, y));
			}
		}
	}
	
	@Test
	public void testExtract() {
		Frame2D top = new MutableFrame2D(5, 5);
		Frame2D bottom = new MutableFrame2D(5, 5);
		Frame2D stacked = new VerticalStackFrame2D(top, bottom);
		IndirectFrame2D istacked = stacked.extract(1, 1, 3, 8);
		
		// Ensure that the indirect stacked points to the source by twiddling
		// some pixels
		Pixel colored = new ColorPixel(0.3, 0.6, 0.9);
		istacked.setPixel(0, 0, colored);
		Utils.checkPixelEquality(stacked.getPixel(1, 1), colored);
	}
}
