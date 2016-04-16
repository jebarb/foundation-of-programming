package a6;

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

    protected boolean check_coordinates(int x, int y) {
        if (x < 0 || x >= getWidth()) {
            throw new RuntimeException("x is out of bounds");
        }
        if (y < 0 || y >= getHeight()) {
            throw new RuntimeException("y is out of bounds");
        }
        return true;
    }

}
