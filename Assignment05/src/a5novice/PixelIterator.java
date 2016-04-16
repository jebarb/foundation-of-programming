package a5novice;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PixelIterator implements Iterator<Pixel> {

	private final Frame2D source;
	private int xStart;
	private int yStart;
	private int xIndex;
	private int yIndex;
	private int xEnd;
	private int yEnd;

	public PixelIterator(Frame2D source) {
		this.source = source;
		this.xStart = 0;
		this.yStart = 0;
		this.xIndex = -1;
		this.yIndex = 0;
		this.xEnd = source.getWidth() - 1;
		this.yEnd = source.getHeight() - 1;
	}

	public PixelIterator(Frame2D source, int xStart, int yStart, int xEnd, int yEnd) {
		this.source = source;
		this.xStart = xStart;
		this.yStart = yStart;
		this.xIndex = xStart - 1;
		this.yIndex = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public boolean hasNext() {
		return (this.xIndex < this.xEnd || this.yIndex < this.yEnd);
	}

	public Pixel next() {
		if (hasNext()) {
			if (xIndex < xEnd) {
				xIndex++;
			} else if (xIndex == xEnd) {
				xIndex = xStart;
				yIndex++;
			}
			return this.source.getPixel(xIndex, yIndex);
		} else {
			throw new NoSuchElementException("There is no pixel at (" + xIndex + ", " + yIndex + ")");
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}

