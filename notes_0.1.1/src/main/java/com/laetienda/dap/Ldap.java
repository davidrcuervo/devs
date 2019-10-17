package com.laetienda.dap;

import org.apache.directory.ldap.client.api.LdapConnection;

public class Ldap {
	
	private LdapConnection connection;
	
	public Ldap(LdapConnection connection) {
		setConnection(connection);
	}

	public LdapConnection getConnection() {
		return connection;
	}

	public void setConnection(LdapConnection connection) {
		this.connection = connection;
	}

}
