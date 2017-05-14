package com.laetienda.multimedia;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class Png extends Img {

	private BufferedImage image;
	private int width;
	private int height;
	private String extension;
	
	public static void main(String[] args) {

	}
	
	protected Png(File file) throws MultimediaException{
		
		try{
			image = ImageIO.read(file);
			width = image.getWidth();
			height = image.getHeight();
		}catch(IOException ex){
			throw new MultimediaException("It was not possible to read " + file.getAbsolutePath() + " The file does not exist or it is not readable.", ex);
		}
	}
	/**
	 * Taken from the example on web page <a href="http://www.codejava.net/java-se/graphics/how-to-resize-images-in-java>link</a>
	 * @param width integer of the size of the width
	 * @param height integer that represents the height of the output image
	 * @exception MultimediaException 
	 */
	protected BufferedImage get(int width, int height) throws MultimediaException {
		
		if(width <= 0){
			width = (int)height * this.width / this.height;
			width = width <= 0 ? this.width : width;
		}
		
		if(height <= 0){
			height = (int)width * this.height / this.width;
			height = height <= 0 ? this.height : height;
		}
		
		BufferedImage result = new BufferedImage(width, height, image.getType());
		Graphics2D g2d = result.createGraphics();
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		
		return result;
	}
	
	protected String getMimeType(){
		return "image/" + extension;
	}
	
	protected String getExtension(){
		return extension;
	}
	
	
	protected int getWidth(){
		return width;
	}
	
	protected int getHeight(){
		return height;
	}

	/**
	 * @param extension the extension to set
	 */
	protected void setExtension(String extension) {
		this.extension = extension;
	}

}
