package com.laetienda.tomcat.lib;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;

public class Service {
	
	Tomcat tomcat = new Tomcat();
	String directory;
	Integer port;
	Context context;
	
	Service(){
		tomcat = new Tomcat();
		directory = System.getProperty("user.dir") + File.separator + ".." + File.separator + "tomcat";
		port = 8080;
	}
	
	public void start(){
		tomcat.setPort(port);
		tomcat.setBaseDir(directory);
		tomcat.getHost().setAppBase(directory);
		tomcat.getHost().setAutoDeploy(true);
		tomcat.getHost().setDeployOnStartup(true);
		
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
