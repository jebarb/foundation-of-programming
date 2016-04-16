package a5adept.test;
import a5adept.Frame2D;
import a5adept.ImmutableFrame2D;
import a5adept.IndirectFrame2D;

import a5adept.Pixel;
import a5adept.ColorPixel;

import a5adept.IllegalFrame2DGeometryException;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class WindowIterableFrameTests {
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
	
	@Test
	public void testWindow() {
		Frame2D frame = newFrame();
		
		List<Frame2D> expectedWindows = Arrays.asList(
				new ImmutableFrame2D(new Pixel[][] {{RED, GREEN}, {GREEN, BLUE}}),
				new ImmutableFrame2D(new Pixel[][] {{GREEN, BLUE}, {BLUE, WHITE}}),
				new ImmutableFrame2D(new Pixel[][] {{BLUE, WHITE}, {WHITE, RED}}),
				new ImmutableFrame2D(new Pixel[][] {{GREEN, BLUE}, {BLUE, WHITE}}),
				new ImmutableFrame2D(new Pixel[][] {{BLUE, WHITE}, {WHITE, RED}}),
				new ImmutableFrame2D(new Pixel[][] {{WHITE, RED}, {RED, GREEN}}),
				new ImmutableFrame2D(new Pixel[][] {{BLUE, WHITE}, {WHITE, RED}}),
				new ImmutableFrame2D(new Pixel[][] {{WHITE, RED}, {RED, GREEN}}),
				new ImmutableFrame2D(new Pixel[][] {{RED, GREEN}, {GREEN, BLUE}}));
		
		Iterator<IndirectFrame2D> windows = frame.window(2,  2);
		List<IndirectFrame2D> realWindows = TestUtils.iteratorToList(windows);
		
		assertEquals("window(2, 2) returned incorrect length",
				expectedWindows.size(), realWindows.size());
		
		for (int i = 0; i < expectedWindows.size(); i++) {
			TestUtils.assertFramesEqual("window(2, 2), " + i + "th element, is incorrect",
					expectedWindows.get(i), realWindows.get(i));
		}
	}
	
	@Test
	public void testWindowRemove() {
		Frame2D frame = newFrame();
		Iterator<IndirectFrame2D> window = frame.window(1, 1);
		
		boolean removeThrows = false;
		try {
			window.remove();
		} catch (UnsupportedOperationException exn) {
			removeThrows = true;
		}
		
		assertTrue("window(1, 1).remove() does not throw properly", 
				removeThrows);
	}
}
