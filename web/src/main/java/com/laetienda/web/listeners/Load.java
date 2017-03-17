package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.tomcat.lib.Service;
import com.laetienda.log.LoggerManager;
import com.laetienda.log.JavaLogger;
import com.laetienda.log.LoggerException;
import com.laetienda.db.Connection;
import com.laetienda.db.exceptions.*;
import com.laetienda.lang.LangManager;

public class Load implements ServletContextListener {
	
	private File directory;
	private JavaLogger log;
	private LangManager langManager;
	private Connection dbManager;
	
	public void contextDestroyed(ServletContextEvent arg0){
		
		log.info("Closing webapp");
		dbManager.close();
		
		try{
			langManager.exportLang();
			
		}catch (SqlException ex){
			log.critical("Failed to close webapp objects");
			log.exception(ex);
		}finally{
			
		}
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		
		ServletContext sc = arg0.getServletContext();
		directory = new File(sc.getInitParameter("directory"));
		
		try{
			
			LoggerManager logManager = new LoggerManager(directory);
			sc.setAttribute("logManager", logManager);
			
			log = logManager.createJavaLogger();
			
			dbManager = new Connection(directory);
			sc.setAttribute("dbManager", dbManager);
			
			langManager = new LangManager(directory, logManager.createJavaLogger());
			langManager.importLang();
			sc.setAttribute("langManager", langManager);
			
			log.debug("Framework has loaded succesfully");
		}catch (LoggerException ex){
			
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
