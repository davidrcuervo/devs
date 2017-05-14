package com.laetienda.tomcat;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.net.Socket;

public class Service {
	
	private Tomcat tomcat = new Tomcat();
	private File directory;
	private Integer port;
	private Integer shutdownPort;
	private String shutdown;
	
	public Service(File directory) throws Exception{
		
		this.directory = directory; 
		port = 8080;
		shutdownPort = 8081;
		shutdown = "shutdown";
		loadConfFile();
		tomcat = new Tomcat();
	}
	
	public void start() throws Exception{
		tomcat.setPort(port);
		tomcat.setBaseDir(directory.getAbsolutePath());
		tomcat.getHost().setAppBase(directory.getAbsolutePath());
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		tomcat.getHost().setAppBase(directory.getAbsolutePath() + File.separator + "WebContent");
		Context context = tomcat.addWebapp("", directory.getAbsolutePath() + File.separator + "WebContent");
		context.addParameter("directory", directory.getAbsolutePath());
		tomcat.getServer().setPort(shutdownPort);
		tomcat.getServer().setShutdown(shutdown);
		tomcat.start();
	}
	
	public Properties loadConfFile() throws Exception{
		
		FileInputStream conf;
		Properties settings = new Properties();
		
		File confFile = new File(directory.getAbsolutePath() + File.separator + "etc" + File.separator + "tomcat.conf.xml");
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
	
	public void stop() throws Exception{
		tomcat.getServer().stop();
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
