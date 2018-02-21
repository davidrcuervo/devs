package com.laetienda.entities;

import java.util.HashMap;
import java.util.List;

import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.Logger;

public abstract class EntityObject {
	private static final Logger log4j = Logger.getLogger(EntityObject.class);
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private HashMap<String, List<String>> errors;
	
	public EntityObject() {
		errors = new HashMap<String, List<String>>();
	}
	
	public abstract Objeto getObjeto();
	//public abstract String getIdentifierName();
	//public abstract EntityObject setIdentifierValue(Integer id);
	
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
	
	@PrePersist
	public void onPrePersist() {
		log4j.info("Updating created timestamp");
		this.getObjeto().setCreated(); 
	}
	
	@PreUpdate
	public void noPreUpdate() {
		log4j.info("Updating modified timestamp");
		this.getObjeto().setModified();
	}
}
