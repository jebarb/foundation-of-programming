package a5jedi;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PixelIteratorZigZag implements Iterator<Pixel> {

    private final Frame2D source;
    private int xStart, yStart;
    private int xIndex, yIndex;
    private int xEnd, yEnd;
    //0=right, 1=down, 2=diagonal down, 3=diagonal up
    private int direction = 0;
    private int count = 0;

    public PixelIteratorZigZag(Frame2D source) {
        this.source = source;
        this.xStart = 0;
        this.yStart = 0;
        this.xIndex = -1;
        this.yIndex = 0;
        this.xEnd = source.getWidth() - 1;
        this.yEnd = source.getHeight() - 1;
    }

    @Override
    public boolean hasNext() {
        return count < (source.getHeight() * source.getWidth());
    }

    @Override
    public Pixel next() {
        if (hasNext()) {
            if (count < 2) {
                xIndex++;
            } else if (direction == 0) {
                if (yIndex == 0) {
                    direction = 2;
                    xIndex--;
                    yIndex++;
                } else if (yIndex == yEnd) {
                    direction = 3;
                    xIndex++;
                    yIndex--;
                } else {
                    throw new RuntimeException("Direction 0 error");
                }
            } else if (direction == 1) {
                if (xIndex == 0) {
                    direction = 3;
                    xIndex++;
                    yIndex--;
                } else if (xIndex == xEnd) {
                    direction = 2;
                    xIndex--;
                    yIndex++;
                } else {
                    throw new RuntimeException("Direction 1 error");
                }
            } else if (direction == 2) {
                if (!check_coordinates(xIndex - 1, yIndex + 1)) {
                    if (yIndex == yEnd) {
                        direction = 0;
                        xIndex++;
                    } else if (xIndex == xStart) {
                        direction = 1;
                        yIndex++;
                    } else {
                        throw new RuntimeException("Direction 2 error");
                    }
                } else {
                    xIndex--;
                    yIndex++;
                }
            } else if (direction == 3) {
                if (!check_coordinates(xIndex + 1, yIndex - 1)) {
                    if (yIndex == yStart) {
                        direction = 0;
                        xIndex++;
                    } else if (xIndex == xEnd) {
                        direction = 1;
                        yIndex++;
                    } else {
                        throw new RuntimeException("Direction 3 error");
                    }
                } else {
                    xIndex++;
                    yIndex--;
                }
            } else {
                throw new NoSuchElementException("There is no pixel at (" + xIndex + ", " + yIndex + ")");
            }
        }
        count++;
        return this.source.getPixel(xIndex, yIndex);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean check_coordinates(int x, int y) {
        boolean res = true;
        if (x < 0 || x >= source.getWidth()) {
            res = false;
        }
        if (y < 0 || y >= source.getHeight()) {
            res = false;
        }
        return res;
    }

}
