package com.laetienda.dap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//import javax.persistence.NoResultException;

import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;

import com.laetienda.entities.User;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DapManager {
	
	final static Logger log4j = LogManager.getLogger(DapManager.class);
	
	private Properties settings;
	private LdapConnectionPool connectionPool;
	private List<LdapConnection> connections;
	private User tomcat;
	
	public DapManager(File directory) throws DapException{
		connections = new ArrayList<LdapConnection>();
		settings = loadSettings(directory);
		Ldif.setDomain(settings.getProperty("domain"));
		connectionPool = startLdapConnectionPool();
		setTomcat();
	}
	

	/*
	public void startDapServer() throws DapException{

		connectionPool = startLdapConnectionPool();
		//startDbConnection(db);
	}
	*/
	
	public synchronized void stopDapServer(){
		
		Iterator<LdapConnection> iter = connections.iterator();
		while(iter.hasNext()) {
			LdapConnection connection = iter.next();
			if(connection != null && connection.isConnected()) {
				try {
					connection.close();
				}catch(IOException ex) {
					log4j.error("Failed to close LDAP connection", ex);
				}
			}
		}
		connections.removeAll(connections);
		
	}
	
	public synchronized LdapConnection createConnection() throws DapException{
		log4j.debug("Creating LDAP connection from pool");
		LdapConnection connection = null;
		
		try{
			connection = connectionPool.getConnection();
			if(connection != null) {
				
					connections.add(connection);
					log4j.debug("LDAP Connection has been created succesfully");
			}
			
		}catch(LdapException ex){
			throw new DapException("Error while creating connection" ,ex);
		}finally{

		}
		
		return connection;
	}
	
	public synchronized Dap createDap() throws DapException {
		Dap result = null;
		
		try {
			result = new Dap(createConnection(), getTomcat(), new Dn(Ldif.getDomain()), settings.getProperty("domain"));
		}catch(LdapInvalidDnException ex) {
			throw new DapException("Failed to create Dap object", ex);
		}
			
		return result;
	}
	
	public synchronized void closeConnection(LdapConnection connection){
		try{
			
				if(connection.isConnected() || connection.isAuthenticated()) {
					connection.unBind();
				}
				
				if(connection != null && connection.isConnected()){
					connection.close();
				}
			
				connections.remove(connection);
			
		}catch(IOException | LdapException ex){
			log4j.error("Failed to close connection", ex);
		}
	}
	
	public void closeConnection(Dap dap) {
		if(dap != null && dap.getConnection() != null) closeConnection(dap.getConnection());
	}
	
	private LdapConnectionPool startLdapConnectionPool() throws DapException{
		
		LdapConnectionConfig config = new LdapConnectionConfig();
		
		try{
			config.setLdapHost(settings.getProperty("server_address"));
			config.setLdapPort(Integer.parseInt(settings.getProperty("service_port")));
			//config.setName(String.format(Ldif.TOMCAT_USER_DN().toString()));
			//config.setCredentials(String.format(settings.getProperty("user_password")));
		}catch(NumberFormatException ex){
			throw new DapException("It was not able to connect to the server", ex);
		}
		
		DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
		factory.setTimeOut(0);
	
		GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
		
		return new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(factory), poolConfig);
	}
	
	protected String getSetting(String key){
		return settings.getProperty(key);
	}
	
	private Properties loadSettings(File directory) throws DapException{
		
		Properties defaults = new Properties();
		defaults.setProperty("app_name", "app");
		defaults.setProperty("domain", "example.com");
		defaults.setProperty("server_address", "localhost");
		defaults.setProperty("service_port", "10389");
		defaults.setProperty("user_password", "secret");
		
		String dapFolderPath = directory.getAbsolutePath() + File.separator + "var" + File.separator + "dap";
		defaults.setProperty("dapFolderPath", dapFolderPath);
		
		Properties result = new Properties(defaults);
		String path = directory.getAbsolutePath() + File.separator + "etc" + File.separator + "dap" + File.separator + "dap.conf.xml";
		
		try{
			FileInputStream conf = new FileInputStream(new File(path));
			result.loadFromXML(conf);
		}catch(FileNotFoundException ex){
			throw new DapException("Failed to read conf file. $file: " + path, ex);
		}catch(InvalidPropertiesFormatException ex){
			throw new DapException("Failed to read properties from conf file. $file: " + path, ex);
		}catch(IOException ex){
			throw new DapException("Failed to read properties from conf file. $file: " + path, ex);
		}
		
		return result;
	}
	
	private void setTomcat() {
		tomcat = new User("tomcat", getSetting("tomcatpassword"));
	}
	
	protected User getTomcat() {
		return tomcat;
	}
	
	public static void main(String[] args){
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try{
			log4j.info("Starting LDAP module");
			DapManager dapManager = new DapManager(directory);
			log4j.info("LDAP module has started succesfully");
			
			LdapConnection connection = dapManager.createConnection();
		
			try {
				log4j.info("Binding with dap server");
				connection.bind("uid=1,ou=people,dc=la-etienda,dc=com", "secret");
				log4j.info("it has binded with dap server succesfully");
				
				/**
				 * Example to search entries of a master entry
				 */
				
				EntryCursor cursor = connection.search("ou=People,dc=la-etienda,dc=com", "(|(cn=tomcat)(cn=sysadmin))", SearchScope.ONELEVEL);
				
				
				for(Entry entry : cursor) {
					if(entry != null) {
						log4j.debug("entry: " + entry.get("uid").getString());
					}
				}
				cursor.close();
				
				connection.unBind();
			}catch(LdapException ex) {
				log4j.error("Failed to bind/search with dap server", ex);
			} catch (IOException ex) {
				log4j.error("Failed to close cursor", ex);

			}finally {
				dapManager.closeConnection(connection);
			}
		
			log4j.info("Stoping LDAP module");
			dapManager.stopDapServer();
			log4j.info("LDAP Module has stopped succesfully");
			
		}catch(DapException ex){
			log4j.fatal("Failed to load LDAP module", ex);
		}finally{
			log4j.info("Game Over");
		}
	}
}
