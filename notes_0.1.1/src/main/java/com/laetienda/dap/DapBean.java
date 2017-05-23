package com.laetienda.dap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;

public class DapBean {
	
	private LdapConnection connection;
	
	public DapBean(LdapConnection connection){
		this.connection = connection;
	}
	
	public DapBean addEntry(DapEntry dapEntry) throws DapException{
		
		if(dapEntry.getErrors().size() > 0){
			//entry has errors. Better not to add it to the directory with errors to avoid exceptions
		}else{ 
		
			Entry entry = dapEntry.getEntry();
			try{
				if(connection.exists(entry.getDn())){
					throw new DapException("Entry already exists: $entry: " + entry.getDn());
				}else{
					connection.add(entry);
				}
			}catch(LdapException ex){
				throw new DapException("Unable to add entry to directory", ex);
			}
		}
		return this;
	}
	
	public List<DapEntry> searchEntry(String base, String attribute, Class<?> classname){
		List<DapEntry> result = new ArrayList<DapEntry>();
		
		SearchRequest req = new SearchRequestImpl();
		req.setScope(SearchScope.SUBTREE);
		req.addAttributes("*");
		
		try{
			req.setBase(new Dn(base, Ldif.getDomain()));
			req.setFilter(attribute);
			
			SearchCursor searchCursor = connection.search(req);
			
			while(searchCursor.next()){
				
				Response response = searchCursor.get();
				
				if(response instanceof SearchResultEntry){
					
					Entry resultEntry = ((SearchResultEntry)response).getEntry();
					Object o = Class.forName(classname.getName()).getConstructor(classname).newInstance();
					DapEntry obj = (DapEntry)o;
					obj.setDapEntry(resultEntry);
					result.add(obj);
				}
			}
			
		}catch(LdapException ex){
			ex.printStackTrace();
		}catch(CursorException ex){
			ex.printStackTrace();
		}catch(NoSuchMethodException ex){
			ex.printStackTrace();
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}catch(InvocationTargetException ex){
			ex.printStackTrace();
		}catch(IllegalAccessException ex){
			ex.printStackTrace();
		}catch(InstantiationException ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public DapBean deleteEntry(DapEntry dapEntry){
		
		try{
			Entry entry = dapEntry.getEntry();
			if(connection.exists(entry.getDn())){
				connection.delete(entry.getDn());
			}
			
		}catch(DapException ex){
			//No necessary to do nothing
		}catch(LdapException ex){
			//No necessary to do anything
		}
		
		return this;
	}
	
	public void close() throws DapException{
		if(connection.isConnected()){
			try{
				connection.close();
			}catch(IOException ex){
				throw new DapException("It failed to close connection", ex);
			}
		}
	}
}
