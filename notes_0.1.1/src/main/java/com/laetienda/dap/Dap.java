package com.laetienda.dap;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.log4j.Logger;

import com.laetienda.entities.User;

public class Dap {
	private final static Logger log4j = Logger.getLogger(Dap.class);
	
	private LdapConnection connection;
	private Dn baseDn;
	private User tomcat;
	private String domain;
	
	public Dap(LdapConnection connection, User tomcat, Dn baseDn, String domain){
		this.connection = connection;
		this.tomcat = tomcat;
		this.domain = domain;
		setBaseDn(baseDn);
	}
	
	public void insertUser(User newUser) throws DapException {
		
		EntryCursor search = null;
		
		try {
			log4j.debug("tomcat uid: " + tomcat.getUid());
			Dn peopleDn = new Dn("ou=People", baseDn.getName());
			Dn tomcatDn = new Dn("uid=" + tomcat.getUid(), "ou=People", baseDn.getName());
			
			connection.bind(tomcatDn, tomcat.getPassword());
			
			search = connection.search(peopleDn, 
					"(|(uid=" + newUser.getUid() + ")(cn=" + newUser.getEmail() + "))", 
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
	
	public void deleteUser(User user) {
		//TODO implement method that deletes user frod LDAP directory for testing proposes we can remove it manually.
	}
	
	public Entry getDapUserEntry(User user) throws DapException  {
		Entry result = null;
		try {
			Dn userDn = new Dn("uid=" + user.getUid(), "ou=People", baseDn.getName());
			result = new DefaultEntry(userDn);
			result.add("objectclass", "person")
				.add("objectclass", "inetOrgPerson")
				.add("objectClass", "krb5KDCEntry")
				.add("objectClass", "krb5Principal")
				.add("objectclass", "organizationalPerson")
				.add("objectclass", "top")
				.add("uid",user.getUid())
				.add("cn", user.getCn())
				.add("krb5KeyVersionNumber", "1")
				.add("sn", user.getSn())
				.add("krb5PrincipalName", user.getUid() + "@" + domain.toUpperCase())
				.add("userpassword", user.getPassword())
				//.add("description", "")
				.add("ou", "People");
		} catch (LdapException e) {
			throw new DapException("Failed to create User entry", e);
		}
		return result;
	}
	
	public Dn getBaseDn() {
		return baseDn;
	}

	protected LdapConnection getConnection() {
		return connection;
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
	
	public boolean checkPassword (String uid, String password) {
		boolean result = false;
		
		try {
			Dn userDn = new Dn("uid=" + uid, "ou=People", baseDn.getName());
			connection.bind(userDn, password);
			
			if(connection.isConnected() && connection.isAuthenticated()) {
				result = true;
				connection.unBind();
			}
			
		} catch (LdapException e) {
			log4j.info("Failed to bind checkPassword for user. $username: " + uid);
		}finally {

		}
		
		return result;
	}
	
}
