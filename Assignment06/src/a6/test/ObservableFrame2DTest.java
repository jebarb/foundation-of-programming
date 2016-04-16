package a6.test;

import a6.Pixel;
import a6.ColorPixel;
import a6.Coordinate;
import a6.Region;

import a6.Frame2D;
import a6.ObservableFrame2D;
import a6.ObservableFrame2DImpl;
import a6.ROIObserver;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

class TestObserver implements ROIObserver {
    public ObservableFrame2D frame;
    public Region region;

    @Override
    public void notify(ObservableFrame2D f, Region r) {
        frame = f;
        region = r;
    }

    /**
     * Resets the observer to a known, empty state. Useful for using the
     * same observer with multiple tests.
     */
    public void reset() {
        this.frame = null;
        this.region = null;
    }

    /**
     * Whether or not this observed anything.
     */
    public boolean hasObservedSomething() {
        return this.frame != null && this.region != null;
    }
}

class MultipleTestObserver implements ROIObserver {
    public List<ObservableFrame2D> framesObserved;
    public List<Region> regionsObserved;

    public MultipleTestObserver() {
        framesObserved = new ArrayList<ObservableFrame2D>();
        regionsObserved = new ArrayList<Region>();
    }

    @Override
    public void notify(ObservableFrame2D f, Region r) {
        framesObserved.add(f);
        regionsObserved.add(r);
    }
}

public class ObservableFrame2DTest {

    private static Pixel RED = new ColorPixel(1d, 0d, 0d);
    private static int DEFAULT_WIDTH = 10;
    private static int DEFAULT_HEIGHT = 10;

    private ObservableFrame2D getFrame() {
        return new ObservableFrame2DImpl(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private boolean pixelsEqual(Pixel a, Pixel b) {
        return a.getRed() == b.getRed() &&
                a.getGreen() == b.getGreen() &&
                a.getBlue() == b.getBlue();
    }

    private boolean regionsEqual(Region a, Region b) {
        return a.getLeft() == b.getLeft() &&
                a.getRight() == b.getRight() &&
                a.getTop() == b.getTop() &&
                a.getBottom() == b.getBottom();
    }

    @Test
    public void testObservableFrameBasics() {
        ObservableFrame2D frame = getFrame();

        // Ensure that the frame has the basic attributes a frame should
        assertEquals("ObservableFrame has wrong width",
                DEFAULT_WIDTH, frame.getWidth());

        assertEquals("ObservableFrame has wrong height",
                DEFAULT_HEIGHT, frame.getHeight());
    }

    @Test
    public void testObservableNotRegistered() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();

        // Ensure that the observer gets no data when it isn't
        // looking at the frame
        frame.setPixel(0, 0, RED);
        assertFalse("The observer observed something before being registered",
                observer.hasObservedSomething());
    }

    @Test
    public void testObservableObserved() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        Region modified = new Region(new Coordinate(4, 4), new Coordinate(4, 4));

        frame.registerROIObserver(observer, region);

        // Register the observable and ensure that it gets an event when
        // a pixel in our region is changed
        frame.setPixel(4, 4, RED);

        assertTrue("The observer has not observed the changed frame",
                pixelsEqual(observer.frame.getPixel(4, 4), RED));
        assertTrue("The observer has observed the wrong region",
                regionsEqual(observer.region, modified));
    }

    @Test
    public void testObservableObservedIndirectly() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        Region modified = new Region(new Coordinate(4, 4), new Coordinate(4, 4));
        frame.registerROIObserver(observer, region);

        // Ensure that mucking with the data in the subframe causes an event in
        // the parent frame
        Frame2D subframe = frame.extract(3, 3, 3, 3);
        subframe.setPixel(1, 1, RED);

        assertTrue("The observer has not observed the changed frame",
                pixelsEqual(observer.frame.getPixel(4, 4), RED));
        assertTrue("The observer has observed the wrong region",
                regionsEqual(observer.region, modified));
    }

    @Test
    public void testObservableObservedOutsideRegion() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        frame.registerROIObserver(observer, region);

        // Change something outside our region, and ensure that the observer
        // is *not* notified
        frame.setPixel(0, 0, RED);

        assertFalse("The observer was notified for a pixel not in its region",
                observer.hasObservedSomething());
    }

    @Test
    public void testObservableObservedMultiple() {
        ObservableFrame2D frame = getFrame();
        MultipleTestObserver observer = new MultipleTestObserver();

        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        Region region2 = new Region(new Coordinate(2, 2), new Coordinate(5, 5));
        Region modified = new Region(new Coordinate(4, 4), new Coordinate(4, 4));

        frame.registerROIObserver(observer, region);
        frame.registerROIObserver(observer, region2);

        // Modify a pixel which covers multiple regions at once, and ensure
        // that it is recorded twice
        frame.setPixel(4, 4, RED);

        assertTrue("The observer has not observed the frame change",
                observer.framesObserved.get(0) == frame);

        assertTrue("The observer has not observed the frame change",
                observer.framesObserved.get(1) == frame);

        assertTrue("The observer is observing garbage frames",
                observer.framesObserved.size() == 2);

        assertTrue("The observer has observed the wrong region",
                regionsEqual(observer.regionsObserved.get(0), modified));

        assertTrue("The observer has observed the wrong region",
                regionsEqual(observer.regionsObserved.get(1), modified));

        assertTrue("The observer is observing garbage regions",
                observer.regionsObserved.size() == 2);
    }

    @Test
    public void testObservableRemoved() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));

        // Register and unregister and ensure no changes occur after unregistration
        frame.registerROIObserver(observer, region);
        frame.unregisterROIObserver(observer);

        frame.setPixel(4, 4, RED);
        assertFalse("The observer was notified when unregistered",
                observer.hasObservedSomething());
    }

    @Test
    public void testObservableRemovedRegion() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));

        // Register and unregister and ensure no changes occur
        frame.registerROIObserver(observer, region);

        Region intersectedRegion = new Region(new Coordinate(0, 0), new Coordinate(4, 4));
        frame.unregisterROIObservers(intersectedRegion);

        frame.setPixel(4, 4, RED);
        assertFalse("The observer was notified when unregistered",
                observer.hasObservedSomething());
    }

    @Test
    public void testFindROIObservablesEmpty() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));

        frame.registerROIObserver(observer, region);

        Region nonIntersectingRegion = new Region(new Coordinate(0, 0), new Coordinate(2, 2));
        ROIObserver[] observers = frame.findROIObservers(nonIntersectingRegion);

        assertEquals("Found observers for region that has none",
                0, observers.length);
    }

    @Test
    public void testFindROIObservablesNonempty() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));

        frame.registerROIObserver(observer, region);

        Region intersectingRegion = new Region(new Coordinate(0, 0), new Coordinate(4, 4));
        ROIObserver[] observers = frame.findROIObservers(intersectingRegion);

        assertEquals("Found no observers when there should be one",
                1, observers.length);
    }

    @Test
    public void testObservableSuspensionNoChange() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        frame.registerROIObserver(observer, region);

        // Suspend and resume, and ensure that no changes are reported
        frame.suspendObservable();
        frame.resumeObservable();

        assertFalse("The observer was notified when nothing changed",
                observer.hasObservedSomething());
    }

    @Test
    public void testObservableSuspensionWithChange() {
        ObservableFrame2D frame = getFrame();
        TestObserver observer = new TestObserver();
        TestObserver observer2 = new TestObserver();

		/*  0123456789
		 * +----------+
		 * |*****     | 0
		 * |*   *     | 1
		 * |*   *     | 2
		 * |*  ~~~~   | 3
		 * |****& ~   | 4
		 * |   ~ &~   | 5
		 * |   ~~~~   | 6 
		 * |          | 7
		 * |          | 8
		 * |          | 9
		 * +----------+
		 * 
		 * & - a pixel that is modified
		 * * - region1
		 * ~ - region2
		 */
        Region region = new Region(new Coordinate(3, 3), new Coordinate(6, 6));
        Region region2 = new Region(new Coordinate(0, 0), new Coordinate(4, 4));

        Region modified = new Region(new Coordinate(4, 4), new Coordinate(5, 5));
        Region modified2 = new Region(new Coordinate(4, 4), new Coordinate(4, 4));

        frame.registerROIObserver(observer, region);
        frame.registerROIObserver(observer2, region2);

        // Suspend the frame, and ensure that we get no changes
        frame.suspendObservable();
        frame.setPixel(4, 4, RED);
        frame.setPixel(5, 5, RED);

        assertFalse("The 1st observer was notified during a suspension",
                observer.hasObservedSomething());

        assertFalse("The 2nd observer was notified during a suspension",
                observer2.hasObservedSomething());

        frame.resumeObservable();

        // Ensure that we get a change when we resumed, and that the region
        // each observer is notified about is the intersection of the region
        // that observer cares about, and the actual region changed.
        assertTrue("The 1st observer has not observed the changed frame, post-resume",
                pixelsEqual(observer.frame.getPixel(4, 4), RED) &&
                        pixelsEqual(observer.frame.getPixel(5, 5), RED));

        assertTrue("The 1st observer has observed the wrong region",
                regionsEqual(observer.region, modified));

        assertTrue("The 2nd observer has not observed the changed frame', post-resume",
                pixelsEqual(observer.frame.getPixel(4, 4), RED) &&
                        pixelsEqual(observer.frame.getPixel(5, 5), RED));

        assertTrue("The 2nd observer has observed the wrong region",
                regionsEqual(observer2.region, modified2));
    }
}
