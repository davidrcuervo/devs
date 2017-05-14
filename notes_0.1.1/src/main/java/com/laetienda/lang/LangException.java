package com.laetienda.lang;

public class LangException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public LangException() { 
		super(); 
	}

	public LangException(String message) { 
		super(message); 
	}
	
	public LangException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public LangException(Throwable cause) { 
		super(cause); 
	}
	
	public LangException(String message, Exception parent){
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
