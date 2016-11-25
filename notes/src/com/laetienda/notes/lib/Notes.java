package com.laetienda.notes.lib;

import java.io.File;

import com.laetienda.tomcat.lib.Service;

public class Notes{
	
	private final String WIN_DIRECTORY = "C:\\Users\\i849921\\git\\devs\\NotesApp\\bin";

	String directory;
	
	Service tomcat;
	
	public Notes() throws Exception{
		super();
		directory = System.getProperty("user.dir") + File.separator + "..";

		directory = WIN_DIRECTORY + File.separator + ".."; //for test proposes, directory will be replaced with the fine windirectory
		tomcat = new Service();
	}
	
	public Service getTomcat() {
		return tomcat;
	}

	public void setTomcat(Service tomcat) {
		this.tomcat = tomcat;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
}
