package a5jedi;

import java.util.Iterator;

public class PixelIterator implements Iterator<Pixel> {

	private final Frame2D source;
	private int xStart, yStart;
	private int xIndex, yIndex;
	private int dx, dy;

	public PixelIterator(Frame2D source) {
		this.source = source;
		this.xStart = 0;
		this.yStart = 0;
		this.xIndex = -1;
		this.yIndex = 0;
		this.dx = 1;
		this.dy = 0;
	}

	public PixelIterator(Frame2D source, int xStart, int yStart, int dx, int dy) {
		this.source = source;
		this.xStart = xStart;
		this.yStart = yStart;
		this.xIndex = xStart - dx;
		this.yIndex = yStart;
		this.dx = dx;
		this.dy = dy;
	}

	public boolean hasNext() {
		return (this.xIndex + dx < this.source.getWidth() || this.yIndex + dy < this.source.getHeight());
	}

	public Pixel next() {
		if (hasNext()) {
			if (xIndex < this.source.getWidth() - dx) {
				xIndex += dx;
			} else {
				xIndex = xStart;
				yIndex += dy;
			}
		}
		return this.source.getPixel(xIndex, yIndex);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}

