package com.laetienda.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.persistence.EntityManager;

import com.laetienda.log.entities.Log;
import com.laetienda.db.Transaction;

public class Logger extends Father {

	public static int counter = 0;
	
	private File file;	
	private Transaction trans;
	private Log entity;
	private String message;
	
	public Logger(){
		super();
		entity = new Log();
	}
	
	public Log getEntity(){
		return entity;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Message that will be printed with the logs.
	 * @param message the message to set
	 * @throws IOException if message is null or empty
	 */
	public void setMessage(String message) throws IOException {
		
		if(message != null && !message.isEmpty())
			this.message = message;
		else
			throw new IOException("Message t is empty");
	}
		
	public void setFile(String fileName) throws Exception{
		
		try{
			file = new File(fileName);
			
			if(this.file.exists()){
				if(!file.canRead()){
					file = null;
					throw new IOException("The log file is not readable -> FILE: " + file.getAbsolutePath());
				}
				
				if(!file.canWrite()){
					file = null;
					throw new IOException("The log file is not writable -> FILE: " + file.getAbsolutePath());
				}
				
				if(!file.isFile()){
					file = null;
					throw new IOException("The log file is not a file  -> FILE: " + file.getAbsolutePath());
				}
				
			}else{
				String header = "COUNTER\t"
						+ "DATE\t"
						+ "USER\t"
						+ "LEVEL\t"
						+ "PROGRAM\t"
						+ "METHOD\t"
						+ "LINE\t\t"
						+ "MESSAGE\n";
				
				FileWriter out = new FileWriter(file, true);
				out.write(header);
				out.close();
			}
			
		}catch(NullPointerException ex){
			file = null;
			throw ex;
			
		}catch(SecurityException ex){
			throw ex;
		}catch(IOException ex){
			file = null;
			throw new IOException("\n" + ex.getMessage() + " FILE: " + fileName);
		}
	}
	
	public void setDb(EntityManager em){
		
		trans = new Transaction(em);
	}
	
	//This method is like the table of contents to save the log.
	//It calls each of the method (steps) to save the log.
	public void print() throws IOException{
		
		int level = getLevel(getEntity().getLevel());
		int minLevel = getLevel(getSetting("min-level"));
		
		if(minLevel <= level){
		
			if(getSetting("saveTO").equals("console")) {
				System.out.println(getLogLine());
				
			} else if(getSetting("saveTO").equals("file")){
				saveToFile();
				
			} else if(getSetting("saveTO").equals("database")) {
				saveToDb();
				
			} else {
				throw new IOException("saveTO option is not valid. saveTO: " + getSetting("saveTO"));
			}
		}
			
		entity = new Log();
		Logger.counter++;
	}
	
	private int getLevel(String level) throws IOException{
		int result = -1;
		level = level.toUpperCase();
		
		int c;
		for(c = 0; c < Logger.getLevels().length; c++){
			//System.out.println(level + " compared with " + Logger.getLevels()[c]);
			if(level.equals(Logger.getLevels()[c])){
				result = c;
				break;
			} 
		}
		
		if(result == -1){
			throw new IOException("The log level does not exist");
		}
		
		return result;
	}

	private void saveToDb(){
			
		try{
			trans.begin(trans.getEm());
			trans.save(trans.getEm(), getEntity());
		}catch (Exception ex){
			message += "LOG ERROR: Not possible to log on database. -> EXCEPTION MESSAGE: " + ex.getMessage();
			saveToFile();
		}finally{
			trans.getEm().clear();
		}		
	}
	
	private void saveToFile(){
	
		try{
			FileWriter out = new FileWriter(file, true);
			out.write(getLogLine() + "\n");
			out.close();
		}catch (IOException ex){
			message += "LOG ERROR: Not possible to log on file. -> EXCEPTION MESSAGE: " + ex.getMessage();
			System.err.println(getLogLine());
		}
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
	
	public String getFilePath(){
		
		if(this.file == null){
			return "";
		}else{
			return this.file.getAbsolutePath();
		}
	}
}

