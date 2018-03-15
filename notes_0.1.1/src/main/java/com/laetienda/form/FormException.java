package com.laetienda.form;

import com.laetienda.app.AppException;

public class FormException extends AppException {
	private static final long serialVersionUID = 1L;
	
	public FormException() { 
		super(); 
	}

	public FormException(String message) { 
		super(message); 
	}
	
	public FormException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public FormException(Throwable cause) { 
		super(cause); 
	}
	
	public FormException(String message, Exception parent){
		super(message, parent);
	}
}
