package com.laetienda.logger;

import com.laetienda.AppException;
import com.laetienda.db.SqlException;
import com.laetienda.lang.LangException;
import com.laetienda.multimedia.MultimediaException;
import com.laetienda.notes.NotesException;

@Deprecated
public class JavaLogger {
	
	private Logger logger;
	private LoggerManager logManager;
	
	protected JavaLogger(LoggerManager logManager){
		this.logManager = logManager;
		this.logger = logManager.createLogger();
	}
	
	private void run(String message, String level){
		
		try{
			setClassDetails();
			logger.getEntity().setLevel(level);
			logger.setMessage(message);
			logger.getEntity().setUser(logManager.getSetting("user"));
			logger.print();
			
		}catch (LoggerException ex){
			ex.printStackTrace();
		}finally{
			
		}
	}
	
	private void setClassDetails() throws LoggerException{
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		
		for(int c=1; c < stackTraceElements.length; c++){
			if(stackTraceElements[c].getClassName().equals(JavaLogger.class.getName()) && stackTraceElements[c].getClassName().indexOf("java.lang.Thread") != 0){	
			
			} else{
				logger.getEntity().setProgram(stackTraceElements[c].getClassName());
				logger.getEntity().setMethod(stackTraceElements[c].getMethodName());
				logger.getEntity().setLine(stackTraceElements[c].getLineNumber());
				break;
			}
		}
	}
	
	public void exception(AppException ex){
		if(ex.getParent() != null){
			System.out.println(ex.getMessage());
			ex.getParent().printStackTrace();
		}else{
			ex.printStackTrace();
		}
	}
	
	public void exception(Exception ex){
		
		if(ex instanceof SqlException){
			SqlException sqlException = (SqlException)ex;
			
			if(sqlException.getParent() != null){
				System.out.println(ex.getMessage());
				sqlException.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
		
		}else if(ex instanceof MultimediaException){
			MultimediaException multimediaException = (MultimediaException)ex;
			
			if(multimediaException.getParent() != null){
				System.out.println(ex.getMessage());
				multimediaException.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
			
		}else if(ex instanceof NotesException){
			NotesException notesException = (NotesException)ex;
			
			if(notesException.getParent() != null){
				System.out.println(ex.getMessage());
				notesException.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
			
		}else if(ex instanceof LangException){
			LangException langException = (LangException)ex;
			
			if(langException.getParent() != null){
				System.out.println(ex.getMessage());
				langException.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
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
