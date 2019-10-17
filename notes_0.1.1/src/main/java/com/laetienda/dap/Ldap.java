package com.laetienda.dap;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ldap {
	private final static Logger log4j = LogManager.getLogger(Ldap.class);
	
	public Ldap() {
	}
	
	
	public Entry searchDn(String dn, LdapConnection conn) throws DapException {
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
