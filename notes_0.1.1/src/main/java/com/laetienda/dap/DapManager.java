package com.laetienda.dap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import com.laetienda.app.Aes;
import com.laetienda.app.AppException;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DapManager {
	
	private final static Logger log4j = LogManager.getLogger(DapManager.class);
	
	private Properties settings;
	private LdapConnectionPool connectionPool;
	private List<LdapConnection> connections;
	private Entry people;
	private Entry tomcat;
	private Entry base;
	
	/*
	public Entry getPeople() {
		return people;
	}
	*/

	public DapManager(File directory) throws DapException{
		connections = new ArrayList<LdapConnection>();
		settings = loadSettings(directory);
		Ldif.setDomain(settings.getProperty("domain"));
		setTomcatAndBase();
//		connectionPool = startLdapConnectionPool();
//		people = setPeopleLdapEntry();
//		testConnection();
	}
	

	private void setTomcatAndBase() throws DapException {
		try {
			base = new DefaultEntry(Ldif.getDomain());
			tomcat = new DefaultEntry("uid=tomcat,ou=people" + base.getDn().getName());
		} catch (LdapException e) {
			throw new DapException(e);
		}
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
	/*
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
			throw new DapException(ex);
		}finally{

		}
		
		return connection;
	}
	*/
	
	public synchronized Dap createDap() throws DapException {
		Dap result = null;
		
		try {
			Dn baseDn = new Dn(Ldif.getDomain());
			DefaultEntry ldapTomcat = new DefaultEntry("uid=tomcat,ou=people", baseDn);
			DefaultEntry ldapBaseDit = new DefaultEntry(baseDn);
			result = new Dap(createLdap(), ldapTomcat, ldapBaseDit, settings.getProperty("domain"));
		}catch(LdapException ex) {
			throw new DapException(ex);
		}
			
		return result;
	}
	
	public synchronized LdapConnection createLdap() throws DapException {
		log4j.debug("Connection to {}:{}", getSetting("server_address"), Integer.parseInt(getSetting("service_port")));
		LdapConnection conn = new LdapNetworkConnection(getSetting("server_address"), Integer.parseInt(getSetting("service_port")) ,true);
		try {
			conn.bind(tomcat.getDn(), getSetting("tomcatpassword"));
//			conn.bind()
		} catch (LdapException e) {
			closeConnection(conn);
			throw new DapException(e);
		}
		return conn;
	}
	
	public synchronized void closeConnection(LdapConnection connection) throws DapException{
		try{
			
			if(connection != null) {
				if(connection.isConnected() || connection.isAuthenticated()) {
					connection.unBind();
				}
				
				if(connection.isConnected()){
					connection.close();
				}
			}
		
			connections.remove(connection);
			
		}catch(IOException | LdapException | NullPointerException ex){
			throw new DapException(ex);
		}
	}
	
	public void closeConnection(Dap dap) throws DapException {
		if(dap != null && dap.getConnection() != null) closeConnection(dap.getConnection());
	}
	
	/*
	private LdapConnectionPool startLdapConnectionPool() throws DapException{
		
		LdapConnectionConfig config = new LdapConnectionConfig();
		
		try{
			config.setLdapHost(settings.getProperty("server_address"));
			config.setLdapPort(Integer.parseInt(settings.getProperty("service_port")));
			config.setUseTls(Boolean.parseBoolean(settings.getProperty("use_tls")));
			config.setUseSsl(Boolean.parseBoolean(settings.getProperty("use_ssl")));
			log4j.debug("$use_tls: " + Boolean.parseBoolean(settings.getProperty("use_tls")));
			log4j.debug("ssl protocol: " + config.getSslProtocol());
			config.setName(String.format(Ldif.TOMCAT_USER_DN().toString()));
			config.setCredentials(String.format(settings.getProperty("tomcatpassword")));
		}catch(NumberFormatException ex){
			throw new DapException("It was not able to connect to the server", ex);
		}
		
		DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
		factory.setTimeOut(0);
	
		GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
		
		return new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(factory), poolConfig);
	}
	*/
	public String getSetting(String key){
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
		Aes aes = new Aes();
		String pass;
		
		try{
			FileInputStream conf = new FileInputStream(new File(path));
			result.loadFromXML(conf);
			
			//Pass recovered password by Aes class to decipher passwords
			log4j.debug("$tomcatpassword: " + result.getProperty("tomcatpassword"));
			pass = aes.decrypt(result.getProperty("tomcatpassword"), "tomcat");
			result.setProperty("tomcatpassword", pass);
//			pass = aes.decrypt(result.getProperty("user_password"), "user");
//			result.setProperty("user_password", pass);
			
		}catch(IOException | AppException ex){
			log4j.error("Failed to load DAP configuration file. $exception: " + ex.getMessage());
			throw new DapException(ex.getMessage(), ex);
		}
		
		return result;
	}
	/*
	private Entry setPeopleLdapEntry() throws DapException {
		
		int c=0;
		Entry result = null;
		LdapConnection ldap = createLdap();
		
		try {
			Dn peopleDn = new Dn("ou=People", Ldif.getDomain());
			EntryCursor cursor = ldap.search( peopleDn, "(objectclass=*)", SearchScope.OBJECT );
			
			for(Entry entry : cursor) {
				result = entry;
				c++;
			}
			cursor.close();
			
			if(c < 1 || c > 1) {
				result = new DefaultEntry(peopleDn);
			}
			
		} catch (LdapException | IOException e) {
			throw new DapException(e);
		}
		
		return result;
	}
	*/
	
	private void testConnection() throws DapException{
		Ldap ldap = new Ldap();
		LdapConnection conn = createLdap();

		Entry result = ldap.searchDn(base.getDn().getName(), conn);
			
		if(result == null) {
			throw new DapException("Failed to connect and read database");				
		}else {
			log4j.debug("Application has connected to LDAP service succesfully");
		}
		closeConnection(conn);
	}
	
	public static void main(String[] args) throws DapException, IOException{
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:\\Users\\i849921\\git\\devs\\web\\target\\classes"); //SAP lenovo
		log4j.info("Starting LDAP module");
		
		DapManager dapManager = null;
		LdapConnection connection = null;
		Ldap ldap = new Ldap();
		
		try{
			
//			dapManager = new DapManager(directory);
//			connection = dapManager.createLdap();
			connection = new LdapNetworkConnection("homeServer3.la-etienda.com", 636, true); 
			connection.bind("cn=admin,dc=example,dc=com", "Welcome1");
							
//			Example to search entries of a master entry
//			EntryCursor cursor = connection.search("ou=People,dc=la-etienda,dc=com", "(|(cn=tomcat)(cn=sysadmin))", SearchScope.ONELEVEL);
				
			Entry result = ldap.searchDn("cn=admin,dc=example,dc=com", connection);
			
			if(result == null) {
				log4j.debug("Nothing found");				
			}else {
				log4j.debug("Object found in OpenLDAP");
				log4j.debug("dn: {}", result.getDn().getName());
				log4j.debug("cn: {}", result.get("cn").getString());
				log4j.debug("description: {}", result.get("description").getString());
			}

			/*
			for(Entry entry : cursor) {
				if(entry != null) {
					log4j.debug("entry: " + entry.get("uid").getString());
				}
			}
			cursor.close();
			*/	
//			connection.unBind();
		}catch(DapException | LdapException e) {
			log4j.error(e);
			e.printStackTrace();
		}finally{
			connection.close();
//			dapManager.closeConnection(connection);
//			dapManager.stopDapServer();
			log4j.info("Game Over");
		}
	}
}
