package com.laetienda.logger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log4j {

	final static Logger log4j = LogManager.getLogger(Log4j.class);
	
	public static void main (String[] args) {
		Log4j log4j = new Log4j();
		log4j.runMe("Hello Log4j");
	}
	
	public void runMe(String parameter) {
		log4j.info("This is a test");
	}
}
