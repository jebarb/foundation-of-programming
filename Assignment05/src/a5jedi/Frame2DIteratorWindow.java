package a5jedi;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Frame2DIteratorWindow implements Iterator<IndirectFrame2D> {
	protected Frame2D source;
	protected int width;
	protected int height;
	protected int xIndex = -1;
	protected int yIndex = -1;
	
	public Frame2DIteratorWindow(Frame2D source, int width, int height) {
		this.width = width;
		this.height = height;
		this.source = source;
	}
	
	public boolean hasNext() {
		return (xIndex + width < source.getWidth() || yIndex + height < source.getHeight());
	}
	
	public IndirectFrame2D next() {
		if (hasNext()) {
			if (xIndex + width < source.getWidth() - 1) {
				xIndex++;
			} else {
				xIndex = 0;
				yIndex++;
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

