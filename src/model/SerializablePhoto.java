package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * @authors Avinash Paluri and Vishal Patel
 *
 * Class that allows for Photo to be serialized
 */

public class SerializablePhoto implements Serializable, Comparable<SerializablePhoto> {
	
    private int width, height;
	private int[][] pixels;
	
	public SerializablePhoto(Image image) {
		width = (int)image.getWidth();
		height = (int)image.getHeight();
		pixels = new int[width][height];
		
		PixelReader reader = image.getPixelReader();
		for (int currentWidth = 0; currentWidth < width; currentWidth++)
			for (int currentHeight = 0; currentHeight < height; currentHeight++)
				pixels[currentWidth][currentHeight] = reader.getArgb(currentWidth, currentHeight);
	}

	
	/** 
	 * @return Image
	 * 
	 * gets image based on the pixels
	 */
	public Image getImage() {
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter w = image.getPixelWriter();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				w.setArgb(i, j, pixels[i][j]);
		
		return image;
	}

	
	/** 
	 * @return int
	 * 
	 * gets width of image
	 */
	public int getWidth() {
		return width;
	}

	
	/** 
	 * @return int
	 * 
	 * gets height of image
	 */
	public int getHeight() {
		return height;
	}

	
	/** 
	 * @return int[][]
	 * 
	 * gets array of pixels
	 */
	public int[][] getPixels() {
		return pixels;
	}

	
	/** 
	 * @param image
	 * @return int
	 * 
	 * compares two images based on pixels
	 */
	public int compareTo(SerializablePhoto image) {
		if (width != image.getWidth() || height != image.getHeight())
			return 0;
		
		for (int currentRow = 0; currentRow < width; currentRow++)
			for (int currentColumn = 0; currentColumn < height; currentColumn++)
				if (pixels[currentRow][currentColumn] != image.getPixels()[currentRow][currentColumn])
					return 1;
		
		return -1;
	}
	
}