package a6.test;

import a6.ColorPixel;
import a6.Coordinate;
import a6.Frame2D;
import a6.IllegalFrame2DGeometryException;
import a6.ObservableFrame2D;
import a6.ObservableFrame2DImpl;
import a6.Pixel;
import a6.ROIObserver;
import a6.Region;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

class ROIObserverImpl implements ROIObserver {
    // Using string output gives some good information for debugging purposes
    // (i.e. you can see exactly what regions are passed (or not passed))
    public static final String TEMPLATE = "Region: (%d, %d) -> (%d, %d)";

    @Override
    public void notify(ObservableFrame2D frame, Region r) {
        System.out.println(String.format(TEMPLATE,
                r.getLeft(),
                r.getTop(),
                r.getRight(),
                r.getBottom()));
    }

}

public class SagmoeObservableFrame2DTest {
    private Random rand = new Random();
    private static final double EPSILON = .0001;

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
    }

    /*
     * Tests the constructor on expected behavior with regards to dimensions
     * and content (should all be black pixels)
     */
    @Test
    public void constructorTest() {
        int width = 0;
        int height = 0;

        boolean failedConstruction = false;

        try {
            @SuppressWarnings("unused")
            ObservableFrame2DImpl failedFrame = new ObservableFrame2DImpl(width, height);
        } catch (IllegalFrame2DGeometryException e) {
            failedConstruction = true;
        }

        assertTrue("The constructor didn't throw the correct exception", failedConstruction);

        width = rand.nextInt(10) + 1;
        height = rand.nextInt(10) + 1;

        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(width, height);
        assertTrue("Frame width does not match input", width == frame.getWidth());
        assertTrue("Frame height does not match input", height == frame.getHeight());

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                assertTrue("There is a non-black pixel in the frame",
                        frame.getPixel(col, row).getIntensity() < (0d + EPSILON));
            }
        }
    }

    /*
     * Creates three different regions to observe, associating one observer with
     * the first region and another observer with the remaining two.
     */
    @Test
    public void basicMultipleRegionsTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(10, 10);

        Coordinate aTopLeft = new Coordinate(1, 1);
        Coordinate aBottomRight = new Coordinate(5, 5);
        Region regionA = new Region(aTopLeft, aBottomRight);

        Coordinate b1TopLeft = new Coordinate(0, 0);
        Coordinate b1BottomRight = new Coordinate(3, 3);
        Region regionB1 = new Region(b1TopLeft, b1BottomRight);

        Coordinate b2TopLeft = new Coordinate(2, 2);
        Coordinate b2BottomRight = new Coordinate(7, 7);
        Region regionB2 = new Region(b2TopLeft, b2BottomRight);

        ROIObserverImpl observerA = new ROIObserverImpl();
        ROIObserverImpl observerB = new ROIObserverImpl();

        frame.registerROIObserver(observerA, regionA);
        frame.registerROIObserver(observerB, regionB1);
        frame.registerROIObserver(observerB, regionB2);

        frame.setPixel(new Coordinate(1, 1), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        String expectedStringBase = String.format(ROIObserverImpl.TEMPLATE, 1, 1, 1, 1);

        // Because there are two observers with regions containing the point
        // above, we expect to see two calls to .println(), hence the string
        // formatting below.
        String expectedString = expectedStringBase + "\n" + expectedStringBase + "\n";
        assertEquals(expectedString, outContent.toString());

        frame.setPixel(new Coordinate(2, 2), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        expectedStringBase = String.format(ROIObserverImpl.TEMPLATE, 2, 2, 2, 2);
        // Because all three regions contain this point, we expect another three
        // calls to .println() in the observer's .notify() method.
        expectedString += expectedStringBase + "\n" + expectedStringBase + "\n" + expectedStringBase + "\n";
        assertEquals(expectedString, outContent.toString());

        frame.setPixel(new Coordinate(8, 8), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        // Because there isn't any observer with a region containing this point,
        // no additional output is expected.
        expectedString += "";
        assertEquals(expectedString, outContent.toString());
    }

    @Test
    public void advancedMultipleRegionsTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(10, 10);

        Coordinate aTopLeft = new Coordinate(1, 1);
        Coordinate aBottomRight = new Coordinate(5, 5);
        Region regionA = new Region(aTopLeft, aBottomRight);

        Coordinate b1TopLeft = new Coordinate(0, 0);
        Coordinate b1BottomRight = new Coordinate(3, 3);
        Region regionB1 = new Region(b1TopLeft, b1BottomRight);

        Coordinate b2TopLeft = new Coordinate(2, 2);
        Coordinate b2BottomRight = new Coordinate(7, 7);
        Region regionB2 = new Region(b2TopLeft, b2BottomRight);

        ROIObserverImpl observerA = new ROIObserverImpl();
        ROIObserverImpl observerB = new ROIObserverImpl();

        frame.registerROIObserver(observerA, regionA);
        frame.registerROIObserver(observerB, regionB1);
        frame.registerROIObserver(observerB, regionB2);

        // Make sure that the suspending feature functions correctly.
        frame.suspendObservable();

        frame.setPixel(new Coordinate(2, 1), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));

        // As the frame is not currently observable, we expect for there to be
        // no notification made by an observer

        String expectedString = "";
        assertEquals(expectedString, outContent.toString());

        frame.setPixel(new Coordinate(3, 2), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        assertEquals(expectedString, outContent.toString()); // same as above

        frame.setPixel(new Coordinate(4, 4), new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        assertEquals(expectedString, outContent.toString()); // same as above

        frame.resumeObservable();

        // We expect the full region from observerA
        String observerANotification = String.format(ROIObserverImpl.TEMPLATE, 2, 1, 4, 4) + "\n";
        // And truncated regions from observerB
        String observerBNotification1 = String.format(ROIObserverImpl.TEMPLATE, 2, 1, 3, 3) + "\n";
        String observerBNotification2 = String.format(ROIObserverImpl.TEMPLATE, 2, 2, 4, 4) + "\n";
        expectedString += observerANotification + observerBNotification1 + observerBNotification2;

        assertEquals(expectedString, outContent.toString());
    }

    @Test
    public void setPixelTest() {
        int size = 15;
        int width = rand.nextInt(size) + 1;
        int height = rand.nextInt(size) + 1;

        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(width, height);

        for (int i = 0; i < 100; i++) {
            Coordinate tempCoordinate = new Coordinate(rand.nextInt(width), rand.nextInt(height));

            double red = rand.nextDouble();
            double green = rand.nextDouble();
            double blue = rand.nextDouble();
            Pixel tempPixel = new ColorPixel(red, green, blue);

            // Check that the pixels are set correctly and that the return value
            // of the method is correct (i.e. the frame itself)
            assertTrue(frame == frame.setPixel(tempCoordinate, tempPixel));
            assertTrue(tempPixel == frame.getPixel(tempCoordinate));
        }
    }

    @Test
    public void basicFindObserversTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(10, 10);

        Coordinate aTopLeft = new Coordinate(1, 1);
        Coordinate aBottomRight = new Coordinate(5, 5);
        Region regionA = new Region(aTopLeft, aBottomRight);

        Coordinate b1TopLeft = new Coordinate(0, 0);
        Coordinate b1BottomRight = new Coordinate(3, 3);
        Region regionB1 = new Region(b1TopLeft, b1BottomRight);

        Coordinate b2TopLeft = new Coordinate(2, 2);
        Coordinate b2BottomRight = new Coordinate(7, 7);
        Region regionB2 = new Region(b2TopLeft, b2BottomRight);

        ROIObserverImpl observerA = new ROIObserverImpl();
        ROIObserverImpl observerB = new ROIObserverImpl();

        frame.registerROIObserver(observerA, regionA);
        frame.registerROIObserver(observerB, regionB1);
        frame.registerROIObserver(observerB, regionB2);

        Coordinate point = new Coordinate(2, 2);
        ROIObserver[] observers = frame.findROIObservers(new Region(point, point));
        ROIObserver[] expected = {observerA, observerB, observerB};


        for (int i = 0; i < observers.length; i++) {
            boolean truthiness = false;
            for (int j = 0; j < expected.length; j++) {
                if (observers[i] == expected[j]) truthiness = true;
            }
            assertTrue("The expected and found observers were not the same " + observers.length, truthiness);
        }

    }

    @Test
    public void advancedFindObserversTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(5, 5);
        Coordinate origin = new Coordinate(0, 0);
        Coordinate one = new Coordinate(1, 1);
        Coordinate two = new Coordinate(2, 2);
        Coordinate three = new Coordinate(3, 3);
        Coordinate four = new Coordinate(4, 4);

        Region zero = new Region(origin, origin);
        Region oneSquare = new Region(origin, one);
        Region twoSquare = new Region(origin, two);
        Region threeSquare = new Region(origin, three);
        Region fourSquare = new Region(origin, four);

        ROIObserver observer1 = new ROIObserverImpl();
        ROIObserver observer2 = new ROIObserverImpl();
        ROIObserver observer3 = new ROIObserverImpl();
        ROIObserver observer4 = new ROIObserverImpl();
        ROIObserver observer5 = new ROIObserverImpl();

        frame.registerROIObserver(observer1, zero);

        frame.registerROIObserver(observer2, zero);
        frame.registerROIObserver(observer2, oneSquare);

        frame.registerROIObserver(observer3, zero);
        frame.registerROIObserver(observer3, oneSquare);
        frame.registerROIObserver(observer3, twoSquare);

        frame.registerROIObserver(observer4, zero);
        frame.registerROIObserver(observer4, oneSquare);
        frame.registerROIObserver(observer4, twoSquare);
        frame.registerROIObserver(observer4, threeSquare);

        frame.registerROIObserver(observer5, zero);
        frame.registerROIObserver(observer5, oneSquare);
        frame.registerROIObserver(observer5, twoSquare);
        frame.registerROIObserver(observer5, threeSquare);
        frame.registerROIObserver(observer5, fourSquare);

        // Find all of the observers looking at (0, 0)
        ROIObserver[] found = frame.findROIObservers(new Region(origin, origin));
        ROIObserver[] expected = {
                observer1,
                observer2, observer2,
                observer3, observer3, observer3,
                observer4, observer4, observer4, observer4,
                observer5, observer5, observer5, observer5, observer5};

        for (int i = 0; i < found.length; i++) {
            assertTrue("The expected and found observers were not the same" + found.length, found[i] == expected[i]);
        }

        // Let't try it with a different point, (2, 2)
        found = frame.findROIObservers(new Region(two, two));
        ROIObserver[] newExpected = {
                observer3,
                observer4, observer4,
                observer5, observer5, observer5};

        for (int i = 0; i < found.length; i++) {
            assertTrue("The expected and found observers were not the same", found[i] == newExpected[i]);
        }

    }

    @Test
    public void unregisterByObserverTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(3, 3);
        Coordinate origin = new Coordinate(0, 0);
        Coordinate one = new Coordinate(1, 1);
        Coordinate two = new Coordinate(2, 2);

        Region zeroPoint = new Region(origin, origin);
        Region oneSquare = new Region(origin, one);
        Region onePoint = new Region(one, one);
        Region twoSquare = new Region(origin, two);
        Region twoPoint = new Region(two, two);

        ROIObserver observer1 = new ROIObserverImpl();
        ROIObserver observer2 = new ROIObserverImpl();
        ROIObserver observer3 = new ROIObserverImpl();

        frame.registerROIObserver(observer1, zeroPoint);

        frame.registerROIObserver(observer2, zeroPoint);
        frame.registerROIObserver(observer2, oneSquare);
        frame.registerROIObserver(observer2, onePoint);

        frame.registerROIObserver(observer3, zeroPoint);
        frame.registerROIObserver(observer3, oneSquare);
        frame.registerROIObserver(observer3, onePoint);
        frame.registerROIObserver(observer3, twoSquare);
        frame.registerROIObserver(observer3, twoPoint);


        frame.setPixel(origin, new ColorPixel(1d, 1d, 1d));
        String expectedOutputBase = String.format(ROIObserverImpl.TEMPLATE, 0, 0, 0, 0) + "\n";
        String expectedOutput = "";

		/*
		 * Expect 6 notifications from the following combinations of observers and regions:
		 * 		(observer1, zeroPoint)
		 * 		(observer2, zeroPoint), (observer2, oneSquare)
		 * 		(observer3, zeroPoint), (observer3, oneSquare), (observer3, twoSquare)
		 */
        for (int i = 0; i < 6; i++) {
            expectedOutput += expectedOutputBase;
        }

        assertEquals(expectedOutput, outContent.toString());
		
		/*
		 * The following two statements should unregister the following combinations:
		 * 		(observer1, zeroPoint)
		 * 		(observer3, zeroPoint), (observer3, oneSquare), (observer3, onePoint),
		 * 		(observer3, twoSquare), (observer3, twoPoint)
		 */
        frame.unregisterROIObserver(observer1);
        frame.unregisterROIObserver(observer3);

        // The combinations (observer2, zeroPoint) and (observer2, oneSquare)
        // will still be looking at this coordinate.
        frame.setPixel(origin, new ColorPixel(0d, .5, 1d));
        expectedOutput += expectedOutputBase + expectedOutputBase;

        // The combinations (observer2, oneSquare) and (observer2, onePoint)
        // will still be looking at this coordinate
        frame.setPixel(one, new ColorPixel(1d, .5, 0d));
        String newOuput = String.format(ROIObserverImpl.TEMPLATE, 1, 1, 1, 1) + "\n";
        expectedOutput += newOuput + newOuput;

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void unregisterByRegionTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(3, 3);
        Coordinate origin = new Coordinate(0, 0);
        Coordinate one = new Coordinate(1, 1);
        Coordinate two = new Coordinate(2, 2);

        Region zeroPoint = new Region(origin, origin);
        Region oneSquare = new Region(origin, one);
        Region onePoint = new Region(one, one);
        Region twoSquare = new Region(origin, two);
        Region twoPoint = new Region(two, two);

        ROIObserver observer1 = new ROIObserverImpl();
        ROIObserver observer2 = new ROIObserverImpl();
        ROIObserver observer3 = new ROIObserverImpl();

        frame.registerROIObserver(observer1, zeroPoint);

        frame.registerROIObserver(observer2, zeroPoint);
        frame.registerROIObserver(observer2, oneSquare);
        frame.registerROIObserver(observer2, onePoint);

        frame.registerROIObserver(observer3, zeroPoint);
        frame.registerROIObserver(observer3, oneSquare);
        frame.registerROIObserver(observer3, onePoint);
        frame.registerROIObserver(observer3, twoSquare);
        frame.registerROIObserver(observer3, twoPoint);


        frame.setPixel(origin, new ColorPixel(1d, 1d, 1d));
        String expectedOutputBase = String.format(ROIObserverImpl.TEMPLATE, 0, 0, 0, 0) + "\n";
        String expectedOutput = "";
		
		/*
		 * Expect 6 notifications from the following combinations of observers and regions:
		 * 		(observer1, zeroPoint)
		 * 		(observer2, zeroPoint), (observer2, oneSquare)
		 * 		(observer3, zeroPoint), (observer3, oneSquare), (observer3, twoSquare)
		 */
        for (int i = 0; i < 6; i++) {
            expectedOutput += expectedOutputBase;
        }

        assertEquals(expectedOutput, outContent.toString());
		
		/*
		 * The call to unregisterROIObservers below should remove of the following combinations:
		 * 		(observer2, oneSquare), (observer2, onePoint)
		 * 		(observer3, oneSquare), (observer3, onePoint), (observer3, twoSquare)
		 */
        frame.unregisterROIObservers(new Region(one, one));


        frame.setPixel(two, new ColorPixel(.5, .5, .5));
        // (observer3, twoPoint) should still be valid and observing
        expectedOutput += String.format(ROIObserverImpl.TEMPLATE, 2, 2, 2, 2) + "\n";


        frame.setPixel(origin, new ColorPixel(.5, .5, .5));
        // (observer1, zeroPoint), (observer2, zeroPoint), and (observer3, zeroPoint)
        // should also still be valid and observing.
        for (int i = 0; i < 3; i++) {
            // Same coordinate as above
            expectedOutput += expectedOutputBase;
        }

        assertEquals(expectedOutput, outContent.toString());


    }

    @Test
    public void indirectObservervationTest() {
        ObservableFrame2DImpl frame = new ObservableFrame2DImpl(5, 5);
        Frame2D subframe = frame.extract(new Coordinate(2, 2), new Coordinate(4, 3));
        Region testRegion = new Region(new Coordinate(1, 1), new Coordinate(4, 4));
		
		/*  01234
		 * +-----+
		 * |     | 0
		 * | ****| 1
		 * | *$$$| 2
		 * | *$$$| 3
		 * | ****| 4
		 * +-----+ 
		 * * - region
		 * $ - subframe
		 */

        ROIObserverImpl observer = new ROIObserverImpl();
        frame.registerROIObserver(observer, testRegion);
        ColorPixel testPixel = new ColorPixel(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());

        subframe.setPixel(new Coordinate(1, 0), testPixel);

        // We expect the pixel at coordinate (3,2) to have been changed by the
        // subframe's setPixel method.
        String expectedOutput = String.format(ROIObserverImpl.TEMPLATE, 3, 2, 3, 2) + "\n";
        assertEquals("The region output was not the same as the one expected",
                expectedOutput, outContent.toString());

        // We also expect the pixel we retrieve to be the same as the one we set.
        assertEquals("The pixel received from the subframe was not the same as the one given",
                testPixel, subframe.getPixel(new Coordinate(1, 0)));

        assertEquals("The pixel received from the frame was not the same as the one given",
                testPixel, frame.getPixel(new Coordinate(3, 2)));

    }

}
