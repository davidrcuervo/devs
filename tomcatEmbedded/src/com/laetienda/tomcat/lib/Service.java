package com.laetienda.tomcat.lib;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.net.Socket;

public class Service {
	
	private final String WIN_DIRECTORY = "C:\\Users\\i849921\\git\\devs\\NotesApp\\bin";
	
	
	private Tomcat tomcat = new Tomcat();
	private String directory;
	private Integer port;
	private Integer shutdownPort;
	private String shutdown;
	
	public Service() throws Exception{
		directory = System.getProperty("user.dir") + File.separator + "..";
		directory = WIN_DIRECTORY + File.separator + ".."; //This line is for testing propouses
		
		port = 8080;
		shutdownPort = 8081;
		shutdown = "shutdown";
		loadConfFile(directory);
		tomcat = new Tomcat();
	}
	
	public void start() throws Exception{
		tomcat.setPort(port);
		tomcat.setBaseDir(directory);
		tomcat.getHost().setAppBase(directory);
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		tomcat.getHost().setAppBase(directory + File.separator + "WebContent");
		Context context = tomcat.addWebapp("", directory + File.separator + "WebContent");
		context.addParameter("directory", directory);
		tomcat.getServer().setPort(shutdownPort);
		tomcat.getServer().setShutdown(shutdown);
		tomcat.start();
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
		
		if(settings.containsKey("shutdownPort")){
			setShutdownPort(Integer.parseInt(settings.getProperty("shutdownPort")));
		}
		
		return settings;
	}
	
	public void await() throws Exception{
		tomcat.getServer().await();
	}
	
	public void shutdown() throws Exception{
		Socket socket = new Socket("localhost", shutdownPort);
		
		if(socket.isConnected()){
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(shutdown);
			pw.close();
			socket.close();
		}
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
