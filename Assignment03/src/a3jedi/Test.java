package a3jedi;

public class Test {
	public static void main(String args[]) {
		GrayPixel pix = new GrayPixel(0);
		System.out.println(pix.getIntensity());
		pix.lighten(.25);
		System.out.println(pix.getIntensity());
		ColorPixel cPix = new ColorPixel(0, 0, 0);
		System.out.println(cPix.getRed() + " " + cPix.getGreen() + " " + cPix.getBlue());
		System.out.println(cPix.lighten(.25).getRed() + " " + cPix.lighten(.25).getGreen() + " " + cPix.lighten(.25).getBlue());
		System.out.println(cPix.getRed() + " " + cPix.getGreen() + " " + cPix.getBlue());
		
		Pixel nullll = null;
		Pixel[] nullPix = {};
		
		Pixel[] pix1 = {cPix};
		ImmutableFrame2D immFrame1 = new ImmutableFrame2D(1, 1, pix1);
		//ImmutableFrame2D immFrame2 = new ImmutableFrame2D(1, 1, nullPix);
		System.out.println(immFrame1.getPixel(0, 0));
		//System.out.println(immFrame2.getPixel(0, 0));
		Pixel[] pixes = {cPix, cPix, cPix, cPix, cPix, cPix, cPix, cPix, cPix, cPix, cPix, cPix};
		ImmutableFrame2D immFrame = new ImmutableFrame2D(3, 4, pixes);
		System.out.println(immFrame.getPixel(2, 3));
		immFrame.setPixel(2, 3, cPix);
		//immFrame.setPixel(2, 3, nullPix);
		System.out.println(immFrame.getPixel(2, 3));
	}

}
