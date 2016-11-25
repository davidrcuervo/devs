package com.laetienda.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

public class Connection {
	
	//private final String[] SETTINGS = {"SERVER", "DATABASE", "PORT", "USER", "PASSWORD", 
	//		"PLATFORM", "SCRIPT", "CREATE-SOURCE", "PERSISTENCE_UNIT_NAME"};
	
	private ArrayList<EntityManager> ems;
	private EntityManagerFactory emfactory;
	private Map<String, String > settings;
	
	public Connection() throws IOException{
	    
		settings = new HashMap<String, String>();
    	ems = new ArrayList<EntityManager>();
    }
	
	 public synchronized void open() throws IOException{
	    
	   	try{
    		emfactory = Persistence.createEntityManagerFactory(
    				settings.getProperty("PERSISTENCE_UNIT_NAME"),
    				map
    				);
    	}catch (IllegalStateException ex){
	    		
    		throw new IOException(ex);
    	}catch(PersistenceException ex){
    		
    		throw new IOException(ex);
    	}
    }
	
	private Properties setDefaultSettings(){
		
		Properties defaults = new Properties();
		
		defaults.setProperty(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		defaults.setProperty("PERSISTENCE_UNIT_NAME", "db");
		defaults.setProperty(JDBC_DRIVER, "org.postgresql.Driver");
		defaults.setProperty(JDBC_URL, "jdbc:postgresql://db.la-etienda.lan:5432/logger");
		defaults.setProperty(JDBC_USER, "logger");
		defaults.setProperty(JDBC_PASSWORD, "1234");
		defaults.setProperty(TARGET_SERVER, "false");
		defaults.setProperty("javax.persistence.schema-generation.database.action", "create");
		defaults.setProperty("javax.persistence.schema-generation.create-source", "metadata");
		//defaults.setProperty("javax.persistence.schema-generation.create-script-source", "META-INF/postgres/create.sql");
		
		return new Properties(defaults);
	}
	
	public synchronized void loadConfFile(String path) throws IOException{
		
		FileInputStream conf;
		File directory = new File(path);
		
		if(directory.exists() && directory.isDirectory()){
			
			try{
				conf = new FileInputStream(new File(directory.getAbsolutePath() + "/etc/database/conf.xml"));
				settings.loadFromXML(conf);
				
				String db_host = settings.getProperty("db_host");
				String db_port = settings.getProperty("db_port");
				String database = settings.getProperty("database");
				String db_username = settings.getProperty("db_username");
				String db_password = settings.getProperty("db_password");
				
				settings.setProperty(JDBC_URL, "jdbc:postgresql://" + db_host + ":" + db_port + "/" + database);
				settings.setProperty(JDBC_USER, db_username);
				settings.setProperty(JDBC_PASSWORD, db_password);
				
			}catch(Exception ex){
				throw new IOException("Exception: " + ex.getClass().getName() + "\n"
						+ "Exception message: " + ex.getMessage() + "\n"
						+ "Failed to load conf file. $file: " + path + "/etc/database/conf.xml" );
			}finally{
				
			}
		}else{
			throw new IOException("No valid application path. $path: " + path);
		}
	}
	
	public synchronized Connection setSetting(String key, String value) throws IOException{
		
		//boolean flag = false;
		//key = key.toUpperCase();
		
		//for(String temp : SETTINGS){
		//	if(temp.equals(key)){
				settings.setProperty(key, value);
		//		flag =true;
			//	break;
		//	}
		//}
		
		//if(!flag){
		//	throw new IOException("The setting is not valid. $key: " + key);
		//}
		
		return this;
	}
	
	public String getSetting(String key){
		return settings.getProperty(key);
	}
   
    public synchronized EntityManager getEm(){
    	
    	EntityManager em = getEmfactory().createEntityManager();
    	ems.add(em);
    	
    	return em;
    }
    
    public synchronized boolean closeEm(EntityManager em){
    	
    	boolean result = false;
    	
    	if(ems.contains(em)){
    		int index = ems.indexOf(em);
    		EntityManager temp = ems.get(index);
    		if(temp.isOpen()){
    			temp.clear();
    			temp.close();
    		}
    		ems.remove(index);
    		result = true;
    	}
    	
    	return result;
    }
    
    public void close(){
    	
    	for(EntityManager em : ems){
    		if(em.isOpen()){
    			em.close();
    		}
    	}
    	
    	getEmfactory().close();
    }
    
    private EntityManagerFactory getEmfactory(){
    	return this.emfactory;
    }
}
