package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.tomcat.Service;
import com.laetienda.tomcat.TomcatException;
import com.laetienda.logger.LoggerManager;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.db.SqlException;
import com.laetienda.lang.LangException;
import com.laetienda.lang.LangManager;
import com.laetienda.multimedia.MediaManager;
import com.laetienda.multimedia.MultimediaException;
import com.laetienda.notes.NotesException;
import com.laetienda.notes.NotesManager;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Load implements ServletContextListener {
	
	final static Logger log4j = LogManager.getLogger(Load.class);
	
	private File directory;
	private LoggerManager logManager;
	private LangManager langManager;
	private DbManager dbManager;
	private MediaManager mediaManager;
	private NotesManager notesManager;
	private DapManager dapManager;
	
	public void contextDestroyed(ServletContextEvent arg0){
		
		log4j.info("closing cafeteros web application");
		
		if(notesManager != null){
			
		}
		
		if(langManager != null){
			
			try{
				langManager.exportLang();
			}catch(SqlException ex){
				log4j.fatal("Failed to export language to CSV file. Information might be lost", ex);
				exit();
			}
		}
		
		if(dbManager != null){
			dbManager.close();
		}
		
		log4j.info("cafeteros web application has closed successfully");
		if(logManager != null){
			logManager.close();
		}
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		log4j.error("Testing log4j2 logger");
		ServletContext sc = arg0.getServletContext();
		directory = new File(sc.getInitParameter("directory"));
		
		try{
			dbManager = new DbManager(directory);
			sc.setAttribute("dbManager", dbManager);
			log4j.info("database library has loaded succesfully");
		}catch(DbException ex){
			log4j.fatal(ex);
			exit();
		}
		
		try{
			langManager = new LangManager(directory);
			sc.setAttribute("langManager", langManager);
			log4j.info("language library has loaded succesfully");
		}catch(LangException ex){
			log4j.fatal(ex);
			exit();
		}
		
		try{
			mediaManager = new MediaManager(directory);
			sc.setAttribute("mediaManager", mediaManager);
			log4j.info("multimedia library has loaded succesfully");
		}catch(MultimediaException ex){
			log4j.fatal(ex);
			exit();
		}
			
		try{
			notesManager = new NotesManager(directory);
			sc.setAttribute("notesManager", notesManager);
			log4j.info("notes library has loaded succesfully");
		}catch(NotesException ex){
			log4j.fatal(ex);
			exit();
		}
		
		try{
			dapManager = new DapManager(directory);
			//Db db = dbManager.createTransaction();
			sc.setAttribute("dapManager", dapManager);
			log4j.info("DAP library has been loaded succesfully");
			/*}catch(DapException ex){
				log4j.fatal(ex);
				dapManager.stopDapServer();
				dbManager.closeTransaction(db);
				exit();
			}*/
			
		}catch(DapException ex){
			log4j.fatal(ex);
			exit();
		}
			
		try{
			langManager.importLang();
			
		}catch(SqlException ex){
			log4j.fatal(ex);
		}
			
		log4j.info("Framework has loaded succesfully");
	}
	
	private void exit(){
		
		try{
			Service daemon = new Service(directory);
			daemon.shutdown();
		}catch(TomcatException ex1){
			log4j.fatal("Tomcat was not able to shutdown");
			System.exit(-1);
		}
	}
}
