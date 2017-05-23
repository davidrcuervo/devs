package com.laetienda.dap;

import java.util.HashMap;
import java.util.List;

import org.apache.directory.api.ldap.model.entry.Entry;  

public interface DapEntry {
	
	public Entry getEntry() throws DapException;
	public HashMap<String, List<String>> getErrors();
	public void addError(String list, String error);
	public void setDapEntry(Entry entry);
}
