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
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Installer {

	final static Logger log4j = LogManager.getLogger(Installer.class);

	
	private DapManager dapManager;
	private String rootPassword;
	private LdapConnection connection;
	
	public Installer(File directory) throws DapException{
		rootPassword = "secret";
		dapManager = new DapManager(directory);
	}
	
	public void setRootPassword(String password){
		rootPassword = password;
	}
	
	public void setConnection() throws DapException {
		try {
			setConnection(
				dapManager.getSetting("server_address"),
				Integer.parseInt(dapManager.getSetting("service_port"))
				);
		}catch(NumberFormatException ex) {
			throw new DapException(ex.getMessage(), ex);
		}
	}
	
	public void setConnection(String address, int port) throws DapException {
		log4j.info("Connecting to LDAP server...");
		log4j.debug("LDAP Server address: " + address + ":" + port); 
		
		boolean useSsl = Boolean.parseBoolean(dapManager.getSetting("use_tls"));
		connection = new LdapNetworkConnection(address, port, useSsl);
		
		if(connection.isConnected()){
			log4j.info("... it has connected to LDAP server succesfully");
		}else {
			log4j.error("Failed to connect LDAP server");
		}
	}
	
	public void closeConnection() throws DapException {
		log4j.info("Closing connection with the LDAP server...");
		
		try {
			connection.close();
			log4j.info("... connection with LDAP server has closed succesfully");
		} catch (IOException ex) {
			throw new DapException(ex);
		}
	}
	
	public void bind(String username, String password) throws DapException {
		log4j.info("Binding with the LDAP service...");
		log4j.debug("Binding username: " + username);
				
		try {
			connection.bind(username, password);
			log4j.info("... it has binded succesfully");
		}catch(LdapException ex) {
			
			//log4j.error("Failed to bind to LDAP service", ex);
			throw new DapException(ex);
		}
	}
	
	public void unBind() throws DapException {
		log4j.info("Unbinding from LDAP service...");
		
		try {
			connection.unBind();
			log4j.info("... it has unBinded succesfully");
		} catch (LdapException ex) {
			log4j.error("Failed to unBind from LDAP service", ex);
			throw new DapException("Failed to unBind from LDAP service", ex);
		}
	}
	
	@Deprecated
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
	
	@Deprecated
	private void setupDomain(LdapConnection connection) throws DapException{
		this.connection = connection;
		this.install();
	}
	
	public void install() throws DapException{
		log4j.info("Adding LDAP entries to LDAP directory...");
		
		try{
			if(!connection.exists(Ldif.PEOPLE_DN())){
				connection.add(Ldif.PEOPLE_ENTRY());
			}
			/*
			if(!connection.exists(Ldif.SYSADMIN_DN())){
				connection.add(Ldif.SYSADMIN_ENTRY(rootPassword));
			}
			
			if(!connection.exists(Ldif.TOMCAT_USER_DN())){
				connection.add(Ldif.TOMCAT_USER_ENTRY(dapManager.getSetting("tomcatpassword")));
			}
			*/
			if(!connection.exists(Ldif.GROUPS_DN())){
				connection.add(Ldif.GROUPS_ENTRY());
			}
			/*
			if(!connection.exists(Ldif.SYSADMINS_DN())){
				connection.add(Ldif.SYSADMINS_ENTRY());
			}
			
			if(!connection.exists(Ldif.MANAGERS_DN())){
				connection.add(Ldif.MAANGERS_ENTRY());
			}
			*/
			if(!connection.exists(Ldif.SERVICES_DN())) {
				connection.add(Ldif.SERVICES_ENTRY());
			}
			/*
			if(!connection.exists(Ldif.LDAP_DN())) {
				connection.add(Ldif.LDAP_ENTRY());
			}
			
			if(!connection.exists(Ldif.KRBTGT_DN())) {
				connection.add(Ldif.KRBTGT_ENTRY());
			}
			
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
			*/
			log4j.info("... LDAP entries to LDAP directory has been added succesfully");
			
		}catch(LdapException ex){
			log4j.error("Failed to add entries to LDAP directory", ex);
			throw new DapException("Unable to setup domain elements in LDAP" ,ex);
		}
	}
	
	public static void main(String[] args) {
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			Installer installer = new Installer(directory);
			try {
				installer.setConnection("localhost", 10389);
				installer.bind("uid=admin,ou=system", "secret");
				installer.install();
				installer.unBind();
			}catch(DapException ex) {
				log4j.error(ex.getMessage(), ex.getRootParent());
			}finally {
				try {
					installer.closeConnection();
				} catch (DapException e) {
					log4j.error(e.getMessage(), e);
				}
			}
				
		} catch (DapException ex) {
			log4j.error(ex.getMessage(), ex);
		}
	}
}
