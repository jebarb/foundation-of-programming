package a7;

public interface ObservableFrame2D extends Frame2D {

	void registerROIObserver(ROIObserver observer, Region r);	
	void registerROIObserver(ROIObserver observer);
	
	void unregisterROIObserver(ROIObserver observer);
	
	void suspendObservable();
	void resumeObservable();
}
