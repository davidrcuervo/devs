package com.laetienda.dap;

import com.laetienda.app.AppException;

public class DapException extends AppException {
	private static final long serialVersionUID = 1L;
	
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
		super(message, parent);
	}
}
