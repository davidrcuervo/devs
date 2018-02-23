package com.laetienda.dap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;

import com.laetienda.entities.User;

public class Dap {
	
	private LdapConnection connection;
	private Dn baseDn;
	private User tomcat;
	
	public Dap(LdapConnection connection, User tomcat, Dn baseDn){
		this.connection = connection;
		setBaseDn(baseDn);
	}
	
	public void insertUser(User newUser) throws DapException {
		
		EntryCursor search = null;
		
		try {
			Dn peopleDn = new Dn("ou=People", baseDn.getName());
			Dn tomcatDn = new Dn("uid=" + Integer.toString(tomcat.getUid()), "ou=People", baseDn.getName());
			
			connection.bind(tomcatDn, tomcat.getPassword());
			
			search = connection.search(peopleDn, 
					"(|(uid=" + Integer.toString(newUser.getUid()) + ")(cn=" + newUser.getEmail() + "))", 
					SearchScope.ONELEVEL);
			
			for(Entry entry : search) {
				if(entry != null) {
					newUser.addError("mail", "Email already exists");
				}
			}
			
			if(newUser.getErrors().size() > 0) {
				throw new DapException("User has not been saved because it has errors.");
			}else {
				connection.add(getDapUserEntry(newUser));
			}
			
		} catch (LdapException ex) {
			throw new DapException("Failed to insert user in ldap directory", ex);
		}finally {
			try {
				if(search != null && !search.isClosed()) {
					search.close();
				}
				if(connection.isConnected() || connection.isAuthenticated()) {
					connection.unBind();
				}
				
			} catch (IOException | LdapException e) {
				throw new DapException("Failed to close connection with ldap server", e);
			}
		}
	}
	
	public Entry getDapUserEntry(User user) throws DapException  {
		Entry result = null;
		try {
			Dn userDn = new Dn("uid=" + Integer.toString(user.getUid()), "ou=People", baseDn.getName());
			result = new DefaultEntry(userDn);
			result.add("objectclass", "person")
				.add("objectclass", "inetOrgPerson")
				.add("objectclass", "organizationalPerson")
				.add("objectclass", "top")
				.add("uid",Integer.toString(user.getUid()))
				.add("cn", user.getCn())
				.add("sn", user.getSn())
				.add("userpassword", user.getPassword())
				.add("ou", "People")
				.add("description", user.getDescription());
		} catch (LdapException e) {
			throw new DapException("Failed to create User entry", e);
		}
		return result;
	}
	
	/*
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
	*/
	public void close() throws DapException{
		if(connection.isConnected()){
			try{
				connection.close();
			}catch(IOException ex){
				throw new DapException("It failed to close connection", ex);
			}
		}
	}

	public Dn getBaseDn() {
		return baseDn;
	}

	public void setBaseDn(Dn baseDn) {
		this.baseDn = baseDn;
	}
	
	public static void main (String[] args) {
		
		try {
			Dn test = new Dn("uid=" + Integer.toString(2),"ou=people","dc=la-etienda","dc=com");
			System.out.println(test.getName());
			System.out.println(test.getNormName());
			System.out.println(test.toString());
		} catch (LdapInvalidDnException e) {
			e.printStackTrace();
		}
		
	}
	
}
