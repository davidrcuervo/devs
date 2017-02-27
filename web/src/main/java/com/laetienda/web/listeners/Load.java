package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.tomcat.lib.Service;
import com.laetienda.log.bin.JavaLogger;
import com.laetienda.db.Connection;
import com.laetienda.db.exceptions.*;

import com.laetienda.lang.Lang;

public class Load implements ServletContextListener {
	
	private File directory;
	private JavaLogger log;
	private Connection dbManager;
	private Lang lang;
	
	public void contextDestroyed(ServletContextEvent arg0){
		
		log.info("Closing webapp");
		dbManager.close();
		
		try{
			lang.exportLang();
			
		}catch (SqlException ex){
			log.critical("Failed to close webapp objects");
			log.exception(ex);
		}finally{
			
		}
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		
		ServletContext sc = arg0.getServletContext();
		
		try{
			
			directory = new File(sc.getInitParameter("directory"));

			log = new JavaLogger(directory);
			dbManager = new Connection(directory);
			
			lang = new Lang(directory, dbManager.createTransaction(), log);
			lang.importLang();
			
			log.debug("Framework has loaded succesfully");
		
		}catch (SqlException ex){
			log.exception(ex);
			
		}catch (Exception ex){
			
			ex.printStackTrace();
			this.exit();
		}
	}
	
	private void exit(){
		try{
			Service daemon = new Service(directory);
			daemon.shutdown();
		}catch(Exception ex1){
			
			ex1.printStackTrace();
			System.exit(-1);
		}
	}
}
