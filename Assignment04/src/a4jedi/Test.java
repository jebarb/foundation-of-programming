package a4jedi;

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
		
		GrayPixel pix2 = new GrayPixel(0);
		System.out.println(pix2.getIntensity());
		pix2.lighten(.25);
		System.out.println(pix2.getIntensity());
		ColorPixel cPix2 = new ColorPixel(0, 0, 0);
		System.out.println(cPix2.getRed() + " " + cPix2.getGreen() + " " + cPix2.getBlue());
		cPix.lighten(.25);
		System.out.println(cPix2.getRed() + " " + cPix2.getGreen() + " " + cPix2.getBlue());
		
		MutableGrayFrame2D mut = new MutableGrayFrame2D(3, 2);
		mut.darken(.4);
		mut.setGrayPixel(1,  1,  new GrayPixel(.5));
		
		Pixel nullll = null;
		Pixel[] nullPix = {};
		PixelTransformation xform = new GammaCorrect(100);
		Frame2D xformed = new TransformedFrame2D(mut, xform);
		System.out.println(xformed.getPixel(1,1).getIntensity());
		System.out.println(xformed.getPixel(1,1).getIntensity());
		System.out.println(xformed.getPixel(1,1).getIntensity());
		
	}

}
