package a5novice.test;
import a5novice.Frame2D;
import a5novice.IndirectFrame2D;
import a5novice.ImmutableFrame2D;

import a5novice.Pixel;
import a5novice.ColorPixel;

import a5novice.Coordinate;

import a5novice.IllegalFrame2DGeometryException;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Ensures that the Coordinate methods on Frame2D work as specified. 
 * 
 * Note that this only tests them via ImmutableFrame2D, but they should
 * work on IndirectFrame2D also since the assignment directs them to be
 * implemented on AnyFrame2D.
 */
public class CoordinateFrameTest {
	static private Pixel RED = new ColorPixel(1d, 0d, 0d);
	static private Pixel GREEN = new ColorPixel(0d, 1d, 0d);
	static private Pixel BLUE = new ColorPixel(0d, 0d, 1d);
	static private Pixel WHITE = new ColorPixel(1d, 1d, 1d);
	
	/**
	 * Creates a new frame:
	 * 
	 *        0     1    x
	 *   0 [ RED   BLUE  ]
	 *   1 [ GREEN WHITE ]
	 *   y
	 */
	Frame2D newFrame() {
		Pixel[][] pixels = {
			{ RED, GREEN, },
			{ BLUE, WHITE, }
		};
		return new ImmutableFrame2D(pixels);
	}
	
	/**
	 * Ensures that:
	 * 
	 *     getPixel(int, int) == getPixel(Coordinate)
	 */
	@Test
	public void testGetPixelCoordinate() {
		Frame2D frame = newFrame();
		
		Pixel xy = frame.getPixel(0, 0);
		Pixel coord = frame.getPixel(new Coordinate(0, 0));
		TestUtils.assertColorsEqual("getPixel(int, int) != getPixel(Coordinate)", 
				xy, coord);
		
		boolean badCoordThrows = false;
		try {
			frame.getPixel(new Coordinate(2, 2));
		} catch (IllegalArgumentException exn) {
			badCoordThrows = true;
		}
		
		assertTrue("getPixel(Coordinate) does not throw properly", badCoordThrows);
		
		boolean nullCoordThrows = false;
		try {
			frame.getPixel(null);
		} catch (IllegalArgumentException exn) {
			nullCoordThrows = true;
		}
		
		assertTrue("getPixel(Coordinate) does not throw properly", nullCoordThrows);
	}
	
	/**
	 * Ensures that:
	 * 
	 *     setPixel(int, int, Pixel) == setPixel(Coordinate, Pixel)
	 */
	@Test
	public void testSetPixelCoordinate() {
		Frame2D frame = newFrame();
		
		Frame2D xy = frame.setPixel(0, 0, WHITE);
		Frame2D coord = frame.setPixel(0, 0, WHITE);
		TestUtils.assertFramesEqual("setPixel(int, int, Pixel) != setPixel(Coordinate, Pixel)",
				xy, coord);
		
		boolean coordThrowsBadCoord = false;
		try {
			frame.setPixel(new Coordinate(2, 2), WHITE);
		} catch (ArrayIndexOutOfBoundsException exn) {
			// I would think this should be IllegalArgumentException, but the 
			// reference code disagrees with me.
			coordThrowsBadCoord = true;
		}
		
		assertTrue("setPixel(Coordinate, Pixel) does not throw properly", 
				coordThrowsBadCoord);
		
		boolean coordThrowsBadPixel = false;
		try {
			frame.setPixel(new Coordinate(0, 0), null);
		} catch (IllegalArgumentException exn) {
			coordThrowsBadPixel = true;
		}
		
		assertTrue("setPixel(Coordinate, Pixel) does not throw properly", 
				coordThrowsBadPixel);
		
		boolean nullCoordThrows = false;
		try {
			frame.setPixel(null, WHITE);
		} catch (IllegalArgumentException exn) {
			nullCoordThrows = true;
		}
		
		assertTrue("setPixel(Coordinate, Pixel) does not throw properly", 
				nullCoordThrows);
	}
	
	/**
	 * Ensures that extract(int, int, int, int) == extract(Coordinate, Coordinate)
	 */
	@Test
	public void testExtractCoordinates() {
		Frame2D frame = newFrame();
		
		// Why the same point for both corners? Explained here:
		// https://piazza.com/class/ibv0uw0vibk6xp?cid=379
		IndirectFrame2D xy = frame.extract(0,  0,  1,  1);
		IndirectFrame2D coord = frame.extract(new Coordinate(0, 0), new Coordinate(0, 0));
		TestUtils.assertFramesEqual("extract(int, int, int, int) != extract(Coordinate, Coordinate",
				xy, coord);
	}
}
