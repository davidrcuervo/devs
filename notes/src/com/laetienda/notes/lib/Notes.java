package com.laetienda.notes.lib;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Notes{
	
	//private final String DIRECTORY = "C:\\Users\\i849921\\git\\devs\\NotesApp\\bin";
	private final String DIRECTORY = "/home/myself/dev/NotesApp/bin";

	private String directory;
	private String pathToNotes;
	private String pandoc;
	
	public Notes() throws Exception{
		
		directory = System.getProperty("user.dir") + File.separator + "..";
		directory = DIRECTORY + File.separator + ".."; //for test proposes, directory will be replaced with the fine windirectory
		pathToNotes = directory + File.separator + "var" + File.separator + "notes";
		pandoc = "pandoc";
		loadConfFile(directory);	
	}
	
	public Properties loadConfFile(String dirPath) throws Exception{
		
		FileInputStream conf;
		Properties settings = new Properties();
		
		File confFile = new File(dirPath + File.separator + "etc" + File.separator + "notes" + File.separator + "conf.xml");
		conf = new FileInputStream(new File(confFile.getAbsolutePath()));
		settings.loadFromXML(conf);
		
		if(settings.containsKey("pathToNotes")){
			
			setPathToNotes(settings.getProperty("pathToNotes"));
			//System.out.println(settings.getProperty("pathToNotes"));
		}
		
		if(settings.containsKey("pathToNotes")){
			pandoc = settings.getProperty("pandoc");
			//System.out.println(settings.getProperty("pandoc"));
		}
		
		return settings;
	}
	
	public void setPathToNotes(String path){
		pathToNotes = path;
	}
	
	public String getPathToNotes(){
		return pathToNotes;
	}
	
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) throws Exception{
		this.directory = directory;
		loadConfFile(directory);
	}

	public String getPandoc() {
		return pandoc;
	}

	public void setPandoc(String pandoc) {
		this.pandoc = pandoc;
	}
}
