package com.laetienda.multimedia;

import java.awt.image.BufferedImage;

public abstract class Img {
	
	protected abstract int getHeight();
	protected abstract int getWidth();
	protected abstract BufferedImage get(int width, int height) throws MultimediaException;
	protected abstract String getMimeType();
	protected abstract String getExtension();
	
}
