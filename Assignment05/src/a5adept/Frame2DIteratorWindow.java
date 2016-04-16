package a5adept;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Frame2DIteratorWindow implements Iterator<IndirectFrame2D> {
    private Frame2D source;
    private int width;
    private int height;
    private int xIndex = -1;
    private int yIndex = 0;

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
            if (xIndex + width < source.getWidth()) {
                xIndex++;
            } else if (xIndex + width == source.getWidth()) {
                xIndex = 0;
                yIndex++;
            } else {
                throw new NoSuchElementException("Invalid subframe");
            }
        }
        return this.source.extract(xIndex, yIndex, width, height);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}

