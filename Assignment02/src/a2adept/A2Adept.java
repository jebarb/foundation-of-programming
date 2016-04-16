package a2adept;

import a2novice.A2Novice;
import a2.Picture;
import a2.Pixel;
import java.util.Scanner;

public class A2Adept {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Picture p = a2novice.A2Novice.readPictureFromScanner(s);
		a2novice.A2Novice.printPicture(removeWhiteSpace(p));
	}
	
	/**
	 * Improve readability of removeColumns and removeRows methods
	 * Remove all rows and columns that are all whitespace
	 * @param p Picture object to be cleaned of white rows/columns
	 * @return Picture with no rows or columns of white space
	 */
	public static Picture removeWhiteSpace(Picture p) {
		return removeColumns(removeRows(p));
	}

	/**
	 * Remove rows comprised of all white space
	 * @param p Picture with white space to be removed
	 * @return Picture with no rows of whitespace
	 */
	public static Picture removeRows(Picture p) {
		//Array used to convert new hight values to corresponding original rows
		int[] rows = new int[p.getHeight()];
		//Counter to tally new number of rows
		int newHeight = 0;
		for (int i = 0; i < p.getHeight(); i++) {
			//Set false if non-whitespace detected
			boolean isWhite = true;
			for (int j = 0; j < p.getWidth(); j++) {
				if (p.getPixel(j, i).getIntensity() < 90) {
					isWhite = false;
				}
			}
			if (!isWhite) {
				//Translator for new to old rows
				rows[newHeight] = i;
				newHeight++;
			}
		}
		//Create new picture with new height, removed whitespace rows
		Picture newPic = new Picture(p.getWidth(), newHeight);
		for (int i = 0; i < newHeight; i++) {
			for (int j = 0; j < p.getWidth(); j++) {
				newPic.setPixel(j, i, p.getPixel(j, rows[i]));
			}
		}
		return newPic;
	}

	/**
	 * Remove columns of whitespace from picture
	 * @param p Picture to be processed
	 * @return Picture with no whitespace columns
	 */
	public static Picture removeColumns(Picture p) {
		//Array used to convert old column numbers to new column locations
		int[] columns = new int[p.getWidth()];
		//Counter to tally new width
		int newWidth = 0;
		for (int i = 0; i < p.getWidth(); i++) {
			//Set false if non-whitespace pixel detected
			boolean isWhite = true;
			for (int j = 0; j < p.getHeight(); j++) {
				if (p.getPixel(i, j).getIntensity() < 90) {
					isWhite = false;
				}
			}
			if (!isWhite) {
				//Translate old column location to new
				columns[newWidth] = i;
				newWidth++;
			}
		}
		//Create new picture with no whitespace columns
		Picture newPic = new Picture(newWidth, p.getHeight());
		for (int i = 0; i < newPic.getHeight(); i++) {
			for (int j = 0; j < newWidth; j++) {
				newPic.setPixel(j, i, p.getPixel(columns[j], i));
			}
		}
		return newPic;
	}
}

