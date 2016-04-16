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

public class TileIterableFrameTests {
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
	public void testTile() {
		Frame2D frame = newFrame();
		
		/*
		 * 3x3 was chosen because it ensures that there is a bit of extra
		 * data left outside the tile; ignoring these extra pixels and returning
		 * only data in the tiles is a key part of implementing tile() correctly
		 */
		List<Frame2D> expectedTiles = Arrays.asList(new ImmutableFrame2D(
				new Pixel[][] {{RED, GREEN, BLUE}, {GREEN, BLUE, WHITE}, {BLUE, WHITE, RED}}));
		
		Iterator<IndirectFrame2D> tiles = frame.tile(3, 3);
		List<IndirectFrame2D> realTiles = TestUtils.iteratorToList(tiles);
		
		assertEquals("tile(3, 3) returned incorrect length",
				expectedTiles.size(), realTiles.size());
		
		for (int i = 0; i < expectedTiles.size(); i++) {
			TestUtils.assertFramesEqual("tiles(3, 3), " + i + "th element, is incorrect",
					expectedTiles.get(i), realTiles.get(i));
		}
	}
	
	@Test
	public void testTileRemove() {
		Frame2D frame = newFrame();
		Iterator<IndirectFrame2D> tile = frame.tile(1, 1);
		
		boolean removeThrows = false;
		try {
			tile.remove();
		} catch (UnsupportedOperationException exn) {
			removeThrows = true;
		}
		
		assertTrue("tile(1, 1).remove() does not throw properly", 
				removeThrows);
	}
}
