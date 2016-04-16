package a7;

abstract public class AnyFrame2D implements Frame2D {

    abstract public int getWidth();

    abstract public int getHeight();

    abstract public Pixel getPixel(int x, int y);

    @Override
    public Pixel getPixel(Coordinate c) {
        if (c == null) {
            throw new IllegalArgumentException("Coordinate is null");
        }
        return this.getPixel(c.getX(), c.getY());
    }

    abstract public Frame2D setPixel(int x, int y, Pixel p);

    @Override
    public Frame2D setPixel(Coordinate c, Pixel p) {
        if (c == null) {
            throw new IllegalArgumentException("Coordinate is null");
        }
        return this.setPixel(c.getX(), c.getY(), p);
    }

    @Override
    public IndirectFrame2D extract(int xoff, int yoff, int width, int height) {
        return new IndirectFrame2DImpl(this, xoff, yoff, width, height);
    }

    @Override
    public IndirectFrame2D extract(Coordinate corner_a, Coordinate corner_b) {
        if (corner_a == null || corner_b == null) {
            throw new IllegalArgumentException("One or both coordinates is null");
        }

        int min_x = corner_a.getX() < corner_b.getX() ? corner_a.getX() : corner_b.getX();
        int min_y = corner_a.getY() < corner_b.getY() ? corner_a.getY() : corner_b.getY();
        int max_x = corner_a.getX() > corner_b.getX() ? corner_a.getX() : corner_b.getX();
        int max_y = corner_a.getY() > corner_b.getY() ? corner_a.getY() : corner_b.getY();

        return extract(min_x, min_y, (max_x - min_x) + 1, (max_y - min_y) + 1);
    }

    @Override
    public IndirectFrame2D extract(Region r) {
        if (r == null) {
            throw new IllegalArgumentException("Region is null");
        }

        return extract(r.getUpperLeft(), r.getLowerRight());
    }

    public Frame2D lightenOrDarken(double factor) {
        if (factor == 0) return this;
        Frame2D frame = new MutableFrame2D(this.getWidth(), this.getHeight());
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                frame.setPixel(i, j, this.getPixel(i, j).lightenOrDarken(factor));
            }
        }
        return frame;
    }

    @Override
    public Frame2D blur(int factor) {
        if (factor < 0) {
            throw new RuntimeException("Factor can not be less than 0");
        } else if (factor > 5) {
            throw new RuntimeException("Factor can not be greater than 5");
        } else if (factor == 0) {
            return this;
        }
        Frame2D frame = new MutableFrame2D(this.getWidth(), this.getHeight());
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                frame.setPixel(i, j, this.blurPixel(i, j, factor));
            }
        }
        return frame;
    }

    private Pixel blurPixel(int i, int j, int factor) {
        int count = 0;
        double red = 0, green = 0, blue = 0;
        for (int x = i - factor; x <= factor + i; x++) {
            for (int y = j - factor; y <= factor + j; y++) {
                if (x > -1 && y > -1 && x < getWidth() && y < getHeight()) {
                    red += getPixel(x, y).getRed();
                    green += getPixel(x, y).getGreen();
                    blue += getPixel(x, y).getBlue();
                    count++;
                }
            }
        }
        Pixel res = new ColorPixel(red / (double) count, green / (double) count, blue / (double) count);
        return res;
    }

    @Override
    public Frame2D saturate(double factor) {
        if (factor == 0) {
            return this;
        }
        Frame2D frame = new MutableFrame2D(this.getWidth(), this.getHeight());
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                frame.setPixel(i, j, this.getPixel(i, j).saturate(factor));
            }
        }
        return frame;
    }

    @Override
    public ObservableFrame2D createObservable() {
        return new ObservableFrame2DImpl(this);
    }

}
