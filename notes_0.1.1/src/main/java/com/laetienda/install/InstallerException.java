package com.laetienda.install;

import com.laetienda.AppException;

public class InstallerException extends AppException {
private static final long serialVersionUID = 1L;
	
	public InstallerException() { 
		super(); 
	}

	public InstallerException(String message) { 
		super(message); 
	}
	
	public InstallerException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public InstallerException(Throwable cause) { 
		super(cause); 
	}
	
	public InstallerException(String message, Exception parent){
		super(message, parent);
	}
}
