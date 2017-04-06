package com.laetienda.notes;

public class NotesException extends Exception {
	private static final long serialVersionUID = 1L;

	
private Exception parent;
	
	public NotesException() { 
		super(); 
	}

	public NotesException(String message) { 
		super(message); 
	}
	
	public NotesException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public NotesException(Throwable cause) { 
		super(cause); 
	}
	
	public NotesException(String message, Exception parent){
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
