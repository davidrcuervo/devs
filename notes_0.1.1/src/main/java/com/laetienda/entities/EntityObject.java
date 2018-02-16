package com.laetienda.entities;

import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Deprecated
public abstract class EntityObject {

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private HashMap<String, List<String>> errors;
	
	public EntityObject() {
		errors = new HashMap<String, List<String>>();
	}
	
	public abstract String getIdentifierName();
	public abstract EntityObject setIdentifierValue(Integer id);
	
	public String getDate(Calendar date){
		
		return FORMATTER.format(date.getTime());
	}	
	
	public void addError(String list, String error){
		
		List<String> errorList;
		
		if(errors.get(list) == null){
			errorList = new ArrayList<String>();
		} else{
			errorList = errors.get(list);			
		}
		
		errorList.add(error);
		errors.put(list, errorList);
	}
	
	public HashMap<String, List<String>> getErrors(){
		return errors;
	}
}
