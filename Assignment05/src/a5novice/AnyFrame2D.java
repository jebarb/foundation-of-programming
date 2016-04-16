package a5novice;

import java.util.Iterator;

abstract public class AnyFrame2D implements Frame2D {

	abstract public int getWidth();
	abstract public int getHeight();
	abstract public Pixel getPixel(int x, int y);
	abstract public Frame2D setPixel(int x, int y, Pixel p);
	
    public Pixel getPixel(Coordinate c) {
    	if (c==null) {
    		throw new IllegalArgumentException("Coordinate is null");
    	}
    	return this.getPixel(c.getX(), c.getY());
    }
    
    public Frame2D setPixel(Coordinate c, Pixel p) {
    	if (c==null) {
    		throw new IllegalArgumentException("Coordinate is null");
    	}
    	return this.setPixel(c.getX(), c.getY(), p);
    }

	public Frame2D lighten(double factor) {
		Frame2D result = this;
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				result = result.setPixel(x,y,getPixel(x,y).lighten(factor));
			}
		}
		return result;
	}

	public Frame2D darken(double factor) {
		if ((factor < 0.0 || factor > 1.0)) {
			throw new IllegalArgumentException("Factor out of range");
		}

		Frame2D result = this;
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				result = result.setPixel(x,y,getPixel(x,y).darken(factor));
			}
		}
		return result;
	}		

	public IndirectFrame2D extract(int xOffset, int yOffset, int width, int height) {
		IndirectFrame2D frame = new IndirectFrame2DImpl(this, xOffset, yOffset, width, height);
		return frame;
	}
	
    public IndirectFrame2D extract(Coordinate corner_a, Coordinate corner_b) {
		return this.extract(corner_a.getX(), corner_a.getY(), corner_b.getX() - corner_a.getX() + 1, corner_b.getY() - corner_a.getY() + 1);
    }
    
    public Iterator<Pixel> iterator() {
    	Iterator<Pixel> it = new PixelIterator(this);
    	return it;
    }

	protected boolean checkGet(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new IllegalArgumentException("x is out of bounds");
		}
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("y is out of bounds");
		}
		return true;
	}

	protected boolean checkSet(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			throw new ArrayIndexOutOfBoundsException("x is out of bounds");
		}
		if (y < 0 || y >= getHeight()) {
			throw new ArrayIndexOutOfBoundsException("y is out of bounds");
		}
		return true;
	}

	protected static boolean check_factor(double factor) {
		if (factor < 0) {
			throw new IllegalArgumentException("Factor can not be less than 0.0");
		} else if (factor > 1.0) {
			throw new IllegalArgumentException("Factor can not be greater than 1.0");
		}
		return true;
	}
}
