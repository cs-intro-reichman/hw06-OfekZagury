// This class uses the Color class, which is part of a package called awt,
// which is part of Java's standard class library.
import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
		String fileName = args[0];	
		Color[][] testImage = read(fileName);
		System.out.println("print");
		print(testImage);
		System.out.println();
		System.out.println("flippedHorizontally");
		print(flippedHorizontally(testImage));
		System.out.println();
		System.out.println("flippedVertically");
		print(flippedVertically(testImage));
		System.out.println();
		System.out.println("grayScaled");
		print(grayScaled(testImage));
		System.out.println();
		System.out.println("scaled");
		print(scaled(testImage, 3, 5));
		System.out.println();
		print(blend(new Color(255, 255, 255), new Color(255, 255, 255), 0.5));
		
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		Color[][] image = new Color[numRows][numCols];
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				image[i][j] = new Color(in.readInt(), in.readInt(), in.readInt());
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	
	private static void print(Color[][] image) {
		for (int i = 0; i < image.length; i++) {
			System.out.println();
			for (int j = 0; j < image[i].length; j++) {
				print(image[i][j]);
			}
		}
	}
	
	
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] flippedHorizontally = new Color[image.length][image[0].length];
		for (int i = 0; i < flippedHorizontally.length; i++) {
			for (int j = 0; j < flippedHorizontally[i].length; j++) {
				flippedHorizontally[i][j] = image[i][image[i].length - j - 1];
			}
		} 
		return flippedHorizontally;
	}
	
	
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] flippedVertically = new Color[image.length][image[0].length];
		for (int i = 0; i < flippedVertically.length; i++) {
			for (int j = 0; j < flippedVertically[i].length; j++) {
				flippedVertically[i][j] = image[image.length - i - 1][j];
			}
		} 
		return flippedVertically;
	}
	
	public static Color luminance(Color pixel) {
		int lum = (int)(pixel.getRed() * 0.299 + pixel.getGreen() * 0.587 + pixel.getBlue() * 0.114);
		Color luminance = new Color(lum, lum, lum);
		return luminance;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] grayScaled = new Color[image.length][image[0].length];
		for (int i = 0; i < grayScaled.length; i++) {
			for (int j = 0; j < grayScaled[i].length; j++) {
				grayScaled[i][j] = luminance(image[i][j]);
			}
		} 
		return grayScaled;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		double w0 = (double) (image[0].length);
		double h0 = (double) (image.length);
		double w = (double) (width);
		double h = (double) (height);
		double h0Divh = (double) (h0 / h);
		double w0Divw = (double) (w0 / w);
		Color[][] scaled = new Color[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				scaled[i][j] = image[(int) (i * h0Divh)][(int) (j * w0Divw)];
			}
		} 
		return scaled;
		
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {

		int redOfBlend = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int greenOfBlend = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int blueOfBlend = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
		Color blend = new Color(redOfBlend, greenOfBlend, blueOfBlend);
		return blend;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blend = new Color[image1.length][image1[0].length];
		for (int i = 0; i < blend.length; i++) {
			for (int j = 0; j < blend[i].length; j++) {
				blend[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		} 	
		return blend;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		target = scaled(target, source[0].length, source.length);
		Color[][] morph = new Color[source.length][source[0].length];
		morph = source;
		double theN = (double) (n);
		double nMinusi;
		for (int i = 0; i <= n; i++) {
			nMinusi = (double) (n-i);
			morph = blend(source, target, (double) (nMinusi / theN));
			Runigram.display(morph);
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

