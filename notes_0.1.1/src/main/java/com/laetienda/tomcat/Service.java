package com.laetienda.tomcat;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.servlet.ServletException;

import java.net.Socket;

public class Service {
	
	private Tomcat tomcat = new Tomcat();
	private File directory;
	private Integer port;
	private Integer shutdownPort;
	private String shutdown;
	
	public Service(File directory) throws TomcatException{
		
		this.directory = directory; 
		port = 8080;
		shutdownPort = 8081;
		shutdown = "shutdown";
		loadConfFile();
		tomcat = new Tomcat();
	}
	
	public void start() throws TomcatException{
		tomcat.setPort(port);
		tomcat.setBaseDir(directory.getAbsolutePath());
		tomcat.getHost().setAppBase(directory.getAbsolutePath());
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		tomcat.getHost().setAppBase(directory.getAbsolutePath() + File.separator + "WebContent");
		
		try{
			Context context = tomcat.addWebapp("", directory.getAbsolutePath() + File.separator + "WebContent");
			context.addParameter("directory", directory.getAbsolutePath());
			tomcat.getServer().setPort(shutdownPort);
			tomcat.getServer().setShutdown(shutdown);
			tomcat.start();
		}catch(ServletException ex){
			throw new TomcatException("Failed to start tomcat", ex);
		}catch(LifecycleException ex){
			throw new TomcatException("Failed to start tomcat", ex);
		}
	}
	
	public Properties loadConfFile() throws TomcatException{
		
		FileInputStream conf;
		Properties settings = new Properties();
		
		File confFile = new File(directory.getAbsolutePath() + File.separator + "etc" + File.separator + "tomcat.conf.xml");
		
		try{
			conf = new FileInputStream(new File(confFile.getAbsolutePath()));
			settings.loadFromXML(conf);
			
			if(settings.containsKey("port")){
				setPort(Integer.parseInt(settings.getProperty("port")));
			}
			
			if(settings.containsKey("shutdownPort")){
				setShutdownPort(Integer.parseInt(settings.getProperty("shutdownPort")));
			}
		}catch(FileNotFoundException ex){
			throw new TomcatException("The file " + confFile.getAbsolutePath() + " does not exist or it is not readable", ex);
		}catch(InvalidPropertiesFormatException ex){
			throw new TomcatException("Invalid format of settings file. $file: " + confFile.getAbsolutePath(), ex);
		}catch(IOException ex){
			throw new TomcatException("I was not possible to read settings from " + confFile.getAbsolutePath(), ex );
		}
		
		return settings;
	}
	
	public void await(){
		tomcat.getServer().await();
	}
	
	public void stop() throws TomcatException{
		try{
			tomcat.getServer().stop();
		}catch(LifecycleException ex){
			throw new TomcatException("Component detected a fatal error", ex);
		}
	}
	
	public void shutdown() throws TomcatException{
		
		try{
			System.out.println(shutdownPort);
			Socket socket = new Socket("localhost", shutdownPort);
			
			if(socket.isConnected()){
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println(shutdown);
				pw.close();
				socket.close();
			}
		}catch(IOException ex){
			throw new TomcatException("Error ocurred while creating socket", ex);
		}
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
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

	public Integer getShutdownPort() {
		return shutdownPort;
	}

	public void setShutdownPort(Integer shutdownPort) {
		this.shutdownPort = shutdownPort;
	}

	public String getShutdown() {
		return shutdown;
	}

	public void setShutdown(String shutdown) {
		this.shutdown = shutdown;
	}
}
