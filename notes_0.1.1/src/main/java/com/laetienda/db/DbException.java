package com.laetienda.db;

import com.laetienda.AppException;

public class DbException extends AppException {
	
private static final long serialVersionUID = 1L;
	
	public DbException() { 
		super(); 
	}

	public DbException(String message) { 
		super(message); 
	}
	
	public DbException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public DbException(Throwable cause) { 
		super(cause); 
	}
	
	public DbException(String message, Exception parent){
		super(message, parent);
	}
}
