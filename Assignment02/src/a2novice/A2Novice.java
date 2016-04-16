package a2novice;
import java.util.Scanner;

import a2.Picture;
import a2.Pixel;

public class A2Novice {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		//Create picture from input
		Picture p = readPictureFromScanner(s);
		//Print picture to console
		printPicture(p);
	}
	
	/**
	 * Create picture object from scanner
	 * @param s Scanner object polulated from cmd line args
	 * @return Picture object from scanner values
	 */
	public static Picture readPictureFromScanner(Scanner s) {
		//First two values from scanner are width and height
		Picture p = new Picture(s.nextInt(), s.nextInt());
		//Populate picture with pixel intensities from scanner
		for (int i = 0; i < p.getHeight(); i++) {
			for (int j = 0; j < p.getWidth(); j++) {
				p.setPixel(j, i, new Pixel(s.nextInt()));
			}
		}
		return p;
	}
	
	/**
	 * Print picture object to console
	 * @param p Picture object to be printed
	 */
	public static void printPicture(Picture p) {
		for (int i = 0; i < p.getHeight(); i++) {
			for (int j = 0; j < p.getWidth(); j++) {
				System.out.print(p.getPixel(j, i).asChar());
			}
			System.out.print("\n");
		}
	}
}