package a5jedi;

import java.util.Iterator;
import java.util.NoSuchElementException;

class Frame2DIteratorTile implements Iterator<IndirectFrame2D> {
	protected Frame2D source;
	protected int width;
	protected int height;
	protected int xIndex = -1;
	protected int yIndex = -1;
	
	public Frame2DIteratorTile(Frame2D source, int width, int height) {
		this.width = width;
		this.height = height;
		this.source = source;
	}
	
	@Override
	public boolean hasNext() {
		return (xIndex + 2*width < source.getWidth() || yIndex + 2*height < source.getHeight());
	}
	
	public IndirectFrame2D next() {
		if (hasNext()) {
			if (xIndex + width < source.getWidth() - 1) {
				xIndex += this.width;
				return this.source.extract(xIndex, yIndex, width, height);
			} else {
				xIndex = 0;
				yIndex += this.width;
			}
			return this.source.extract(xIndex, yIndex, width, height);
		} else {
			throw new NoSuchElementException("There is no pixel at (" + xIndex + ", " + yIndex + ")");
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}