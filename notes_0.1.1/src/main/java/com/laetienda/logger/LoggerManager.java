package com.laetienda.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class LoggerManager {
	private static final String[] LEVELS = {"DEBUG", "INFO", "NOTICE", "WARNING", "ERROR", "CRITICAL", "ALERT", "EMERGENCY"};
	private File directory;
	private File file;
	private Properties settings;
	private Options options;
	
	public LoggerManager(File directory) throws LoggerException{
		this.directory = directory;
				
		options = setOptions();
		settings = setDefaultSettings();
		loadConfFile();
		validateProperties();
	}
	
	public synchronized Logger createLogger(){
		return new Logger(this);
	}
	
	public synchronized JavaLogger createJavaLogger(){
		return new JavaLogger(this);
	}
	
	public void closeJavaLogger(JavaLogger log){
		
	};
	
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
	
	protected Options getOptions(){
		return options;
	}
	
	private Properties setDefaultSettings(){
		
		Properties settings = new Properties();
		
		settings.setProperty("file", directory.getAbsolutePath() + File.separator + "var" + File.separator + "log" + File.separator + "logger.log");
		settings.setProperty("level", "error");
		settings.setProperty("user", "null");
		settings.setProperty("program", "null");
		settings.setProperty("exceptions", "true");
		settings.setProperty("min-level", "debug");
		settings.setProperty("line", "0");
		settings.setProperty("saveTO", "console");	//options are: console, file, database
		
		return new Properties(settings);
	}
	
	private Properties loadConfFile() throws LoggerException{
		
		FileInputStream conf;
		
		if(directory.exists() && directory.isDirectory()){
			
			try{
				conf = new FileInputStream(new File(directory.getAbsolutePath() + 
						File.separator + "etc" +
						File.separator + "logger.conf.xml"));
				
				settings.loadFromXML(conf);
			
			}catch(FileNotFoundException ex){
				throw new LoggerException("the file (" + directory + "/etc/logger.conf.xml) does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.", ex);
			}catch(SecurityException ex){
				throw new LoggerException("checkRead method denies read access to the file (" +  directory + "/etc/database/conf.xml)", ex);
			}catch(InvalidPropertiesFormatException ex){
				throw new LoggerException("Error while loading conf.xml file for Logger. /n " + ex.getMessage(), ex);
			}catch(IOException ex){
				throw new LoggerException("Error while loading conf.xml file /n " + ex.getMessage(), ex);
			}finally{
				
			}
		}else{
			throw new LoggerException("No valid application directory");
		}
		
		return settings;
	}
	
	private void validateProperties() throws LoggerException{
		
		if(getSetting("saveTO").equals("file")){
			try{
				if(new File(getSetting("file")).exists() || new File(getSetting("file")).createNewFile()){
					file = new File(getSetting("file"));
				}else{
					throw new LoggerException("Logs are configured to write file but file is not writable. file: " + getSetting("file") );
				}
			}catch(IOException ex){
				throw new LoggerException("Logs are configured to write file but file is not writable. file: " + getSetting("file") );
			}
		}
	}
	
	protected File getFile(){
		return file;
	}
	
	protected Properties getSettings(){
		return settings;
	}
	
	protected String getSetting(String setting){
		return getSettings().getProperty(setting);
	}
	
	protected void setSetting(String key, String value){
		getSettings().setProperty(key, value);
	}
	
	public static String[] getLevels(){
		return LEVELS;
	}
	
	public void close(){
		
	}
}
