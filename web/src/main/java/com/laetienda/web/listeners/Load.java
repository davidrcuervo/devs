package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.logger.LoggerManager;
import com.laetienda.logger.JavaLogger;
import com.laetienda.logger.LoggerException;
import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.db.SqlException;
import com.laetienda.lang.LangException;
import com.laetienda.lang.LangManager;
import com.laetienda.multimedia.MediaManager;
import com.laetienda.multimedia.MultimediaException;
import com.laetienda.notes.NotesException;
import com.laetienda.notes.NotesManager;
import com.laetienda.options.OptionException;
import com.laetienda.options.OptionsManager;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;

public class Load implements ServletContextListener {
	
	private File directory;
	private JavaLogger log;
	private LoggerManager logManager;
	private LangManager langManager;
	private DbManager dbManager;
	private MediaManager mediaManager;
	private NotesManager notesManager;
	private DapManager dapManager;
	private OptionsManager optManager;
	
	public void contextInitialized(ServletContextEvent arg0) {
		
		ServletContext sc = arg0.getServletContext();
		directory = new File(sc.getInitParameter("directory"));
		
		try{
			logManager = new LoggerManager(directory);
			sc.setAttribute("logManager", logManager);
			log = logManager.createJavaLogger();
			log.info("Logger library has started successfully");
		}catch (LoggerException ex){
			if(ex.getParent() != null){
				System.err.println(ex.getMessage());
				ex.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
			exit();
		}
			
		try{
			dbManager = new DbManager(directory);
			sc.setAttribute("dbManager", dbManager);
			log.info("database library has loaded succesfully");
		}catch(DbException ex){
			log.exception(ex);
			exit();
		}
		
		try{
			optManager = new OptionsManager(directory);
			sc.setAttribute("optManager", optManager);
			log.info("Options library has loaded succesfully");
		}catch(OptionException ex){
			log.exception(ex);
			exit();
		}
		
		try{
			langManager = new LangManager(directory, logManager.createJavaLogger());
			sc.setAttribute("langManager", langManager);
			log.info("language library has loaded succesfully");
		}catch(LangException ex){
			log.exception(ex);
			exit();
		}
		
		try{
			mediaManager = new MediaManager(directory);
			sc.setAttribute("mediaManager", mediaManager);
			log.info("multimedia library has loaded succesfully");
		}catch(MultimediaException ex){
			log.exception(ex);
			exit();
		}
			
		try{
			notesManager = new NotesManager(directory);
			sc.setAttribute("notesManager", notesManager);
			log.info("notes library has loaded succesfully");
		}catch(NotesException ex){
			log.exception(ex);
			exit();
		}
		
		try{
			dapManager = new DapManager(directory);
			Db db = dbManager.createTransaction();
			
			try{
				dapManager.startDapServer(db);
				sc.setAttribute("dapManager", dapManager);
				dbManager.closeTransaction(db);
				log.info("DAP library has been loaded succesfully");
			}catch(DapException ex){
				log.exception(ex);
				dapManager.stopDapServer();
				dbManager.closeTransaction(db);
				exit();
			}
			
		}catch(DapException ex){
			log.exception(ex);
			exit();
		}
			
		try{
			langManager.importLang();
			
		}catch(SqlException ex){
			log.exception(ex);
		}
			
		log.info("Framework has loaded succesfully");
	}
	
	public void contextDestroyed(ServletContextEvent arg0){
		close();
	}
	
	private void close(){
		
		log.info("closing cafeteros web application");
		
		if(notesManager != null){
			
		}
		
		if(langManager != null){
			
			try{
				langManager.exportLang();
			}catch(SqlException ex){
				log.critical("Failed to export language to CSV file. Information might be lost");
				log.exception(ex);
			}
		}
		
		if(optManager != null){
			
		}
		
		if(dbManager != null){
			dbManager.close();
		}
		
		log.info("cafeteros web application has closed successfully");
		if(logManager != null){
			logManager.close();
		}
	}
	
	private void exit(){
		
		String errorMessage = "It failed while loading libraries. Application is about to shutdown";
		
		if(log == null){
			System.out.println(errorMessage);
		}else{
			log.critical(errorMessage);
		}
		
		close();
		System.exit(-1);
	}
}
