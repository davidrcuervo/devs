package com.laetienda.tomcat.lib;

import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.catalina.Context;

public class Service {
	
	private final String WIN_DIRECTORY = "C:\\Users\\i849921\\git\\devs\\NotesApp\\bin";
	
	Tomcat tomcat = new Tomcat();
	String directory;
	Integer port;
	Context context;
	
	public Service() throws Exception{
		directory = System.getProperty("user.dir") + File.separator + "..";
		directory = WIN_DIRECTORY + File.separator + ".."; //This line is for testing propouses
		
		port = 8080;
		loadConfFile(directory);
		tomcat = new Tomcat();
	}
	
	public void start(){
		tomcat.setPort(port);
		tomcat.setBaseDir(directory);
		tomcat.getHost().setAppBase(directory);
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		
		tomcat.getServer().await();
	}
	
	public Properties loadConfFile(String dirPath) throws Exception{
		
		FileInputStream conf;
		Properties settings = new Properties();
		
		File confFile = new File(dirPath + File.separator + "etc" + File.separator + "tomcat" + File.separator + "conf.xml");
		conf = new FileInputStream(new File(confFile.getAbsolutePath()));
		settings.loadFromXML(conf);
		
		if(settings.containsKey("port")){
			setPort(Integer.parseInt(settings.getProperty("port")));
		}
		
		return settings;
	}
	
	public void stop() throws Exception{
		tomcat.getServer().stop();
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
}
