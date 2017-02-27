package com.laetienda.lang;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.laetienda.db.Connection;
import com.laetienda.db.Transaction;
import com.laetienda.db.Sql;
import com.laetienda.db.exceptions.*;
import com.laetienda.lang.entities.Language;
import com.laetienda.log.bin.JavaLogger;

/**
 * Hello world!
 *
 */
public class Lang {
	
	private File directory;
	private Connection dbManager;
	private Transaction db;
	private JavaLogger log;
	private Properties settings;
			
    public static void main( String[] args ) throws Exception{
    	
    	File directory = new File("/home/myself/git/eclipse/Web.opt");
    	Lang lang = new Lang(directory);
    	lang.out("test");
    	lang.close();

    }
    
    public Lang(File directory) throws Exception {
    	this.directory = directory;
    	dbManager = new Connection(directory);
    	db = dbManager.createTransaction();
    	log = new JavaLogger(directory);
    	log.info("Construction Lang object.");
    	settings = loadSettings(directory);
    }
    
    public Lang(File directory, Transaction db, JavaLogger log){
    	this.db = db;
    	this.directory = directory;
    	this.log = log;
    	log.info("Construction Lang object.");
    	settings = loadSettings(directory);
    }
    
    public void close(){
    	
    	if(dbManager != null){
    		dbManager.close();
    	}
    }
    
    public String out(String identifier){
    	String result;
    	    	
    	try{
    		Language temp = db.getEm().createNamedQuery("Language.findByIdentifier", Language.class).setParameter("identifier", identifier).getSingleResult();
    		
    		switch (getLang()){
			
				case "es":
					result = temp.getSpanish();
					break;
			
				case "en":
					result = temp.getEnglish();
					break;
				
				case "fr":
					result = temp.getFrench();
					break;
				
				default:
					result = temp.getEnglish();
					break;
    		}
    		
    	}catch (Exception ex){
    		result = "<span style='color: red;'>" + identifier + "</span>";
    		log.error("The requested text does not exist in the language table. $identifier: " + identifier);
			log.exception(ex);
    	}finally{
    		db.getEm().clear();
    	}
    	
    	return result;
    }
    
    public String getLang(){
	
		return this.settings.getProperty("default_lang");
    }
    
    public void exportLang() throws SqlException{
    	log.info("Exporting languages table to csv file");
    	
    	Sql sql = new Sql(directory);
		File file = new File(settings.getProperty("file_csv"));
		String query = "COPY lang_languages TO STDOUT WITH DELIMITER ',' CSV HEADER";
    	sql.queryToCsv(query, file);  	        
    }
    
    public void importLang() throws SqlException{
    	log.info("Importing languages from .csv file to table");
    	
    	Sql sql = new Sql(directory);
    	File file = new File(settings.getProperty("file_csv"));
    	String query = "COPY lang_languages FROM STDIN WITH DELIMITER ',' CSV HEADER";
    	
    	sql.csvToTable(query, file);
    }
    
    private Properties loadSettings(File directory){
    	log.info("Loading settings for Lang object");
    	
    	String path = directory.getAbsolutePath() 
				+ File.separator + "etc"
				+ File.separator + "lang"
				+ File.separator + "conf.xml";
    	
    	Properties settings = new Properties();
    	settings.setProperty("file_csv", directory.getAbsolutePath() + File.separator + 
    			"var" + File.separator + 
    			"lang" + File.separator + 
    			"langs.csv");
    	settings.setProperty("default_lang", "en");
    	
    	if(directory.exists() && directory.isDirectory()){
			
			try{
				FileInputStream conf = new FileInputStream(new File(path));
				settings.loadFromXML(conf);
			}catch (Exception ex){
				log.exception(ex);
			}finally{
				
			}
    	}
    	
    	return settings;
	}
}
