package com.laetienda.logger;

//import org.apache.log4j.Logger;

public class Log4j {

	final static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(Log4j.class);
	
	public static void main (String[] args) {
		Log4j log4j = new Log4j();
		log4j.runMe("Hello Log4j");
	}
	
	public void runMe(String parameter) {
		log4j.info("This is a test");
	}
}
