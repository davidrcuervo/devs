package com.laetienda.dap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;

public class Installer {
	
	private DapManager dapManager;
	private String rootPassword;
	
	public Installer(File directory) throws DapException{
		rootPassword = "secret";
		dapManager = new DapManager(directory);
	}
	
	public void setRootPassword(String password){
		rootPassword = password;
	}
	
	public void install(String user, String password) throws DapException{
		
		LdapConnectionConfig config = new LdapConnectionConfig();
		LdapConnection connection = null; 
		
		try{
			config.setLdapHost(dapManager.getSetting("server_address"));
			config.setLdapPort(Integer.parseInt(dapManager.getSetting("service_port")));
			config.setName(String.format(user));
			config.setCredentials(String.format(password));
			
			DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
			GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
			
			LdapConnectionPool connectionPool = new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(factory), poolConfig);
			connection = connectionPool.getConnection();
			
			setupDomain(connection);
			
		}catch(NumberFormatException ex){
			throw new DapException("Service port number is not valid", ex);
		}catch(LdapException ex){
			throw new DapException("Failed to start connection", ex);
		}finally{
			if(connection != null && connection.isConnected()){
				try{
					connection.close();
				}catch(IOException ex){
					
				}
			}
		}
 	}
	
	private void setupDomain(LdapConnection connection) throws DapException{
		System.out.println("Installing");
		try{
			if(!connection.exists(Ldif.PEOPLE_DN())){
				connection.add(Ldif.PEOPLE_ENTRY());
			}
			
			if(!connection.exists(Ldif.SYSADMIN_DN())){
				connection.add(Ldif.SYSADMIN_ENTRY(rootPassword));
			}
			
			if(!connection.exists(Ldif.TOMCAT_USER_DN())){
				connection.add(Ldif.TOMCAT_USER_ENTRY(dapManager.getSetting("tomcatpassword")));
			}
			
			if(!connection.exists(Ldif.GROUPS_DN())){
				connection.add(Ldif.GROUPS_ENTRY());
			}
			
			if(!connection.exists(Ldif.SYSADMINS_DN())){
				connection.add(Ldif.SYSADMINS_ENTRY());
			}
			
			if(!connection.exists(Ldif.MANAGERS_DN())){
				connection.add(Ldif.MAANGERS_ENTRY());
			}
			
			/*
			if(!connection.exists(Ldif.USERS_DN())){
				connection.add(Ldif.USERS_ENTRY());
			}
			
			
			if(!connection.exists(Ldif.VISITORS_DN())){
				connection.add(Ldif.VISITORS_ENTRY());
			}
			*/
			
			if(!connection.exists(Ldif.ACI_SYSADMIN_DN())){
				connection.add(Ldif.ACI_SYSADMIN());
			}
			
			if(!connection.exists(Ldif.ACI_MANAGER_DN())){
				connection.add(Ldif.ACI_MANAGER());
			}
			
			if(!connection.exists(Ldif.ACI_USER_DN())){
				connection.add(Ldif.ACI_USER());
			}
			
			if(!connection.exists(Ldif.ACI_TOMCAT_DN())){
				connection.add(Ldif.ACI_TOMCAT_SUBENTRY());
			}
			
		}catch(LdapException ex){
			throw new DapException("Unable to setup domain elements in LDAP" ,ex);
		}
	}
}
