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
	
	private ArrayList<EntityManager> ems;
	private EntityManagerFactory emfactory;
	private Properties defaults;
	private Map<String, String > settings;
	
	public static void main (String args[]) throws IOException{
		
		File file = new File("/home/myself/git/eclipse/Web.opt");
		Connection test = new Connection(file);
		test.close();
	}
	
	public Connection(File directory) throws IOException{
	    
		defaults = setDefaultSettings();
		settings = loadConfFile(directory);
    	ems = new ArrayList<EntityManager>();
    	open();
    }
		
	 public synchronized void open() throws IOException{
	    
	   	try{ 
	   		
	   		/*
	   		 * ENABLE FOR DEBUGGING ONLY
	   		 * -------------------------
	   		 * 
	   		for(Map.Entry<String, String> temp : settings.entrySet()){
	   			System.out.println(temp.getKey() + ": " + temp.getValue());
	   		}
	   		*/
	   		
    		emfactory = Persistence.createEntityManagerFactory(
    				settings.get("PERSISTENCE_UNIT_NAME"),
    				settings
    				);
    	}catch (IllegalStateException ex){
	    	ex.printStackTrace();	
    		throw new IOException(ex);
    	}catch(PersistenceException ex){
    		
    		throw new IOException(ex);
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
	
	private synchronized Map<String, String> loadConfFile(File directory) throws IOException{
		
		FileInputStream conf;
		Map<String, String> result = new HashMap<String, String>();
		Properties settings = new Properties(defaults);
		String path = directory.getAbsolutePath() 
				+ File.separator + "etc"
				+ File.separator + "database"
				+ File.separator + "conf.xml";
				
		if(directory.exists() && directory.isDirectory()){
			
			try{
				conf = new FileInputStream(new File(path));
				settings.loadFromXML(conf);
				
				for(String key : settings.stringPropertyNames()){
					result.put(key, settings.getProperty(key));
				}
				
				result.put(JDBC_URL, "jdbc:postgresql://" + result.get("db_host") + ":" + result.get("db_port") + "/" + result.get("database"));
				/*
				if(result.get("javax.persistence.schema-generation.scripts.create-target") != null){
					File temp = new File(result.get("javax.persistence.schema-generation.scripts.create-target"));
					result.put("javax.persistence.schema-generation.scripts.create-target", temp.toURI().toURL().toString());
				}
				*/
			}catch(Exception ex){
				throw new IOException("Exception: " + ex.getClass().getName() + "\n"
						+ "Exception message: " + ex.getMessage() + "\n"
						+ "Failed to load database conf file. $file: " + path);
			}finally{
				
			}
		}else{
			throw new IOException("No valid application path. $path: " + path);
		}
		
		return result;
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
