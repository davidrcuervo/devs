package com.laetienda.dap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.persistence.NoResultException;

import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;

import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.entities.Identifier;
import com.laetienda.entities.User;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;

public class DapManager {
	
	private Properties settings;
	private LdapConnectionPool connectionPool;
	private ArrayList<LdapConnection> connections;
	
	public DapManager(File directory) throws DapException{
		connections = new ArrayList<LdapConnection>();
		settings = loadSettings(directory);
		Ldif.setDomain(settings.getProperty("domain"));
	}
	
	public void startDapServer(Db db) throws DapException{

		connectionPool = startLdapConnectionPool();
		startDbConnection(db);
	}
	
	public void stopDapServer(){
		
		for(LdapConnection connection : connections){
			closeConnection(connection);
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
	
	public synchronized void closeConnection(LdapConnection connection){
		try{
			if(connection.isConnected()){
				connection.close();
			}
			
			if(connection.isAuthenticated()){
				//TODO
			}
			
			connections.remove(connection);
		}catch(IOException ex){
			System.out.println(ex.getMessage());
		}
	}
	
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
	
	private LdapConnectionPool startLdapConnectionPool() throws DapException{
		
		LdapConnectionConfig config = new LdapConnectionConfig();
		
		try{
			config.setLdapHost(settings.getProperty("server_address"));
			config.setLdapPort(Integer.parseInt(settings.getProperty("service_port")));
			config.setName(String.format(Ldif.TOMCAT_USER_DN().toString()));
			config.setCredentials(String.format(settings.getProperty("user_password")));
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
	
	public static void main(String[] args){
	/*	File directory = new File("/home/myself/git/eclipse/Web.opt");
		
		try{
			DapManager dapManager = new DapManager(directory);
			
			try{
				dapManager.startDapServer();
				
			}catch(DapException ex){
				ex.printStackTrace();
				ex.getParent().printStackTrace();
			}finally{
				dapManager.stopDapServer();
			}
			
		}catch(DapException ex){
			ex.printStackTrace();
			ex.getParent().printStackTrace();
		}finally{
			System.out.println("Closing the application");
		}
		*/
	}
}
