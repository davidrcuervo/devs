package com.laetienda.db;

public class DbException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public DbException() { 
		super(); 
	}

	public DbException(String message) { 
		super(message); 
	}
	
	public DbException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public DbException(Throwable cause) { 
		super(cause); 
	}
	
	public DbException(String message, Exception parent){
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
