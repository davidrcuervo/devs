package com.laetienda.dap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

//import javax.persistence.NoResultException;

import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;

//import com.laetienda.db.Db;
//import com.laetienda.db.DbException;
//import com.laetienda.entities.Identifier;
//import com.laetienda.entities.User;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.Response;
import org.apache.directory.api.ldap.model.message.SearchRequest;
import org.apache.directory.api.ldap.model.message.SearchRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchResultEntry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;

public class DapManager {
	
	final static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(DapManager.class);
	
	private Properties settings;
	private LdapConnectionPool connectionPool;
	private ArrayList<LdapConnection> connections;
	private DapUser tomcat;
	
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
	
	public void stopDapServer(){
			
		for(LdapConnection connection : connections){
			try {
				closeConnection(connection);
			}catch(DapException ex) {
				log4j.error(ex.getMessage(), ex.getParent());
			}
		}
	}
	
	public synchronized LdapConnection createConnection() throws DapException{
		LdapConnection connection = null;
		
		try{
			connection = connectionPool.getConnection();
			connections.add(connection);
		}catch(LdapException ex){
			throw new DapException("Error while creating connection" ,ex);
		}finally{

		}
		
		return connection;
	}
	
	public synchronized Dap createDap() throws DapException {
		Dap result = null;
		
		try {
			result = new Dap(createConnection(), getTomcat(), new Dn(Ldif.getDomain()));
		}catch(LdapInvalidDnException ex) {
			throw new DapException("Failed to create Dap object", ex);
		}
			
		return result;
	}
	
	public synchronized void closeConnection(LdapConnection connection) throws DapException{
		try{
			if(connection.isConnected()){
				connection.close();
			}
			
			if(connection.isAuthenticated()){
				//TODO
			}
			
			connections.remove(connection);
		}catch(IOException ex){
			throw new DapException("Failed to close connection", ex);
		}
	}
	
	/*
	public void startDbConnection(Db db) throws DapException{
		
		try{
			Identifier identifier = db.getEm().createNamedQuery("Identifier.findByName", Identifier.class).setParameter("name", User.ID_NAME).getSingleResult();
			Integer id = identifier.getValue();
			
			if(User.FIRST_ID < id){
				throw new DapException("Identifier counter for user ubjects is smaller than first id. $id: " + id);
			}else{
				//It means that everything is ok and find to go.
			}
			
		}catch(NoResultException ex){
			Identifier identifier = new Identifier();
			identifier.setName(User.ID_NAME);
			identifier.setIdentifier(User.FIRST_ID);
			identifier.setDescription("Id counter to use to build user id \"uid\".");
			
			try{
				db.insert(identifier);
			}catch(DbException ex1){
				throw new DapException("Failed to create identifier counter for user objects", ex1.getParent());
			}
		}
	}
	*/
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
		tomcat = new DapUser(2, getSetting("tomcatpassword"));
	}
	
	protected DapUser getTomcat() {
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
