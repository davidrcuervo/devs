package com.laetienda.lang;

import com.laetienda.entities.Language;
import com.laetienda.db.Db;
import com.laetienda.logger.JavaLogger;

public class Lang {
	public static String[] LANG_CODES = {"en", "es", "fr"};
	
	private Db db;
	private String langCode;
	private JavaLogger log;
	
	protected Lang(Db db, LangManager manager, JavaLogger log) throws LangException {
		this.log = log;
		this.db = db;
		setLang(manager.getSetting("default_lang"));
	}
	
	public String out(String identifier){
    	String result;
    	    	
    	try{
    		Language temp = db.getEm().createNamedQuery("Language.findByIdentifier", Language.class).setParameter("identifier", identifier).getSingleResult();
    		
    		switch (langCode){
			
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
	
	public void setLang(String lang) throws LangException{
		
		boolean flag = false;
		
		try{
			
			for(String langCode : LANG_CODES){
				if(lang.equals(langCode)){
					flag = true;
					this.langCode = lang;
				}
			}
					
			if(flag){
				//DO NOTHIG
			}else{
				throw new LangException("Language code not supported. $langCode: " + lang);
			}
		}catch(NullPointerException ex){
			throw new LangException(ex.getMessage(), ex);
		}finally{
			
		}
	}
}
