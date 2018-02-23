package com.laetienda.lang;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.laetienda.db.Sql;
import com.laetienda.db.Db;
import com.laetienda.db.SqlException;

public class LangManager {
	
	final static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(LangManager.class);
	
	private File directory;
	private Properties settings;
    
    public LangManager(File directory) throws LangException{

    		this.directory = directory;
    		settings = loadSettings(directory);
    }
    
    public Lang createLangInterface(Db db) throws LangException {
    		return new Lang(db, this);
    }
    
    public String getSetting(String setting){
    		return settings.getProperty(setting);
    }
    
    public void exportLang() throws SqlException{
    	log4j.info("Exporting languages table to csv file");
    	
    	Sql sql = new Sql(directory);
		File file = new File(settings.getProperty("file_csv"));
		String query = "COPY lang_languages TO STDOUT WITH DELIMITER ',' CSV HEADER";
    		sql.queryToCsv(query, file);  	        
    }
    
    public void importLang() throws SqlException{
	    	log4j.info("Importing languages from .csv file to table");
	    	
	    	Sql sql = new Sql(directory);
	    	File file = new File(settings.getProperty("file_csv"));
	    	String query = "COPY lang_languages FROM STDIN WITH DELIMITER ',' CSV HEADER";
	    	
	    	sql.csvToTable(query, file);
    }
    
    private Properties loadSettings(File directory) throws LangException{
    	log4j.info("Loading settings for Lang object");
    	
    	String path = directory.getAbsolutePath() 
				+ File.separator + "etc"
				+ File.separator + "lang.conf.xml";
    	
    	Properties defaults = new Properties();
    	defaults.setProperty("file_csv", directory.getAbsolutePath() + File.separator + 
    			"var" + File.separator + 
    			"lang" + File.separator + 
    			"langs.csv");
    	defaults.setProperty("default_lang", "en");
    	
    	Properties settings = new Properties(defaults);
    	
    	if(directory.exists() && directory.isDirectory()){
			
			try{
				FileInputStream conf = new FileInputStream(new File(path));
				settings.loadFromXML(conf);
			}catch (Exception ex){
				throw new LangException("Failed to load lang.conf.xml settings file", ex);
			}finally{
				
			}
    	}
    	
    	return settings;
	}
}
