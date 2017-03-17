package com.laetienda.log;

public class LoggerException extends Exception {
private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public LoggerException() { 
		super(); 
	}

	public LoggerException(String message) { 
		super(message); 
	}
	
	public LoggerException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public LoggerException(Throwable cause) { 
		super(cause); 
	}
	
	public LoggerException(String message, Exception parent){
		super(message);
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	public Exception getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Exception parent) {
		this.parent = parent;
	}
}
