package a2jedi;

import a2.Picture;
import a2.Pixel;
import java.util.Scanner;

public class A2Jedi {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Picture p = a2novice.A2Novice.readPictureFromScanner(s);
		a2novice.A2Novice.printPicture(averageIntensity(p));
	}

	//Find pixel in given relation to original pixel
	public static int right(Picture p, int width, int height) {
		return p.getPixel(width + 1, height).getIntensity();
	}

	public static int left(Picture p, int width, int height) {
		return p.getPixel(width - 1, height).getIntensity();
	}

	public static int above(Picture p, int width, int height) {
		return p.getPixel(width, height - 1).getIntensity();
	}

	public static int below(Picture p, int width, int height) {
		return p.getPixel(width, height + 1).getIntensity();
	}

	public static int aboveRight(Picture p, int width, int height) {
		return p.getPixel(width + 1, height - 1).getIntensity();
	}

	public static int aboveLeft(Picture p, int width, int height) {
		return p.getPixel(width - 1, height - 1).getIntensity();
	}

	public static int belowRight(Picture p, int width, int height) {
		return p.getPixel(width + 1, height + 1).getIntensity();
	}

	public static int belowLeft(Picture p, int width, int height) {
		return p.getPixel(width - 1, height + 1).getIntensity();
	}

	/**
	 * @param p Picture object
	 * @return Picture with same dimensions as p, with each pixel 
	 * 			representing the average value of surrounding pixels
	 */
	public static Picture averageIntensity(Picture p) {
		final int MIN_INDEX = 0;
		final int MAX_WIDTH_INDEX = p.getWidth() - 1;
		final int MAX_HEIGHT_INDEX = p.getHeight() - 1;
		Picture newPic = new Picture(p.getWidth(), p.getHeight());
		for (int height = 0; height < p.getHeight(); height++) {
			for (int width = 0; width < p.getWidth(); width++) {
				//Monolith to find location of pixel in picture
				//Checks index to find edges, calls appropriate pixel finders
				//and computes average between them, will then assign average 
				//value to pixel at original location in newPic
				if (height == MIN_INDEX) {
					if (height == MAX_HEIGHT_INDEX) {
						if (width == MIN_INDEX) {
							if (height == MAX_HEIGHT_INDEX) {
								//ONE PIXEL
								return p;
							} else {
								//HEIGHT ONE PIXEL, LEFT
								Pixel pix = new Pixel(right(p, width, height));
								newPic.setPixel(width, height, pix);
							}
						} else if (width == MAX_WIDTH_INDEX) {
							//HEIGHT ONE PIXEL, RIGHT
							Pixel pix = new Pixel(left(p, width, height));
							newPic.setPixel(width, height, pix);
						} else {
							//HEIGHT ONE PIXEL, CENTER
							final int NUM_PIXELS = 2;
							Pixel pix = new Pixel(left(p, width, height) +
									right(p, width, height) / NUM_PIXELS);
							newPic.setPixel(width, height, pix);
						}
					} else if (width == MIN_INDEX) {
						if (width == MAX_WIDTH_INDEX) {
							//WIDTH ONE PIXEL, TOP
							Pixel pix = new Pixel(below(p, width, height));
							newPic.setPixel(width, height, pix);
						} else {
							//TOP LEFT CORNER
							final int NUM_PIXELS = 3;
							Pixel pix = new Pixel((right(p, width, height) + 
									below(p, width, height) +
									belowRight(p, width, height)) / NUM_PIXELS);
							newPic.setPixel(width, height, pix);
						}
					} else if (width == MAX_WIDTH_INDEX) {
						//TOP RIGHT CORNER
						final int NUM_PIXELS = 3;
						Pixel pix = new Pixel((left(p, width, height) +
								below(p, width, height) +
								belowLeft(p, width, height)) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					} else {
						//TOP EDGE
						final int NUM_PIXELS = 5;
						Pixel pix = new Pixel((right(p, width, height) + 
								left(p, width, height) +
								below(p, width, height) +
								belowRight(p, width, height) +
								belowLeft(p, width, height)) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					}
				} else if (height == MAX_HEIGHT_INDEX) {
					if (width == MIN_INDEX) {
						if (width == MAX_WIDTH_INDEX) {
							//WIDTH ONE PIXEL, BOTTOM
							Pixel pix = new Pixel(above(p, width, height));
							newPic.setPixel(width, height, pix);
						} else {
							//BOTTOM LEFT CORNER
							final int NUM_PIXELS = 3;
							Pixel pix = new Pixel((right(p, width, height) +
									above(p, width, height) +
									aboveRight(p, width, height)) / NUM_PIXELS);
							newPic.setPixel(width, height, pix);
						}
					} else if (width == MAX_WIDTH_INDEX) {
						//BOTTOM RIGHT CORNER
						final int NUM_PIXELS = 3;
						Pixel pix = new Pixel((left(p, width, height) +
								above(p, width, height) + 
								aboveLeft(p, width, height)) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					} else {
						//BOTTOM EDGE
						final int NUM_PIXELS = 5;
						Pixel pix = new Pixel((right(p, width, height) + 
								left(p, width, height) +
								above(p, width, height) +
								aboveRight(p, width, height) +
								aboveLeft(p, width, height)) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					}
				} else {
					if (width == MIN_INDEX) {
						if (width == MAX_WIDTH_INDEX) {
							//WIDTH ONE PIXEL, CENTER
							final int NUM_PIXELS = 2;
							Pixel pix = new Pixel((above(p, width, height) +
									below(p, width, height)) / NUM_PIXELS);
							newPic.setPixel(width, height, pix);
						} else {
							//LEFT EDGE
							final int NUM_PIXELS = 5;
							Pixel pix = new Pixel((right(p, width, height) + 
									above(p, width, height) +
									below(p, width, height) +
									belowRight(p, width, height) +
									aboveRight(p, width, height)) / NUM_PIXELS);
							newPic.setPixel(width, height, pix);
						}
					} else if (width == MAX_WIDTH_INDEX) {
						//RIGHT EDGE
						final int NUM_PIXELS = 5;
						Pixel pix = new Pixel((left(p, width, height) +
								above(p, width, height) +
								below(p, width, height) +
								belowLeft(p, width, height) +
								aboveLeft(p, width, height)) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					} else {
						//CENTER
						final int NUM_PIXELS = 8;
						Pixel pix = new Pixel((right(p, width, height) + 
								left(p, width, height) +
								above(p, width, height) +
								below(p, width, height) +
								belowRight(p, width, height) +
								belowLeft(p, width, height) +
								aboveRight(p, width, height) +
								aboveLeft(p, width, height)
								) / NUM_PIXELS);
						newPic.setPixel(width, height, pix);
					}
				}
			}
		}
		return newPic;
	}
	
}
