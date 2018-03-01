package com.laetienda.mail;

import com.laetienda.app.AppException;

public class MailException extends AppException {
	private static final long serialVersionUID = 1L;
	
	public MailException() { 
		super(); 
	}

	public MailException(String message) { 
		super(message); 
	}
	
	public MailException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public MailException(Throwable cause) { 
		super(cause); 
	}
	
	public MailException(String message, Exception parent){
		super(message, parent);
	}
}
