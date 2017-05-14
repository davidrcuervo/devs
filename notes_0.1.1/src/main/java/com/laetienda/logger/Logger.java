package com.laetienda.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.entities.Log;

public class Logger {
	
public static int counter = 0;
	
	private File file;	
	private Db db;
	private Log entity;
	private String message;
	private LoggerManager logManager;
	
	protected Logger(LoggerManager logManager){
		this.logManager = logManager;
		entity = new Log();
		file = logManager.getFile();
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	protected Log getEntity(){
		return entity;
	}

	/**
	 * Message that will be printed with the logs.
	 * @param message the message to set
	 * @throws IOException if message is null or empty
	 */
	
	public void setMessage(String message) throws LoggerException {
		
		if(message != null && !message.isEmpty())
			this.message = message;
		else
			throw new LoggerException("Message is empty");
	}
	
	//This method is like the table of contents to save the log.
	//It calls each of the method (steps) to save the log.
	public void print() throws LoggerException{
		
		int level = getLevel(entity.getLevel());
		int minLevel = getLevel(logManager.getSetting("min-level"));
		
		if(minLevel <= level){
		
			if(logManager.getSetting("saveTO").equals("console")) {
				System.out.println(getLogLine());
				
			} else if(logManager.getSetting("saveTO").equals("file")){
				saveToFile();
				
			} else if(logManager.getSetting("saveTO").equals("database")) {
				saveToDb();
				
			} else {
				System.out.println(getLogLine());
				throw new LoggerException("saveTO option is not valid. saveTO: " + logManager.getSetting("saveTO"));
			}
		}
			
		entity = new Log();
		Logger.counter++;
	}
		
	private int getLevel(String level) throws LoggerException{
		int result = -1;
		level = level.toUpperCase();
		
		int c;
		for(c = 0; c < LoggerManager.getLevels().length; c++){
			//System.out.println(level + " compared with " + Logger.getLevels()[c]);
			if(level.equals(LoggerManager.getLevels()[c])){
				result = c;
				break;
			} 
		}
		
		if(result == -1){
			throw new LoggerException("The log level does not exist");
		}
		
		return result;
	}
	
	private String getLogLine(){
		return Integer.toString(Logger.counter) + "\t"
				+ entity.getCreatedDateInString() + "\t"
				+ entity.getUser() + "\t"
				+ entity.getLevel() + "\t"
				+ entity.getProgram() + "\t"
				+ entity.getMethod() + "\t"
				+ Integer.toString(entity.getLine()) + "\t"
				+ getMessage() + "\t";
	}
	
	private synchronized void saveToFile(){
		
		try{
			FileWriter out = new FileWriter(file, true);
			out.write(getLogLine() + "\n");
			out.close();
		}catch (IOException ex){
			message += "LOG ERROR: Not possible to log on file. -> EXCEPTION MESSAGE: " + ex.getMessage();
			System.err.println(getLogLine());
			ex.printStackTrace();
		}
	}
	
	private void saveToDb(){
		
		try{
			db.insert(db.getEm(), entity);
		}catch (DbException ex){
			message += "LOG ERROR: Not possible to log on database. -> EXCEPTION MESSAGE: " + ex.getMessage();
			saveToFile();
		}finally{
			db.getEm().clear();
		}		
	}
}
