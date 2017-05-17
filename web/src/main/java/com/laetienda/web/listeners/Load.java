package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.tomcat.Service;
import com.laetienda.logger.LoggerManager;
import com.laetienda.logger.JavaLogger;
import com.laetienda.logger.LoggerException;
import com.laetienda.db.DbManager;
import com.laetienda.db.SqlException;
import com.laetienda.lang.LangManager;
import com.laetienda.multimedia.MediaManager;
import com.laetienda.multimedia.MultimediaException;
import com.laetienda.notes.NotesException;
import com.laetienda.notes.NotesManager;
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
	
	public void contextDestroyed(ServletContextEvent arg0){
		
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
		
		if(dbManager != null){
			dbManager.close();
		}
		
		log.info("cafeteros web application has closed successfully");
		if(logManager != null){
			logManager.close();
		}
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		
		ServletContext sc = arg0.getServletContext();
		directory = new File(sc.getInitParameter("directory"));
		
		try{
			
			logManager = new LoggerManager(directory);
			sc.setAttribute("logManager", logManager);
			log = logManager.createJavaLogger();
		}catch (LoggerException ex){
			if(ex.getParent() != null){
				System.err.println(ex.getMessage());
				ex.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
			exit();
		}
		
		log.info("Loading applicatoin libraries");
			
		try{
			dbManager = new DbManager(directory);
			sc.setAttribute("dbManager", dbManager);
			
			langManager = new LangManager(directory, logManager.createJavaLogger());
			sc.setAttribute("langManager", langManager);
			
			mediaManager = new MediaManager(directory);
			sc.setAttribute("mediaManager", mediaManager);
			
			notesManager = new NotesManager(directory);
			sc.setAttribute("notesManager", notesManager);
			
			dapManager = new DapManager(directory);
			
			try{
				dapManager.startDapServer();
				sc.setAttribute("dapManager", dapManager);
			}catch(DapException ex){
				log.exception(ex);
				dapManager.stopDapServer();
			}
			
			try{
				langManager.importLang();
				
			}catch(SqlException ex){
				log.exception(ex);
			}
			
			log.info("Framework has loaded succesfully");
			
		}catch (MultimediaException ex){
			log.exception(ex);
			exit();
		}catch (NotesException ex){
			log.exception(ex);
			exit();
		}catch (DapException ex){
			log.exception(ex);
			this.exit();
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
