package com.laetienda.dap;

public class DapException extends Exception {
private static final long serialVersionUID = 1L;

	
	private Exception parent;
	
	public DapException() { 
		super(); 
	}

	public DapException(String message) { 
		super(message); 
	}
	
	public DapException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public DapException(Throwable cause) { 
		super(cause); 
	}
	
	public DapException(String message, Exception parent){
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
