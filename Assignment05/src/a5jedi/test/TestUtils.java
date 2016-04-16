package a5jedi.test;
import a5jedi.Frame2D;
import a5jedi.Pixel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TestUtils {
	static private double EPSILON = 0.001;
	
	/**
	 * Ensures that two pixels are equal in all of their color fields.
	 */
	static public void assertColorsEqual(String message, Pixel a, Pixel b) {
		assertEquals(message, a.getRed(), b.getRed(), EPSILON);
		assertEquals(message, a.getGreen(), b.getGreen(), EPSILON);
		assertEquals(message, a.getBlue(), b.getBlue(), EPSILON);
	}
	
	/**
	 * Ensures that two frames are equivalent.
	 */
	static public void assertFramesEqual(String message, Frame2D a, Frame2D b) {
		assertEquals(message, a.getWidth(), b.getWidth());
		assertEquals(message, a.getHeight(), b.getHeight());
		
		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				assertColorsEqual(message, a.getPixel(x, y), b.getPixel(x, y));
			}
		}
	}
	
	/**
	 * Ensures that every element of a boolean array is true.
	 */
	static public void assertAll(boolean[] values) {
		for (int i = 0; i < values.length; i++) {
			assertTrue("Test " + i + " failed", values[i]);
		}
	}
	
	/**
	 * Converts an Iterator into a List.
	 */
	static public <T> List<T> iteratorToList(Iterator<T> iter) {
		List<T> list = new ArrayList<T>();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		
		return list;
	}
}