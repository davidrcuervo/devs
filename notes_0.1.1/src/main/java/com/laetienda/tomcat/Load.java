package com.laetienda.tomcat;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.db.SqlException;
import com.laetienda.lang.LangException;
import com.laetienda.lang.LangManager;
import com.laetienda.multimedia.MediaManager;
import com.laetienda.multimedia.MultimediaException;
import com.laetienda.notes.NotesException;
import com.laetienda.notes.NotesManager;

public class Load implements ServletContextListener{
	
	final static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(Load.class);
	
	File directory;
	private DbManager dbManager;
	private LangManager langManager;
	private MediaManager mediaManager;
	private NotesManager notesManager;
	private DapManager dapManager;
	
	public void contextInitialized(ServletContextEvent arg0) {
		log4j.info("Tomcat context is initializing");
		
		ServletContext sc = arg0.getServletContext();
		directory = new File(sc.getInitParameter("directory"));
		
		try {
			log4j.info("Starting database module");
			dbManager = new DbManager(directory);
			sc.setAttribute("dbManager", dbManager);
			log4j.info("Database module has been loaded succesfully");
		}catch(DbException ex) {
			log4j.fatal("Failed while loading database module.", ex);
			exit();
		}
		
		try{
			log4j.info("Starting language module");
			langManager = new LangManager(directory);
			sc.setAttribute("langManager", langManager);
			log4j.info("language library has loaded succesfully");
		}catch(LangException ex){
			log4j.fatal("Failed while loading language module", ex);
			exit();
		}

		try{
			log4j.info("Starting multimedia module");
			mediaManager = new MediaManager(directory);
			sc.setAttribute("mediaManager", mediaManager);
			log4j.info("Multimedia module has loaded succesfully");
		}catch(MultimediaException ex){
			log4j.fatal("Failed to load multimedia module");
			exit();
		}
			
		try{
			log4j.info("Starting wiki module");
			notesManager = new NotesManager(directory);
			sc.setAttribute("notesManager", notesManager);
			log4j.info("Wiki module has loaded succesfully");
		}catch(NotesException ex){
			log4j.fatal("Failed to load wiki module",ex);
			exit();
		}
		
		try {
			log4j.info("Starting LDAP module");
			dapManager = new DapManager(directory);
			sc.setAttribute("dapManager", dapManager);
			log4j.info("LDAP module has started succesfully");
		}catch(DapException ex) {
			log4j.fatal("Failed to load LDAP module",ex);
			exit();
		}

		log4j.info("Tomcat context has been initialized succesfully");
	}
	
	public void contextDestroyed(ServletContextEvent arg0){
		log4j.info("Stopping tomcat context");
		
		log4j.info("Stopping DAP module");
		dapManager.stopDapServer();
		log4j.info("DAP module has stopped succesfully");
		
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
		
		log4j.info("Tomcat context has stopped succesfully");
	}
	
	private void exit() {
		log4j.info("Stopping tomcat");
		
		try {
			Service daemon = new Service(directory);
			daemon.shutdown();
		}catch(TomcatException ex) {
			log4j.fatal("Tomcat was not able to shutdown", ex);
			System.exit(-1);
		}
	}
}
