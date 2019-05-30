package com.laetienda.web.bin;

import java.io.File;
import com.laetienda.tomcat.Controller;
import com.laetienda.tomcat.TomcatException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Run {
		
	public static void main(String[] args) {
		Logger log4j2 = LogManager.getLogger(Run.class);
		log4j2.info("Starting website");

		File directory2 = new File(Run.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		log4j2.debug("$direcotry2: " + directory2.getAbsolutePath());
		
		try{
			Controller tomcat  = new Controller(directory2);
			tomcat.parseArguments(args);
			
		}catch(TomcatException ex){
			log4j2.fatal("Failed to run Tomcat embedded container", ex.getRootParent());
			
		}
	}
}
