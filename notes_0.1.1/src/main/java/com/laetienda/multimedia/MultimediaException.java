package multimedia;

public class MultimediaException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Exception parent;
	
	public MultimediaException() { 
		super(); 
	}

	public MultimediaException(String message) { 
		super(message); 
	}
	
	public MultimediaException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public MultimediaException(Throwable cause) { 
		super(cause); 
	}
	
	public MultimediaException(String message, Exception parent){
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
