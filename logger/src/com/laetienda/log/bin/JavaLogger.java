package com.laetienda.log.bin;

import com.laetienda.log.Logger;
import java.io.IOException;
import java.io.File;

public class JavaLogger extends Logger{
	
	public JavaLogger(File directory) throws Exception{
		super(directory);
	}
	
	private void run(String message, String level){
		
		try{
			setClassDetails();
			getEntity().setLevel(level);
			setMessage(message);
			getEntity().setUser(getSetting("user"));
			print();
			
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			
		}
	}
	
	private void setClassDetails() throws IOException {
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		for(int c=1; c < stackTraceElements.length; c++){
			if(stackTraceElements[c].getClassName().equals(JavaLogger.class.getName()) && stackTraceElements[c].getClassName().indexOf("java.lang.Thread") != 0){	
			
			} else{
				getEntity().setProgram(stackTraceElements[c].getClassName());
				getEntity().setMethod(stackTraceElements[c].getMethodName());
				getEntity().setLine(stackTraceElements[c].getLineNumber());
				break;
			}
		}
	}
	
	public void exception(Exception ex){
		
		if(ex instanceof com.laetienda.db.exceptions.SqlException){
			System.out.println(ex.getMessage());
			System.out.println(((com.laetienda.db.exceptions.SqlException) ex).getQuery());
			((com.laetienda.db.exceptions.SqlException) ex).getParent().printStackTrace();
		}else{
			ex.printStackTrace();
		}
	}
	
	public void debug(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void info(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void notice(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void warning(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void error(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void critical(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void alert(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
	
	public void emergency(String message){
		run(message, Thread.currentThread().getStackTrace()[1].getMethodName().toUpperCase());
	}
}
