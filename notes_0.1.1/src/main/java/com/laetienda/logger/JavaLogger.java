package logger;

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
	
	public void exception(Exception ex){
		
		if(ex instanceof com.laetienda.db.exceptions.SqlException){
			System.out.println(ex.getMessage());
			System.out.println(((com.laetienda.db.exceptions.SqlException) ex).getQuery());
			((com.laetienda.db.exceptions.SqlException) ex).getParent().printStackTrace();
		
		}else if(ex instanceof com.laetienda.db.exceptions.DbException){
			System.out.println(ex.getMessage());
			((com.laetienda.db.exceptions.DbException) ex).getParent().printStackTrace();
			
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
