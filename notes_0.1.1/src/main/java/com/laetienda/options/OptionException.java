package com.laetienda.options;

import com.laetienda.AppException;

public class OptionException extends AppException {
	private static final long serialVersionUID = 1L;
	
	public OptionException() { 
		super(); 
	}

	public OptionException(String message) { 
		super(message); 
	}
	
	public OptionException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public OptionException(Throwable cause) { 
		super(cause); 
	}
	
	public OptionException(String message, Exception parent){
		super(message, parent);
	}
}
