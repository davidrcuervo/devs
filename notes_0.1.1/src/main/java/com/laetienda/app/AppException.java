package com.laetienda.app;
/**
 * 
 * @author myself
 *
 */
public abstract class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public AppException() { 
		super(); 
	}

	public AppException(String message) { 
		super(message); 
	}
	
	public AppException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public AppException(Throwable cause) { 
		super(cause); 
	}
	
	public AppException(String message, Exception parent){
		super(message);
		this.parent = parent;
	}
	
	/**
	 * @return the parent
	 */
	public Exception getParent() {
		return parent;
	}
	
	public Exception getRootParent() {
		Exception result = parent;
		if(parent != null) {
			if(parent instanceof AppException) {
				result = findFirstParent((AppException)parent);
			}
		}else {
			result = this;
		}
			
		return result;
	}
	
	private Exception findFirstParent(AppException temp) {
		Exception result = null;
		
		if(temp.getParent() != null && temp.getParent() instanceof AppException) {
			findFirstParent((AppException)temp.getParent());
		}else {
			result = temp;
		}
		
		return result;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Exception parent) {
		this.parent = parent;
	}
}
