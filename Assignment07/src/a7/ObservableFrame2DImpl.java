package a7;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObservableFrame2DImpl extends AnyFrame2D implements ObservableFrame2D {

	private Frame2D frame;
	private List<RegisteredROIObserver> observers;

	private boolean suspended;
	private Region changed_region;

	public ObservableFrame2DImpl(int width, int height) {
		this(width, height, new ColorPixel(0,0,0));
	}

	public ObservableFrame2DImpl(int width, int height, Pixel init_pixel) {
		this(new MutableFrame2D(width, height, init_pixel));
	}

	public ObservableFrame2DImpl(Frame2D frame) {
		if (frame == null) {
			throw new IllegalArgumentException("Frame is null");
		}
		this.frame = frame;
		observers = new ArrayList<RegisteredROIObserver>();
		suspended = false;
		changed_region = null;
	}

	@Override
	public void registerROIObserver(ROIObserver observer) {
		registerROIObserver(observer, new Region(new Coordinate(0, 0), new Coordinate(getWidth()-1, getHeight()-1)));
	}

	@Override
	public void registerROIObserver(ROIObserver observer, Region r) {
		if (r == null) {
			throw new IllegalArgumentException("Region to observe is null");
		}
		Region roi = r.intersect(new Region(new Coordinate(0, 0), new Coordinate(getWidth()-1, getHeight()-1)));
		if (roi != null) {
			observers.add(new RegisteredROIObserverImpl(observer, roi));
		}
	}

	@Override
	public void unregisterROIObserver(ROIObserver observer) {
		Iterator<RegisteredROIObserver> roi_iterator = observers.iterator();
		while (roi_iterator.hasNext()) {
			RegisteredROIObserver o = roi_iterator.next();
			if (o.getObserver() == observer) {
				roi_iterator.remove();
			}
		}
	}

	@Override
	public void suspendObservable() {
		suspended = true;
	}

	@Override
	public void resumeObservable() {
		suspended = false;
		notify_observers();
	}

	@Override
	public int getWidth() {
		return frame.getWidth();
	}

	@Override
	public int getHeight() {
		return frame.getHeight();
	}

	@Override
	public Pixel getPixel(int x, int y) {
		return frame.getPixel(x,y);
	}

	@Override
	public Frame2D setPixel(int x, int y, Pixel p) {
		frame = frame.setPixel(x,y,p);

		changed_region = (new Region(new Coordinate(x,y), new Coordinate(x,y))).union(changed_region);
		notify_observers();
		return this;
	}

	private void notify_observers() {
		if (!suspended) {
			if (changed_region != null) {
				for (RegisteredROIObserver o : observers) {
					Region intersect = changed_region.intersect(o.getROI());
					if (intersect != null) {
						o.notify(this, intersect);
					}
				}
			}
			changed_region = null;
		}
	}

}
