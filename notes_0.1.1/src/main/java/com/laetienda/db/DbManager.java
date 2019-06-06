package com.laetienda.db;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;
import org.apache.logging.log4j.Logger;

import com.laetienda.app.Aes;

import org.apache.logging.log4j.LogManager;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

public class DbManager {
	static final Logger log4j = LogManager.getLogger(DbManager.class);
	
	private ArrayList<EntityManager> ems;
	private EntityManagerFactory emfactory;
	private Properties defaults;
	private Map<String, String > settings;
	private File directory;
	
	public static void main (String args[]){
		
		try{
			File file = new File("/home/myself/git/eclipse/Web.opt");
			DbManager test = new DbManager(file);
			test.close();
		}catch(DbException ex){
			
		}
	}
	
	public DbManager(File directory) throws DbException{
		
		this.directory = directory;
		defaults = setDefaultSettings();
		settings = loadConfFile(directory);
		ems = new ArrayList<EntityManager>();
//		open();	
	}
	
	/**
	 * 
	 * @return Db instance
	 */
    public synchronized Db createTransaction(){
    	
    		return new Db(getEm()); 
    }
    
    public synchronized EntityManager getEm(){
    	
	    	EntityManager em = emfactory.createEntityManager();
	    	ems.add(em);
	    	
	    	return em;
    }
    
    public void closeTransaction(Db db){
    	
    		if(db != null) {
    			db.getEm().clear();
    			closeEm(db.getEm());
    			db = null;
    		}
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
    
    protected DbManager setCreateDatabaseVariable() {
    	settings.put("javax.persistence.schema-generation.database.action", "create");
    	return this;
    }
	
	public synchronized DbManager open() throws DbException{
		    
		try{ 
		   		
	   		/*
	   		 * ENABLE FOR DEBUGGING ONLY
	   		 * -------------------------
	   		 */
	   		
	   		for(Map.Entry<String, String> temp : settings.entrySet()){
	   			log4j.debug(temp.getKey() + ": " + temp.getValue());
	   		}
	   		
	   		/*
	   		 * End of debugging
	   		 * -------------------------------
	   		 */
	   		
    		emfactory = Persistence.createEntityManagerFactory(
    				settings.get("PERSISTENCE_UNIT_NAME"),
    				settings
				);
    	}catch (IllegalStateException | PersistenceException ex){
    		throw new DbException(ex);
    	}
		
		return this;
 }
	
	public void close(){
    	
	    	for(EntityManager em : ems){
	    		if(em.isOpen()){
	    			em.close();
    		}
    	}
    	
	    if(emfactory != null) {
	    	emfactory.close();
	    }
    }
	 
	 private Properties setDefaultSettings(){
			
		Properties defaults = new Properties();
		
		defaults.setProperty(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		defaults.setProperty("PERSISTENCE_UNIT_NAME", "com.laetienda.db");
		defaults.setProperty(JDBC_DRIVER, "org.postgresql.Driver");
		defaults.setProperty(JDBC_URL, "jdbc:postgresql://db.la-etienda.lan:5432/db");
		defaults.setProperty(JDBC_USER, "db");
		defaults.setProperty(JDBC_PASSWORD, "www.myself.com");
							
		return new Properties(defaults);
	}

	private synchronized Map<String, String> loadConfFile(File directory) throws DbException{
		
		FileInputStream conf;
		Map<String, String> result = new HashMap<String, String>();
		Properties settings = new Properties(defaults);
		Aes aes = new Aes();
		String password;
		String path = directory.getAbsolutePath() 
				+ File.separator + "etc"
				+ File.separator + "database.conf.xml";
				
		if(directory.exists() && directory.isDirectory()){
			
			try{
				conf = new FileInputStream(new File(path));
				settings.loadFromXML(conf);
				
				for(String key : settings.stringPropertyNames()){
					result.put(key, settings.getProperty(key));
				}
				
				password = aes.decrypt(result.get("javax.persistence.jdbc.password"), result.get("javax.persistence.jdbc.user"));
				result.put("javax.persistence.jdbc.password", password);
				result.put(JDBC_URL, "jdbc:postgresql://" + result.get("db_host") + ":" + result.get("db_port") + "/" + result.get("database"));
				/*
				if(result.get("javax.persistence.schema-generation.scripts.create-target") != null){
					File temp = new File(result.get("javax.persistence.schema-generation.scripts.create-target"));
					result.put("javax.persistence.schema-generation.scripts.create-target", temp.toURI().toURL().toString());
				}
				*/
			}catch(Exception ex){
				throw new DbException("Exception: " + ex.getClass().getName() + "\n"
						+ "Exception message: " + ex.getMessage() + "\n"
						+ "Failed to load database conf file. $file: " + path);
			}finally{
				
			}
		}else{
			throw new DbException("No valid application path. $path: " + path);
		}
		
		return result;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}
}
