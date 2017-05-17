package com.laetienda.notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class NotesManager {
	
	//private File directory;
	private Properties settings;
	
	public NotesManager(File directory) throws NotesException {
		
		//this.directory = directory;
		settings = loadSettings(directory);
	}
	
	protected String getSetting(String key){
		return settings.getProperty(key);
	}
	
	private Properties loadSettings(File directory) throws NotesException {
		Properties settings = null;
		String path = "";
		
		try{
			path = directory.getAbsolutePath() + File.separator + "etc" + File.separator + "notes.conf.xml";
			
			Properties defaults = new Properties();
			defaults.setProperty("pathToNotes", directory.getAbsolutePath() + File.separator + "var" + File.separator + "notes");
			defaults.setProperty("pandoc", directory.getAbsolutePath() + File.separator + "lib" + File.separator + "pandoc");
			
			settings = new Properties(defaults);
			
			FileInputStream conf = new FileInputStream(new File(path));
			settings.loadFromXML(conf);
		
		}catch (FileNotFoundException ex){
			throw new NotesException("The " + path + " does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading", ex);
		}catch (SecurityException ex){
			throw new NotesException("Security manager exists and its checkRead method denies read access to " + path, ex);
		}catch (InvalidPropertiesFormatException ex){
			throw new NotesException("Data on input stream does not constitute a valid XML document with the mandated document type.", ex);
		}catch (IOException ex){
			throw new NotesException("Reading from the specified input stream results in an IOException", ex);
		}
		
		return settings;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
