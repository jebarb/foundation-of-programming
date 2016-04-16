package a1adept;

import java.util.Scanner;
import java.util.Arrays;

public class A1Adept {
	
	// Do not change the main method.	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		process(s);
		
	}
	
	/*
	 * Process command line args to print out horizontal histogram of pixel intensity
	 * @param Scanner s
	 * 			Scanner object, formatted as integers between 0-99 inclusive
	 * 			First three values are bin width, width, and height 
	 */
	public static void process(Scanner s) {
		//Final ints for dimensions and useful values
		final int BIN_WIDTH = s.nextInt();
		final int WIDTH = s.nextInt();
		final int HEIGHT = s.nextInt();
		final int MAX = 100;
		//Array for string number of occurences of pixel intensities
		int[] histogram = new int[(int)Math.ceil((double)MAX / (double)BIN_WIDTH)];
		//Outer loop runs through scanner
		for (int i = 0; i < (WIDTH * HEIGHT); i++) {
			int nextInt = s.nextInt();
			//Inner loop finds intensity of pixel and increments associated array value
			for (int j = 0; j < histogram.length; j++) {
				if (nextInt < (BIN_WIDTH * (j + 1))) {
					histogram[j]++;
					break;
				}
			}
		}
		//Print bin value, convert bin occurences to percentage, print
		for (int i = 0; i < histogram.length; i++) {
			System.out.printf("%2d:", i * BIN_WIDTH);
			int percentage = (int)Math.round((double)histogram[i] / (double)(WIDTH * HEIGHT) * 100.0);
			for (int j = 0; j < percentage; j++) {
				System.out.print("*");
			}
			System.out.print("\n");
		}
		return;		
	}

}
