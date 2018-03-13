package com.laetienda.entities;

import java.util.HashMap;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public abstract class EntityObject {
	private static final Logger log4j = LogManager.getLogger(EntityObject.class);
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private HashMap<String, List<String>> errors = new HashMap<String, List<String>>();
	
	public String getDate(Calendar date){
		
		return FORMATTER.format(date.getTime());
	}	
	
	/**
	 * 
	 * @param list Name of the list of errors
	 * @param error Error description, cause and consequences make sure it exists in language database
	 */
	public void addError(String list, String error){
		
		List<String> errorList;
		
		if(errors.get(list) == null){
			errorList = new ArrayList<String>();
		} else{
			errorList = errors.get(list);			
		}
		
		errorList.add(error);
		errors.put(list, errorList);
		log4j.info("Adding error message to hashmap. $list: " + list + " - $error: " + error);
	}
	
	public HashMap<String, List<String>> getErrors(){
		return errors;
	}
}
