package a6;

public interface ObservableFrame2D extends Frame2D {
	void registerROIObserver(ROIObserver observer, Region r);
	
	void unregisterROIObservers(Region r);
	void unregisterROIObserver(ROIObserver observer);

	ROIObserver[] findROIObservers(Region r);
	
	void suspendObservable();
	void resumeObservable();
}
