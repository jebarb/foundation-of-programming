package a3novice.test;

import a3novice.Pixel;
import a3novice.ColorPixel;
import a3novice.Frame2D;
import a3novice.MutableFrame2D;

import static org.junit.Assert.*;

import org.junit.Test;

public class MutableFrame2DTest {
	private static double COLOR_DELTA = 0.001;

	/**
	 * A helper to check to assert equality between two pixels.
	 */
	private void checkPixelEquality(Pixel target, Pixel actual) {
		assertEquals(target.getRed(), actual.getRed(), COLOR_DELTA);
		assertEquals(target.getGreen(), actual.getGreen(), COLOR_DELTA);
		assertEquals(target.getBlue(), actual.getBlue(), COLOR_DELTA);
	}

	@Test
	public void testDefaultGetters() {
		// Ensure that a frame has all pixels as black by default
		Frame2D frame = new MutableFrame2D(1, 2);
		Pixel blackPixel = new ColorPixel(0d, 0d, 0d);

		assertEquals(1, frame.getWidth());
		assertEquals(2, frame.getHeight());

		for (int x = 0; x < 1; x++) {
			for (int y = 0; y < 2; y++) {
				checkPixelEquality(blackPixel, frame.getPixel(x, y));
			}
		}
	}

	@Test
	public void testMutableGetters() {
		// Ensure that a change in a frame is reflected in the getter
		Frame2D frame = new MutableFrame2D(1, 2);
		Pixel assign = new ColorPixel(0d, 0.3, 0.6);

		Frame2D subframe = frame.setPixel(0, 1, assign);
		checkPixelEquality(subframe.getPixel(0, 1), assign);
	}

	@Test
	public void testBadConstructor() {
		// Ensure that invalid constructor values throw exceptions
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
			} catch (RuntimeException exn) {
				failures[i] = true;
			}
		}

		assertTrue(Utils.all(failures));
	}

	@Test
	public void testBadGetPixel() {
		// Ensure that getPixel throws an exception when given invalid coordinates
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
		// Ensure that setPixel throws an exception when given invalid coordinates
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

		/* 
		 * In addition to invalid coordinate tests, ensure that null pixels
		 * throw, and do not change the pixel in the original frame (only a
		 * concern if we're using MutableFrame2D)
		 */
		boolean badPixelThrows = false;

		try {
			frame.setPixel(0, 0, null);
		} catch (RuntimeException exn) {
			badPixelThrows = true;
		}

		assertTrue(badPixelThrows);

		// The pixel is black since we haven't done any valid assignments
		checkPixelEquality(frame.getPixel(0, 0), new ColorPixel(0d, 0d, 0d));
	}
}
