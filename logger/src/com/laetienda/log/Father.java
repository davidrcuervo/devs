package com.laetienda.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Father {
	
	private static final String[] LEVELS = {"DEBUG", "INFO", "NOTICE", "WARNING", "ERROR", "CRITICAL", "ALERT", "EMERGENCY"};
	private File directory;
	private Properties settings;
	private Options options;
	
	public Father(File directory) throws IOException {
		this.directory = directory;
		
		options = setOptions();
		settings = setDefaultSettings();
		loadConfFile();
	}
	
	private Options setOptions(){
		Options options = new Options();
		
		options.addOption(new Option("h", "help", false, "Show help"))
		.addOption(new Option("L", "level", true, "Level log. (debug, info, error, critical"))
		.addOption(new Option("f", "file", true, "File where the logs will be stored"))
		.addOption(new Option("d", "directory", true, "Directory where the application is running"))
		.addOption(new Option("u", "user", true, "User that is adding the log"))
		.addOption(new Option("e", "exceptions", true, "Log exceptions, only for java programs"))
		.addOption(new Option("p", "program", true, "Name of the program/script that is resgistering the log")) //When logging java it uses class_
		.addOption(new Option("M", "method", true, "Name of the method that is adding the log (only for java programs)"))
		.addOption(new Option("m", "message", true, "Massage to log."))
		.addOption(new Option("l", "line", true, "Line number where the log wants to register (very useful to do troubleshoting"));
		
		return options;
	}
	
	public Options getOptions(){
		return options;
	}
	
	private Properties setDefaultSettings(){
		
		Properties settings = new Properties();
		
		settings.setProperty("file", directory.getAbsolutePath() + File.separator + "logger.log");
		settings.setProperty("level", "error");
		settings.setProperty("user", "null");
		settings.setProperty("program", "null");
		settings.setProperty("exceptions", "true");
		settings.setProperty("min-level", "debug");
		settings.setProperty("line", "0");
		settings.setProperty("saveTO", "console");	//options are: console, file, database
		
		return new Properties(settings);
	}
	
	public Properties loadConfFile() throws IOException{
		
		FileInputStream conf;
		
		if(directory.exists() && directory.isDirectory()){
			
			try{
				conf = new FileInputStream(new File(directory.getAbsolutePath() + 
						File.separator + "etc" +
						File.separator + "logger" + 
						File.separator + "conf.xml"));
				
				settings.loadFromXML(conf);
				
			}catch(Exception ex){
				throw new IOException("Exception: " + ex.getClass().getName() + "\n"
						+ "Exception message: " + ex.getMessage() + "\n"
						+ "Failed to load conf file. $file: " + directory + "/etc/database/conf.xml" );
			}finally{
				
			}
		}else{
			throw new IOException("No valid application directory");
		}
		
		return settings;
	}
	
	public Properties getSettings(){
		return settings;
	}
	
	public String getSetting(String setting){
		return getSettings().getProperty(setting);
	}
	
	public void setSetting(String key, String value){
		getSettings().setProperty(key, value);
	}
	
	public static String[] getLevels(){
		return LEVELS;
	}
}
