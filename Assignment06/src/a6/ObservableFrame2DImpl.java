package a6;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class ObservableFrame2DImpl extends AnyFrame2D implements ObservableFrame2D {

    private Pixel[][] pixels;
    private Region changed;
    private LinkedHashMap<ROIObserver, List<Region>> observers;
    private boolean suspended;

    public ObservableFrame2DImpl(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalFrame2DGeometryException();
        }
        pixels = new Pixel[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[x][y] = new ColorPixel(0, 0, 0);
            }
        }
        this.observers = new LinkedHashMap<>();
        this.suspended = false;
    }

    @Override
    public int getWidth() {
        return pixels.length;
    }

    @Override
    public int getHeight() {
        return pixels[0].length;
    }

    @Override
    public Pixel getPixel(int x, int y) {
        check_coordinates(x, y);
        return pixels[x][y];
    }

    @Override
    public Frame2D setPixel(int x, int y, Pixel p) {
        check_coordinates(x, y);
        if (p == null) {
            throw new RuntimeException();
        }
        this.pixels[x][y] = p;
        this.notifyObservers(new Region(new Coordinate(x, y), new Coordinate(x, y)));
        return this;
    }

    public void registerROIObserver(ROIObserver observer, Region r) {
        if (!observers.containsKey(observer)) {
            observers.put(observer, new ArrayList<>());
        }
        observers.get(observer).add(r);
    }

    public void unregisterROIObservers(Region r) {
        for (ROIObserver o : this.findROIObservers(r)) {
            for (int i = 0; i < this.observers.get(o).size(); i++) {
                if (observers.get(o).get(i).intersect(r) != null) {
                    observers.get(o).remove(i);
                    i--;
                }
            }
            if (observers.get(o).size() == 0) {
                observers.remove(o);
            }
        }
    }

    public void unregisterROIObserver(ROIObserver observer) {
        observers.remove(observer);
    }

    public ROIObserver[] findROIObservers(Region r) {
        List<ROIObserver> o = new ArrayList<>();
        for (Entry<ROIObserver, List<Region>> entry : observers.entrySet()) {
            for (Region rCheck : entry.getValue()) {
                if (rCheck.intersect(r) != null) {
                    o.add(entry.getKey());
                }
            }
        }
        return o.toArray(new ROIObserver[o.size()]);
    }

    public void suspendObservable() {
        this.suspended = true;
    }

    public void resumeObservable() {
        this.suspended = false;
        this.notifyObservers(changed);
        changed = null;
    }

    private void notifyObservers(Region r) {
        if (suspended) {
            if (changed == null) {
                changed = r;
            } else {
                changed = changed.union(r);
            }
        } else {
            for (Entry<ROIObserver, List<Region>> entry : observers.entrySet()) {
                for (Region rCheck : entry.getValue()) {
                    if (rCheck.intersect(r) != null) {
                        entry.getKey().notify(this, rCheck.intersect(r));
                    }
                }
            }
        }
    }
}
