package a4jedi.test;

import a4jedi.Pixel;
import a4jedi.ColorPixel;
import a4jedi.GrayPixel;
import a4jedi.Frame2D;
import a4jedi.GrayFrame2D;
import a4jedi.MutableFrame2D;
import a4jedi.IndirectFrame2D;
import a4jedi.IndirectFrame2DImpl;

import a4jedi.IllegalFrame2DGeometryException;
import a4jedi.AnyFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class IndirectFrame2DImplTest {
	private static Pixel BLACK = new ColorPixel(0d, 0d, 0d);
	
	@Test
	public void testGetters() {
		Frame2D source = new MutableFrame2D(5, 5);
		IndirectFrame2D iframe = new IndirectFrame2DImpl(source, 0, 1, 3, 3);
		
		assertEquals(iframe.getWidth(), 3);
		assertEquals(iframe.getHeight(), 3);
		
		Utils.checkPixelEquality(iframe.getPixel(0, 0), source.getPixel(0, 1));
		
		assertEquals(iframe.getSource(), source);
		assertEquals(iframe.getXOffset(), 0);
		assertEquals(iframe.getYOffset(), 1);
	}
	
	@Test
	public void testMutableGetters() {
		Frame2D source = new MutableFrame2D(5, 5);
		IndirectFrame2D iframe = new IndirectFrame2DImpl(source, 0, 1, 3, 3);
		
		Pixel color = new ColorPixel(0.3, 0.6, 0.9);
		Frame2D iframeSet = iframe.setPixel(0, 0, color);
		Utils.checkPixelEquality(iframeSet.getPixel(0, 0), color);
		Utils.checkPixelEquality(source.getPixel(0, 1), color);
		
		// Also, ensure that mutating the underlying frame is reflected in the
		// indirect frame
		source.setPixel(0, 2, color);
		Utils.checkPixelEquality(iframeSet.getPixel(0, 1), color);
	}
	
	/*
	 * testBadConstructor had to be split up, since there are several ways
	 * you could fail to construct an IndirectFrame2DImpl:
	 * 
	 * - null source
	 * - x/y coordinates that are outside of the source
	 * - width/height that extend beyond the boundaries of the source or
	 *   width/height which are non-positive
	 */
	
	@Test
	public void testBadConstructorSource() {
		boolean failed = false;
		
		try {
			new IndirectFrame2DImpl(null, 0, 0, 1, 1);
		} catch (IllegalArgumentException exn) {
			failed = true;
		}
		
		assertTrue(failed);
	}
	
	@Test
	public void testBadConstructorXY() {
		Frame2D source = new MutableFrame2D(5, 5);
		int[][] testXY = {
				{-1, 0}, // X bound to the left of the source
				{0, -1}, // Y bound above the source
				{10, 0}, // X bound to the right of the source
				{0, 10}, // Y bound to the right of the source
		};
		boolean[] failures = new boolean[testXY.length];
		
		for (int i = 0; i < testXY.length; i++) {
			int x = testXY[i][0];
			int y = testXY[i][1];
			try {
				new IndirectFrame2DImpl(source, x, y, 1, 1);
			} catch (IllegalFrame2DGeometryException exn) {
				assertEquals(exn.getMessage(), "Illegal geometry");
				failures[i] = true;
			}
		}
		
		assertTrue(Utils.all(failures));
	}
	
	@Test
	public void testBadConstructorDims() {
		Frame2D source = new MutableFrame2D(5, 5);
		int x = 1;
		int y = 1;
				
		int[][] testDims = {
				{0, 1}, // Too-small width
				{1, 0}, // Too-small height
				{10, 1}, // Width + x larger than the source
				{1, 10}, // Height + y larger than the source
		};
		
		boolean[] failures = new boolean[testDims.length];
		
		for (int i = 0; i < testDims.length; i++) {
			int width = testDims[i][0];
			int height = testDims[i][1];
			try {
				new IndirectFrame2DImpl(source, x, y, width, height);
			} catch (IllegalFrame2DGeometryException exn) {
				assertEquals(exn.getMessage(), "Illegal geometry");
				failures[i] = true;
			}
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

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 3, 3);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				iframe.getPixel(coords[0], coords[1]);
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadSetPixel() {
		int[][] testCoords = { 
				{ -1, 0 }, { 999, 0 }, // Low and high X
				{ 0, -1 }, { 0, 999 }, // Low and high Y
		};
		boolean[] failures = new boolean[testCoords.length];

		Pixel okPixel = new ColorPixel(0d, 0.3, 0.6);
		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 3, 3);
		for (int i = 0; i < testCoords.length; i++) {
			int coords[] = testCoords[i];
			try {
				iframe.setPixel(coords[0], coords[1], okPixel);
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
			iframe.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		Utils.checkPixelEquality(iframe.getPixel(0, 0), BLACK);
	}
	
	@Test
	public void testLighten() {
		Pixel[][] pixels = {
				{ new ColorPixel(0d, 0d, 0d), new ColorPixel(0.3, 0.3, 0.3) },
				{ new ColorPixel(0.6, 0.6, 0.6), new ColorPixel(0.9, 0.9, 0.9) } 
				};

		// Lighter by a scale of 50%
		Pixel[][] lighterPixels = {
				{ new ColorPixel(0.5, 0.5, 0.5),
						new ColorPixel(0.65, 0.65, 0.65) },
				{ new ColorPixel(0.8, 0.8, 0.8),
						new ColorPixel(0.95, 0.95, 0.95) } };

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				iframe = iframe.setPixel(x, y, pixels[x][y]);
			}
		}

		Frame2D lightFrame = iframe.lighten(0.5);
		for (int x = 0; x < iframe.getWidth(); x++) {
			
			for (int y = 0; y < iframe.getHeight(); y++) {
				Utils.checkPixelEquality(lightFrame.getPixel(x, y),
						lighterPixels[x][y]);
			}
		}
		
		// Ensure that the pixels in the source image were mutated
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				int absoluteX = x + 1;
				int absoluteY = y + 1;
				Utils.checkPixelEquality(source.getPixel(absoluteX, absoluteY),
						lighterPixels[x][y]);
			}
		}
		
		// Ensure that *only* those in the region were mutated
		Utils.checkPixelEquality(source.getPixel(0, 0), BLACK);
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

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				iframe = iframe.setPixel(x, y, pixels[x][y]);
				Utils.checkPixelEquality(iframe.getPixel(x, y), pixels[x][y]);
				Utils.checkPixelEquality(source.getPixel(x + 1, y + 1), pixels[x][y]);
			}
		}

		Frame2D darkFrame = iframe.darken(0.5);
		for (int x = 0; x < iframe.getWidth(); x++) {
			
			for (int y = 0; y < iframe.getHeight(); y++) {
				Utils.checkPixelEquality(darkFrame.getPixel(x, y),
						darkerPixels[x][y]);
			}
		}
		
		// Ensure that the pixels in the source image were mutated
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				int absoluteX = x + 1;
				int absoluteY = y + 1;
				Utils.checkPixelEquality(source.getPixel(absoluteX, absoluteY),
						darkerPixels[x][y]);
			}
		}
		
		// Ensure that *only* those in the region were mutated
		Utils.checkPixelEquality(source.getPixel(0, 0), BLACK);
	}
	
	@Test
	public void testBadLighten() {
		double[] intensityValues = { -1d, 2d };
		boolean[] failures = new boolean[intensityValues.length];

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				iframe.lighten(intensityValues[i]);
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

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		for (int i = 0; i < intensityValues.length; i++) {
			try {
				iframe.darken(intensityValues[i]);
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

		Pixel[][] grayPixels = { 
				{ new GrayPixel(0d), new GrayPixel(0.3) },
				{ new GrayPixel(0.6), new GrayPixel(0.9) } };

		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				iframe = iframe.setPixel(x, y, colorPixels[x][y]);
			}
		}

		GrayFrame2D grayFrame = iframe.toGrayFrame();
		for (int x = 0; x < iframe.getWidth(); x++) {
			for (int y = 0; y < iframe.getHeight(); y++) {
				Utils.checkGrayEquality(grayPixels[x][y], grayFrame.getPixel(x, y));
			}
		}
	}
	
	@Test
	public void testCastToAnyFrame() {
		Frame2D source = new MutableFrame2D(5, 5);
		Frame2D iframe = new IndirectFrame2DImpl(source, 1, 1, 2, 2);
		
		// Eclipse doesn't like bare casts, so we have to shut it up
		AnyFrame2D thing = (AnyFrame2D)iframe;
		thing.getWidth();
	}
}
