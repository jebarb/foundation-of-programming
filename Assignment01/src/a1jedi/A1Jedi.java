package a1jedi;

import java.util.Arrays;
import java.util.Scanner;

public class A1Jedi {

	// Do not change the main method.	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		process(s);

	}

	/*
	 * Process command line args to print out vertical histogram of pixel intensity
	 * @param Scanner s
	 * 			Scanner object, formatted as integers between 0-99 inclusive
	 * 			First three values are bin width, width, and height 
	 */
	public static void process(Scanner s) {
		//Assign first three values and useful values to final ints
		final int BIN_WIDTH = s.nextInt();
		final int WIDTH = s.nextInt();
		final int HEIGHT = s.nextInt();
		final int MAX = 100;
		int maxPercent = 0;
		//Array for storing percentages for each pixel
		int[] pixelPercent = new int[(int)Math.ceil((double)MAX / (double)BIN_WIDTH)];
		//Outer loop runs through scanner inputs
		for (int i = 0; i < (WIDTH * HEIGHT); i++) {
			int nextInt = s.nextInt();
			//Inner loop increments respective position of array for each occurence of intensity
			for (int j = 0; j < pixelPercent.length; j++) {
				if (nextInt < (BIN_WIDTH * (j + 1))) {
					pixelPercent[j]++;
					break;
				}
			}
		}
		//Convert occurrences to percentages, detect max percentage
		for (int i = 0; i < pixelPercent.length; i++) {
			pixelPercent[i] = (int)Math.round((double)pixelPercent[i] / (double)(WIDTH * HEIGHT) * 100.0);
			if (pixelPercent[i] > maxPercent) {
				maxPercent = pixelPercent[i];
			}
		}
		//2d array for ease of formatting
		String[][] histogram = new String[maxPercent][pixelPercent.length];
		//Assign * for each percentage point, space for empty value
		for (int i = 0; i < maxPercent; i++) {
			for (int j = 0; j < pixelPercent.length; j++) {
				if (pixelPercent[j] >= maxPercent - i) {
					histogram[i][j] = "*";
				} else {
					histogram[i][j] = " ";
				}
			}
		}
		//Print histogram bars
		for (int i=0; i < maxPercent; i++) {
			for (int j = 0; j < pixelPercent.length; j++) {
				System.out.print(histogram[i][j]);
			}
			System.out.print("\n");
		}
		//Print separator
		for (int i = 0; i < pixelPercent.length; i++) {
			System.out.print("-");
		}
		//Print first digit of bin value
		System.out.print("\n");
		for (int i = 0; i < pixelPercent.length; i++) {
			int num = (BIN_WIDTH * (i)) / 10;
			if (num == 0) {
				System.out.print(" ");
			} else {
				System.out.print(num);
			}
		}
		//Print second digit of bin value
		System.out.print("\n");
		for (int i = 0; i < pixelPercent.length; i++) {
			int num = (BIN_WIDTH * (i)) % 10;
			System.out.print(num);
		}
		return;
	}
}
