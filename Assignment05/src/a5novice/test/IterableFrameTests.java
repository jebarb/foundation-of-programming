package a5novice.test;
import a5novice.Frame2D;
import a5novice.ImmutableFrame2D;

import a5novice.Pixel;
import a5novice.ColorPixel;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Iterator;

public class IterableFrameTests {
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
	 * Ensures that the iterator returns the pixels in the proper order.
	 */
	@Test
	public void testIteratorOrder() {
		Iterator<Pixel> pixels = newFrame().iterator();
		
		assertTrue("Iterator finished too early", pixels.hasNext());
		TestUtils.assertColorsEqual("Iterator returned wrong 1st value",
				RED, pixels.next());
		
		assertTrue("Iterator finished too early", pixels.hasNext());
		TestUtils.assertColorsEqual("Iterator returned wrong 2nd value",
				BLUE, pixels.next());
		
		assertTrue("Iterator finished too early", pixels.hasNext());
		TestUtils.assertColorsEqual("Iterator returned wrong 3rd value",
				GREEN, pixels.next());
		
		assertTrue("Iterator finished too early", pixels.hasNext());
		TestUtils.assertColorsEqual("Iterator returned wrong 4th value",
				WHITE, pixels.next());
		
		assertFalse("Iterator finished too late", pixels.hasNext());
	}
	
	/**
	 * Ensure that remove() throws correctly.
	 */
	@Test
	public void testRemoveFails() {
		boolean removeRaises = false;
		try {
			newFrame().iterator().remove();
		} catch (UnsupportedOperationException exn) {
			removeRaises = true;
		}
		
		assertTrue("remove() did not throw properly", removeRaises);
	}
}
