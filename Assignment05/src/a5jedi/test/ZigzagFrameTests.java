package a5jedi.test;
import a5jedi.Frame2D;
import a5jedi.ImmutableFrame2D;

import a5jedi.Pixel;
import a5jedi.ColorPixel;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ZigzagFrameTests {
	static private Pixel RED = new ColorPixel(1d, 0d, 0d);
	static private Pixel GREEN = new ColorPixel(0d, 1d, 0d);
	static private Pixel BLUE = new ColorPixel(0d, 0d, 1d);
	static private Pixel WHITE = new ColorPixel(1d, 1d, 1d);
	
	/**
	 * Creates a new frame:
	 * 
	 * 		0     1		2	  3    x
	 * 0 [ RED   GREEN  BLUE  WHITE ]
	 * 1 [ GREEN BLUE   WHITE RED   ]
	 * 2 [ BLUE  WHITE  RED   GREEN ]
	 * 3 [ WHITE RED    GREEN BLUE  ]
	 * y
	 */
	Frame2D newFrame() {
		Pixel[][] pixels = {
			{ RED, GREEN, BLUE, WHITE },
			{ GREEN, BLUE, WHITE, RED },
			{ BLUE, WHITE, RED, GREEN },
			{ WHITE, RED, GREEN, BLUE },
		};
		return new ImmutableFrame2D(pixels);
	}
	
	/**
	 * Ensures that the iterator returns the pixels in the proper order.
	 */
	@Test
	public void testZigzag() {
		Frame2D frame = newFrame();
		
		List<Pixel> zigzagExpected = Arrays.asList(
				RED, 
				GREEN, GREEN, 
				BLUE, BLUE, BLUE,
				WHITE, WHITE, WHITE, WHITE,
				RED, RED, RED,
				GREEN, GREEN,
				BLUE);
		
		Iterator<Pixel> zigzag = frame.zigzag();
		List<Pixel> zigzagReal = TestUtils.iteratorToList(zigzag);
		
		assertEquals("zigzag() returned incorrect length",
				zigzagExpected.size(), zigzagReal.size());
		
		for (int i = 0; i < zigzagExpected.size(); i++) {
			Pixel expected = zigzagExpected.get(i);
			Pixel real = zigzagReal.get(i);
			
			TestUtils.assertColorsEqual("zigzag() returned different pixel, index " + i, 
					expected, real);
		}
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
