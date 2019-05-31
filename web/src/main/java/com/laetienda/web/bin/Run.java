package com.laetienda.web.bin;

import java.io.File;
import com.laetienda.tomcat.Controller;
import com.laetienda.tomcat.TomcatException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Run {
	
	final static File DIRECTORY = new File(Run.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	static {
		System.setProperty("logFilePath", new File(DIRECTORY, "var" + File.separator + "log").getAbsolutePath());
	}
		
	public static void main(String[] args) {

		Logger log4j2 = LogManager.getLogger(Run.class);
		log4j2.info("Starting website");

		log4j2.debug("Application path. $DIRECTORY: " + DIRECTORY.getAbsolutePath());
		
		try{
			Controller tomcat  = new Controller(new File(DIRECTORY.getAbsolutePath()));
			tomcat.parseArguments(args);
			
		}catch(TomcatException ex){
			log4j2.fatal("Failed to run Tomcat embedded container", ex.getRootParent());
			
		}
	}
}
