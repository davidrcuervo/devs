package com.laetienda.multimedia;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import org.w3c.dom.Document;

public class Svg extends Img{
	
	private final String MIME_TYPE = "image/png";
	
	private File file;
	private int height;
	private int width;
	//private String extension;
	
	public static void main(String[] args) {
		
		try{
			String dir = "/home/myself/git/eclipse/Web.opt";
			File directory = new File(dir);
			MediaManager mm = new MediaManager(directory);
			String[] args2 = {"images", "Web_mokup.svg"};
			Image image = mm.createImage(args2);
			image.get(10, 10);
		}catch (MultimediaException ex){
			ex.getParent().printStackTrace();
		}
	}
	
	protected Svg(File file) throws MultimediaException{
		
		try{
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			Document doc = f.createDocument(file.toURI().toString());
			
			height = Integer.parseInt(doc.getDocumentElement().getAttribute("height"));
			width = Integer.parseInt(doc.getDocumentElement().getAttribute("width"));
		
		}catch(NumberFormatException ex){
			throw new MultimediaException("Error while getting size of image", ex);
		}catch(IOException ex){
			throw new MultimediaException("Error While reading file", ex);
		}finally{
			this.file = file;
		}
	}
	
	protected BufferedImage get(int width, int height) throws MultimediaException{
		
		BufferedImage result = null;
		
		PNGTranscoder t = new PNGTranscoder();
		
		if(width > 0)
			t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float)width);
		
		if(height > 0)
			t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float)height);
		
		TranscoderInput input = new TranscoderInput(file.toURI().toString());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(outputStream);
		
		try{
			t.transcode(input, output);
			result = ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
			outputStream.close();
		}catch(TranscoderException ex){
			throw new MultimediaException("Error while transcoding from the image " + file.getAbsolutePath() + " to PNG", ex);
		} catch (IOException ex) {
			throw new MultimediaException("Error while reading the output stream", ex);
		}
		
		return result;
	}
	
	protected String getMimeType(){
		return MIME_TYPE;
	}
	
	protected String getExtension(){
		return "png";
	}
	
	protected int getHeight(){
		return height;
	}
	
	protected int getWidth(){
		return width;
	}

	/**
	 * @param extension the extension to set
	 */
	protected void setExtension(String extension) {
		//this.extension = extension;
	}
}
