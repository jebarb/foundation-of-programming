package a4jedi.test;

import a4jedi.PixelTransformation;
import a4jedi.Threshold;
import a4jedi.GammaCorrect;

import a4jedi.Pixel;
import a4jedi.GrayPixel;

import static org.junit.Assert.*;

import org.junit.Test;

public class PixelTransforms {
	static Pixel BLACK = new GrayPixel(0d);
	static Pixel WHITE = new GrayPixel(1d);
	
	@Test
	public void testThreshold() {
		PixelTransformation xform = new Threshold(0.5d);
		
		Utils.checkGrayEquality(xform.transform(new GrayPixel(0.3d)), BLACK);
		Utils.checkGrayEquality(xform.transform(new GrayPixel(0.5d)), BLACK);
		Utils.checkGrayEquality(xform.transform(new GrayPixel(0.6d)), WHITE);
		
		boolean nullThrows = false;
		try {
			xform.transform(null);
		} catch (IllegalArgumentException exn) {
			/*
			 * NOTE: THIS IS NOT A DIRECTLY STATED PART OF THE ASSIGNMENT!
			 * 
			 * The assignment doesn's say to do nul checks inside of 
			 * PixelTransformation classes, and thus doesn't specify the exception
			 * type, but all other explicit null checks included in the assignment
			 * use this same exception type.
			 */
			nullThrows = true;
		}
		
		assertTrue(nullThrows);
		
		double badThresholds[] = {-1d, 2d};
		boolean thresholdFailures[] = new boolean[2];
		
		for (int i = 0; i < badThresholds.length; i++) {
			try {
				new Threshold(badThresholds[i]);
			} catch (RuntimeException exn) {
				/*
				 * The assignment doesn't give a specific exception that this
				 * kind of failure should throw, but it would have to be a
				 * runtime exception
				 */
				thresholdFailures[i] = true;
			}
		}
		
		Utils.all(thresholdFailures);
	}
	
	@Test
	public void testGammaCorrect() {
		PixelTransformation xformDark = new GammaCorrect(0.5);
		PixelTransformation xformLight = new GammaCorrect(2);
		
		Utils.checkGrayEquality(xformDark.transform(new GrayPixel(0.3d)), new GrayPixel(0.09d));
		Utils.checkGrayEquality(xformLight.transform(new GrayPixel(0.3d)), new GrayPixel(0.547722557));
		
		boolean nullThrows = false;
		try {
			xformLight.transform(null);
		} catch (IllegalArgumentException exn) {
			/*
			 * NOTE: THIS IS NOT A DIRECTLY STATED PART OF THE ASSIGNMENT!
			 * 
			 * The assignment doesn's say to do nul checks inside of 
			 * PixelTransformation classes, and thus doesn't specify the exception
			 * type, but all other explicit null checks included in the assignment
			 * use this same exception type.
			 */
			nullThrows = true;
		}
		
		assertTrue(nullThrows);
		
		/*
		 * If you're reading this, you're probably wondering where the invalid 
		 * values -1 and 0 come from.
		 * 
		 * Well, think about the equation:
		 * 
		 * 		  (   1   )
		 *        ( ----- )
		 *        ( gamma )
		 *     old 
		 *     
		 * 0 is invalid, since 1/0 is an undefined quantity. But why -1, or
		 * negative values in general?
		 * 
		 * WolfamAlpha helpfully notes that:
		 * 
		 *         (   1  )
		 *         ( ---- )
		 *         (  -5  )
		 *     (0.5)			~= 1.1487
		 *     
		 * Clearly, you can't have a pixel with a brightness over 1, so that
		 * means that negative values are invalid - they grow the brightness so 
		 * much that it gets out of range.
		 * 
		 * Bizarrely, Wolfram Alpha doesn't know the mass of a whale. Try it:
		 * http://www.wolframalpha.com/input/?i=mass+of+a+whale.
		 * 
		 * Now you know why this test fails, even though the assignment says
		 * nothing about this case. Now back to your regularly scheduled testing
		 * code.
		 */
		
		double badGammas[] = {-1d, 0d};
		boolean gammaFailures[] = new boolean[2];
		
		for (int i = 0; i < badGammas.length; i++) {
			try {
				new GammaCorrect(badGammas[i]);
			} catch (RuntimeException exn) {
				/*
				 * The assignment doesn't give a specific exception that this
				 * kind of failure should throw, but it would have to be a
				 * runtime exception
				 */
				gammaFailures[i] = true;
			}
		}
		
		Utils.all(gammaFailures);
	}
}
