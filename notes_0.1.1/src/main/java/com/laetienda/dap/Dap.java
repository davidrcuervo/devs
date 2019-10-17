package com.laetienda.dap;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.laetienda.entities.User;

public class Dap {
	private final static Logger log4j = LogManager.getLogger(Dap.class);
	
	private LdapConnection connection;
	private DefaultEntry base;
	private DefaultEntry tomcat;
	private String domain;
	
	public Dap(LdapConnection connection, DefaultEntry tomcat, DefaultEntry baseDn, String domain){
		this.connection = connection;
		this.tomcat = tomcat;
		this.domain = domain;
		setBase(baseDn);
	}
	
	@Deprecated
	public void insertUser(User newUser) throws DapException {
		
		EntryCursor search = null;
		
		try {
			log4j.debug("tomcat uid: " + tomcat.getUid());
			Dn peopleDn = new Dn("ou=People", baseDn.getName());
			Dn tomcatDn = new Dn("uid=" + tomcat.getUid(), "ou=People", baseDn.getName());
			
			connection.bind(tomcatDn, tomcat.getPassword());
			
			search = connection.search(peopleDn, 
					"(|(uid=" + newUser.getUid() + ")(mail=" + newUser.getEmail() + "))", 
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
	
	@Deprecated
	public Entry getUser(String username, String password) throws DapException {
		
		EntryCursor search;
		Entry result = null;
		int c = 0;
		
		if(checkPassword(username, password) == null) {
			throw new DapException("Username and password not correct");
		}else {
			try {
				Dn peopleDn = new Dn("ou=people", baseDn.getName());
				search = connection.search(peopleDn, "(uid=" + username + ")", SearchScope.ONELEVEL);
				
				for(Entry entry : search) {
					result = entry;
					c++;
				}
				if(c > 1) {
					throw new DapException("Search find more than one entry with same username");
				}
				
			} catch (LdapException e) {
				throw new DapException(e.getMessage(), e);	
			} 
		}
		
		return result;
	}
	
	@Deprecated
	public void changeUserPassword(User user) throws DapException {
		if(user != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
			try {
				Dn tomcatDn = new Dn("uid=" + tomcat.getUid(), "ou=People", baseDn.getName());
				Dn userDn = new Dn("uid=" + user.getUid(), "ou=People", baseDn.getName());
				
				Modification replacePassword = new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, "userpassword", user.getPassword());
				
				try {
					connection.bind(userDn, user.getPassword());
					log4j.debug("User is trying to use old password and it does not need to be replaced");
				}catch(LdapAuthenticationException ex) {
					connection.bind(tomcatDn, tomcat.getPassword());
					connection.modify(userDn, replacePassword);
				}
				
			} catch (LdapException ex) {
				throw new DapException("Failed to update password in LDAP", ex);
			}finally {
				try {
					if(connection.isConnected() || connection.isAuthenticated()) {
						connection.unBind();
					}
				} catch (LdapException e) {
					throw new DapException("Failed to close connection with ldap server", e);
				}
			}
		}else {
			throw new DapException("User or password was null or empty");
		}
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
	
	public DefaultEntry getBase() {
		return base;
	}

	protected LdapConnection getConnection() {
		return connection;
	}

	public void setBase(DefaultEntry base) {
		this.base = base;
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
	
	@Deprecated
	public Dap checkPassword (String uid, String password) throws DapException {
		boolean result = false;
		
		try {
			Dn userDn = new Dn("uid=" + uid, "ou=People", baseDn.getName());
			connection.bind(userDn, password);
			
			if(connection.isConnected() && connection.isAuthenticated()) {
				result = true;
				//connection.unBind();
			}
			
		} catch (LdapException e) {
			log4j.info("Failed to bind checkPassword for user. $username: " + uid);
			throw new DapException(e.getMessage(), e);
		}finally {

		}
		
		return result ? this : null;
	}
	@Deprecated
	public User userSyncDbAndLdap(User user) throws DapException {
		
		EntryCursor cursor = null;
		
		try {
			log4j.info("Synchronizing db user and LDAP. $uid: " + user.getUid());
			Dn userDn = new Dn("uid=" + user.getUid(), "ou=People", baseDn.getName());
			log4j.debug("$dap: "+ this);
			log4j.debug("$connection.isConnected: " + (connection.isConnected() ? "true" : "false"));
			log4j.debug("$connection.isAuthenticated: " + (connection.isAuthenticated() ? "true" : "false"));
			
			//if(!connection.isAuthenticated()){
				Dn tomcatDn = new Dn("uid=" + tomcat.getUid(), "ou=People", baseDn.getName());
				log4j.debug("$tomcatDn: " + tomcatDn.getName());
				connection.bind(tomcatDn, tomcat.getPassword());
		//	}
			
			cursor = connection.search(userDn, "(objectclass=*)", SearchScope.OBJECT);
			
			for(Entry entry : cursor) {
				log4j.debug("$entry: " + entry.get("uid").getString());
				if(entry.get("uid").getString().equals(user.getUid())) {
					log4j.debug("User found in LDAP. $cn: " + entry.get("cn").getString() + " $sn: " + entry.get("sn").getString());
					user.setCn(entry.get("Cn").getString());
					user.setSn(entry.get("sn").getString());
				}
			}
		
		}catch(NullPointerException | LdapException ex) {
			throw new DapException("Failed to sync db and LDAP user objects", ex);
		}finally {
			
			try {
				if(cursor != null && !cursor.isClosed())
					cursor.close();
			} catch (IOException e) {
				throw new DapException("Failed to close LDAP EntryCursor", e);
			}

		}
		
		return user;
	}
}
