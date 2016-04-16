package a3adept;

public class Test {
	public static void main(String args[]) {
		GrayPixel pix = new GrayPixel(0);
		System.out.println(pix.getIntensity());
		pix.lighten(.25);
		System.out.println(pix.getIntensity());
		ColorPixel cPix = new ColorPixel(0, 0, 0);
		System.out.println(cPix.getRed() + " " + cPix.getGreen() + " " + cPix.getBlue());
		cPix.lighten(.25);
		System.out.println(cPix.getRed() + " " + cPix.getGreen() + " " + cPix.getBlue());
		
		MutableGrayFrame2D mut = new MutableGrayFrame2D(3, 2);
		mut.darken(.4);
		
	}

}
