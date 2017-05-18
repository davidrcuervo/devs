package com.laetienda;
/**
 * 
 * @author myself
 *
 */
public abstract class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public AppException() { 
		super(); 
	}

	public AppException(String message) { 
		super(message); 
	}
	
	public AppException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public AppException(Throwable cause) { 
		super(cause); 
	}
	
	public AppException(String message, Exception parent){
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
