package com.laetienda.dap;

import java.io.IOException;
import java.util.Iterator;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.entities.User;

public class Ldap {
	private final static Logger log4j = LogManager.getLogger(Ldap.class);
	
	private Dn domainDn;
	
	public Ldap() {
		try {
			domainDn = new Dn(Ldif.getDomain());
		} catch (LdapInvalidDnException e) {
			log4j.error("Failed to find domain Dn. $error: {}", e.getMessage());
			domainDn = null;
		}
	}
	
	public Dn getDomainDn() {
		return domainDn;
	}
	
	public Dn buildDn(String temp) {
		Dn result = null;
		
		try {
			result = new Dn(temp, domainDn.getName());
		} catch (LdapInvalidDnException e) {
			log4j.debug("Failed to create Dn. $Error: {}", e.getMessage());
		}
		
		return result;
	}
	
	public Entry searchDn(String dnstr, LdapConnection conn) throws DapException {
		Entry result = null;
		
		try {
			Dn dn = new Dn(dnstr);
			result = searchDn(dn,conn);
		} catch (LdapInvalidDnException e) {
			throw new DapException(e);
		}
		
		return result;
	}
	
	public Entry searchDn(Dn dn, LdapConnection conn) throws DapException {
		Entry result = null;
		EntryCursor cursor = null;;
		try {
			cursor = conn.search(dn, "(objectclass=*)",  SearchScope.OBJECT);
			
			if(cursor.next()) {
				result = cursor.get();
			}
			
			if(cursor.iterator().hasNext()) {
				result = null;
				log4j.error("dn: %s exists more than once", dn);
			}
			
		} catch (LdapException  | CursorException e) {
			log4j.warn("Entry does not exist in Directory");
			log4j.debug(e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		
		return result;
	}
	
	public void insertUser(User user, LdapConnection conn) throws DapException {
		
		try {
			log4j.debug("User Dn. $dn: {}", user.getLdapEntry().getDn().getName());
			
			if(log4j.isDebugEnabled()) {
				Iterator<Attribute> iterator = user.getLdapEntry().getAttributes().iterator();
				while(iterator.hasNext()) {
					Attribute atr = iterator.next();
					log4j.debug("$Attribute: {} -> $Value: {}", atr.getId(), atr.get());
				}
			}
			conn.add(user.getLdapEntry());
		} catch (LdapException e) {
			throw new DapException(e);
		}
	}
	
	public Entry getPeopleLdapEntry(LdapConnection conn) throws DapException {
		Entry result = null;
		
		try {
			Dn peopleDn = new Dn("ou=People", Ldif.getDomain());
//			log4j.debug("peopleDn to be search. $peopleDn: {}", peopleDn.getName());
			result=searchDn(peopleDn, conn);
			log4j.debug("People Dn has been found. $peopleDn: {}", result.getDn().getName());
		} catch (LdapInvalidDnException e) {
			throw new DapException(e);
		}
		
		return result;
	}


	private void closeCursor(EntryCursor cursor) throws DapException {
		try {
			if(cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}catch(IOException e) {
			throw new DapException(e);
		}
	}
}
