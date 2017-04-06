package com.laetienda.multimedia;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class Image {
	
	private static final String[] TYPES = {"png", "jpg", "svg"};
	
	private File file;				//Image file
	private int type;
	private Img img;
	private int height;
	private int width;
	
	protected Image(File file) throws MultimediaException {
		
		height = -1;
		width = -1;
		this.file = file;
		type = getImageType(file);
		img = createImg(type);
	}
	
	public BufferedImage get() throws MultimediaException{
		
		return img.get(width, height);
	}
	
	public BufferedImage get(int width, int height) throws MultimediaException {
		
		setWidth(width);
		setHeight(height);
		return get();
	}
	
	public BufferedImage get(String width, String height) throws MultimediaException{
		
		try{
			setWidth(Integer.parseInt(width));
		}catch (NumberFormatException ex){
			setWidth(-1);
		}
		
		try{
			setHeight(Integer.parseInt(height));
		}catch (NumberFormatException ex){
			setHeight(-1);
		}
		
		return get();
	}
	
	public String getMimeType(){
		return img.getMimeType();
	}
	
	public String getExtension(){
		return img.getExtension();
	}
	
	/**
	 * @param height the height to set
	 * @throws MultimediaException 
	 */
	public void setHeight(int height){
		this.height = height;
	}

	/**
	 * @param width the width to set
	 * @throws MultimediaException 
	 */
	public void setWidth(int width){
		this.width = width;
	}
	
	private int getImageType(File file) throws MultimediaException {
		
		String name = file.getName();
		int dot = name.lastIndexOf('.');
		
		if(dot == -1){
			throw new MultimediaException(name + "is not a valid image file name");
		}
		
		return Arrays.asList(TYPES).indexOf(name.substring(dot + 1));
	}
	
	private Img createImg(int type) throws MultimediaException{
		
		Img result = null;
		
		try{
			String imgType = TYPES[type];
		
			switch (imgType){
				
				case "svg":
					result = new Svg(file);
					break;
				
				case "jpg":	
				case "png":
					result = new Png(file);
					result.setExtension(imgType);
					break;
					
				default:
					throw new MultimediaException("The image extension is not supported by this application");
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			throw new MultimediaException("The index " + type + " is out of range of the TYPES array.", ex);
		}
		
		return result;
	}
}
