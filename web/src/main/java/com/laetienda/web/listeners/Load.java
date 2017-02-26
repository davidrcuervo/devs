package com.laetienda.web.listeners;

import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.persistence.EntityManager;

import com.laetienda.tomcat.lib.Service;
import com.laetienda.log.bin.JavaLogger;
import com.laetienda.db.Connection;
import com.laetienda.db.Transaction;
import com.laetienda.web.entities.Tabla;

public class Load implements ServletContextListener {
	
	private File directory;
	private JavaLogger log;
	private Connection db;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0){
		log.info("Closing webapp");
		
		db.close();
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		try{
			ServletContext sc = arg0.getServletContext();
			directory = new File(sc.getInitParameter("directory"));

			log = new JavaLogger(directory);
			db = new Connection(directory);
			
			
			Transaction tran = new Transaction(db.getEm());
			
			Tabla tabla = new Tabla();
			tabla.setName("test");
			tabla.setDescription("test description");
						
			try{
				tran.begin(tran.getEm());
				tran.getEm().persist(tabla);
				tran.save(tran.getEm());
			}catch (Exception ex){
				log.error("Error while saving into db");
				ex.printStackTrace();
			}finally{
				tran.getEm().clear();
			}
			
			log.debug("Framework has loaded succesfully");
			
		}catch (Exception ex){
			
			ex.printStackTrace();
			try{
				Service daemon = new Service(directory);
				daemon.shutdown();
			}catch(Exception ex1){
				
				ex1.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
