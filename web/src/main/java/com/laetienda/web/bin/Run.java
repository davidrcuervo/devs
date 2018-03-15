package com.laetienda.web.bin;

import java.io.File;
import com.laetienda.tomcat.Controller;
import com.laetienda.tomcat.TomcatException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Run {
	private final static Logger log4j2 = LogManager.getLogger(Run.class);
	
	public static void main(String[] args) {
		System.out.println("Starting website");
		
		File directory = new File("");
		
		try{
			Controller tomcat  = new Controller(directory);
			tomcat.parseArguments(args);
			
		}catch(TomcatException ex){
			log4j2.fatal("Failed to run Tomcat embedded container", ex.getRootParent());
			
		}
	}
}
