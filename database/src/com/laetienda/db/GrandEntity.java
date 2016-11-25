package com.laetienda.db;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public abstract class GrandEntity {
	
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public abstract Integer getId();
	public abstract String getCreatedDateInString();
	public abstract String getModifiedDateInString();
	
	public String getDate(Calendar date){
		
		return FORMATTER.format(date.getTime());
	}
}
