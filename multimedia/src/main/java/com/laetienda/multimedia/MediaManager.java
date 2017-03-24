package com.laetienda.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class MediaManager {
	
	private File directory;
	private Properties settings;
	
	public MediaManager(File directory) throws MultimediaException {
		
		this.directory = directory;
		settings = loadSettings(directory);
	}
	
	public synchronized Image createImage(String[] args) throws MultimediaException {
		return new Image(this, args);
	}
	
	protected String getSetting(String key){
		return settings.getProperty(key);
	}
	
	protected File getDirectory(){
		return directory;
	}
	
	private Properties loadSettings(File directory) throws MultimediaException {
		Properties settings = null;
		String path = "";
		
		try{
			path = directory.getAbsolutePath() + File.separator + "etc" + File.separator + "multimedia" + File.separator + "conf.xml";
			
			Properties defaults = new Properties();
			defaults.setProperty("images_folder", directory.getAbsolutePath() + File.separator + "var" + File.separator + "multimedia" + File.separator + "images");
			defaults.setProperty("videos_folder", directory.getAbsolutePath() + File.separator + "var" + File.separator + "multimedia" + File.separator + "videos");
			defaults.setProperty("temp_folder", directory.getAbsolutePath() + File.separator + "var" + File.separator + "multimedia" + File.separator + "temp");
			
			settings = new Properties(defaults);
			
			FileInputStream conf = new FileInputStream(new File(path));
			settings.loadFromXML(conf);
		
		}catch (FileNotFoundException ex){
			throw new MultimediaException("The " + path + " does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading", ex);
		}catch (SecurityException ex){
			throw new MultimediaException("Security manager exists and its checkRead method denies read access to " + path, ex);
		}catch (InvalidPropertiesFormatException ex){
			throw new MultimediaException("Data on input stream does not constitute a valid XML document with the mandated document type.", ex);
		}catch (IOException ex){
			throw new MultimediaException("Reading from the specified input stream results in an IOException", ex);
		}
		
		return settings;
	}
}
