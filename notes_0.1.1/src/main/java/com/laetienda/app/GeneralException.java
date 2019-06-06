package com.laetienda.app;

import com.laetienda.app.AppException;

public class GeneralException extends AppException {
	
private static final long serialVersionUID = 1L;
	
	public GeneralException() { 
		super(); 
	}

	public GeneralException(String message) { 
		super(message); 
	}
	
	public GeneralException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public GeneralException(Throwable cause) { 
		super(cause); 
	}
	
	public GeneralException(String message, Exception parent){
		super(message, parent);
	}
}
