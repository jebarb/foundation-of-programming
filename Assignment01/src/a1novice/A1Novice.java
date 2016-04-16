package a1novice;

import java.util.Scanner;

public class A1Novice {

	// Do not change the main method.	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		process(s);
	}
	
	/*
	 * Process command line args to print out image
	 * @param Scanner s
	 * 			Scanner object, formatted as integers between 0-99 inclusive
	 * 			First two values are width and height 
	 */
	public static void process(Scanner s) {
		//Process first two inputs as width and height
		final int WIDTH = s.nextInt();
		final int HEIGHT = s.nextInt();
		//Assign lowest and highest possible values to max and min
		int max = 0;
		int min = 100;
		//Initialize avg for running sum, count for number of pixels
		double avg = 0;
		int count = 0;
		//First loop creates rows by adding new line
		for (int i = 0; i < HEIGHT; i++) {
			//Nested loop prints each pixel, exits at appropriate width
			for (int j = 0; j < WIDTH; j++) {
				int intensity = s.nextInt();
				count++;
				avg += intensity;
				//Detect if given intensity is a max or min
				if (intensity > max) {
					max = intensity;
				}
				if (intensity < min) {
					min = intensity;
				}
				//String initialized for storing and printing pixel
				//Pixel type detected using block of if/else if statements
				//One pixel printed per loop
				String pixel = null;
				if (intensity < 10) {
					pixel = "#";
				} else if (intensity < 20) {
					pixel = "E";
				} else if (intensity < 30) {
					pixel = "X";
				} else if (intensity < 40) {
					pixel = "O";
				} else if (intensity < 50) {
					pixel = "\\";
				} else if (intensity < 60) {
					pixel = "/";
				} else if (intensity < 70) {
					pixel = "+";
				} else if (intensity < 80) {
					pixel = "-";
				} else if (intensity < 90) {
					pixel = "|";
				} else { 
					pixel = " ";
				}
				System.out.print(pixel);			
			}
			System.out.print("\n");
		}	
		//Calculate average and print max, min, avg
		avg /= (double)count;
		System.out.println("min Value = " + min);
		System.out.println("max Value = " + max);
		System.out.println("Average Value = " + avg);
		return;
	}	
}
