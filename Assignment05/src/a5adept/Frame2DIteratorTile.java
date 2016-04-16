package a5adept;

import java.util.Iterator;
import java.util.NoSuchElementException;

class Frame2DIteratorTile implements Iterator<IndirectFrame2D> {
    private Frame2D source;
    private int width;
    private int height;
    private int xIndex;
    private int yIndex = 0;

    public Frame2DIteratorTile(Frame2D source, int width, int height) {
        this.width = width;
        this.height = height;
        this.source = source;
        this.xIndex = -width;
    }

    public boolean hasNext() {
        return (xIndex + 2 * width < source.getWidth() || yIndex + 2 * height < source.getHeight());
    }

    public IndirectFrame2D next() {
        if (hasNext()) {
            if (xIndex + width < source.getWidth() - 1) {
                xIndex += this.width;
            } else if (xIndex + width == source.getWidth() - 1) {
                xIndex = 0;
                yIndex += this.width;
            } else {
                throw new NoSuchElementException("There is no pixel at (" + xIndex + ", " + yIndex + ")");
            }
        }
        return this.source.extract(xIndex, yIndex, width, height);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}