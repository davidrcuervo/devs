package com.laetienda.log.bin;

import com.laetienda.log.Logger;
import java.io.IOException;

public class Java extends Logger{
	
	private void run(String message, String level){
		
		try{
			setClassDetails();
			getEntity().setLevel(level);
			setMessage(message);
			getEntity().setUser(getSetting("user"));
			
		}catch (Exception ex){
			
		}finally{
			
		}
	}
	
	private void setClassDetails() throws IOException {
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		for(int c=1; c < stackTraceElements.length; c++){
			if(stackTraceElements[c].getClassName().equals(Logger.class.getName()) && stackTraceElements[c].getClassName().indexOf("java.lang.Thread") != 0){	
			
			} else{
				getEntity().setProgram(stackTraceElements[c].getClassName());
				getEntity().setMethod(stackTraceElements[c].getMethodName());
				getEntity().setLine(stackTraceElements[c].getLineNumber());
				break;
			}
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
