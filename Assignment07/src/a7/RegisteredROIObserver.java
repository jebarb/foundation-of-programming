package a7;

public interface RegisteredROIObserver extends ROIObserver {

	Region getROI();
	ROIObserver getObserver();

}
