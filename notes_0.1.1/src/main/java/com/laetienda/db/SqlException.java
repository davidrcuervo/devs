package com.laetienda.db;

public class SqlException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private String query;
	private Exception parent;
	
	public SqlException() { 
		super(); 
	}

	public SqlException(String message) { 
		super(message); 
	}
	
	public SqlException(String message, Throwable cause) { 
		super(message, cause); 
	}
	
	public SqlException(Throwable cause) { 
		super(cause); 
	}
	
	public SqlException(String message, String query, Exception parent){
		super(message);
		this.query = query;
		this.parent = parent;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
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
