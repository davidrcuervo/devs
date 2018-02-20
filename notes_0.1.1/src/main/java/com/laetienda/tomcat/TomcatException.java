package com.laetienda.tomcat;

import com.laetienda.app.AppException;

public class TomcatException extends AppException {
	private static final long serialVersionUID = 1L;
	
	public TomcatException() { 
		super(); 
	}

	public TomcatException(String message) { 
		super(message); 
	}
	
	public TomcatException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public TomcatException(Throwable cause) { 
		super(cause); 
	}
	
	public TomcatException(String message, Exception parent){
		super(message, parent);
	}
}
