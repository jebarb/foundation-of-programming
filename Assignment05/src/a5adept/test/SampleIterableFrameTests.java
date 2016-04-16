package a5adept.test;
import a5adept.Frame2D;
import a5adept.ImmutableFrame2D;

import a5adept.Pixel;
import a5adept.ColorPixel;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class SampleIterableFrameTests {
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
	public void testSampleBasicCase() {
		Frame2D frame = newFrame();
		
		List<Pixel> samplerExpected = Arrays.asList(
				RED, GREEN, BLUE, WHITE,
				GREEN, BLUE, WHITE, RED,
				BLUE, WHITE, RED, GREEN,
				WHITE, RED, GREEN, BLUE);
		
		Iterator<Pixel> sampler = frame.sample(0, 0, 1, 1);
		List<Pixel> samplerReal = TestUtils.iteratorToList(sampler);
		
		assertEquals("sample(0, 0, 1, 1) returned incorrect length",
				samplerExpected.size(), samplerReal.size());
		
		for (int i = 0; i < samplerExpected.size(); i++) {
			Pixel expected = samplerExpected.get(i);
			Pixel real = samplerReal.get(i);
			
			TestUtils.assertColorsEqual("sample(0, 0, 1, 1) returned different pixel, index " + i, 
					expected, real);
		}
	}
	
	@Test
	public void testSampleAdvancedCase() {
		Frame2D frame = newFrame();
		
		List<Pixel> samplerExpected = Arrays.asList(
				GREEN, WHITE,
				WHITE, GREEN);
		
		Iterator<Pixel> sampler = frame.sample(1, 0, 2, 2);
		List<Pixel> samplerReal = TestUtils.iteratorToList(sampler);
		
		assertEquals("sample(0, 0, 1, 1) returned incorrect length",
				samplerExpected.size(), samplerReal.size());
		
		for (int i = 0; i < samplerExpected.size(); i++) {
			Pixel expected = samplerExpected.get(i);
			Pixel real = samplerReal.get(i);
			
			TestUtils.assertColorsEqual("sample(1, 0, 2, 2) returned different pixel, index " + i, 
					expected, real);
		}
	}
	
	@Test
	public void testSampleRemove() {
		Frame2D frame = newFrame();
		Iterator<Pixel> pixel = frame.sample(0, 0, 1, 1);
		
		boolean removeThrows = false;
		try {
			pixel.remove();
		} catch (UnsupportedOperationException exn) {
			removeThrows = true;
		}
		
		assertTrue("sample(0, 0, 1, 1).remove() does not throw properly", 
				removeThrows);
	}
}
