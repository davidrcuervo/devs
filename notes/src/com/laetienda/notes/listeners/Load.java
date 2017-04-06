package com.laetienda.notes.listeners;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.notes.lib.Notes;
import com.laetienda.tomcat.lib.Service;

public class Load implements ServletContextListener {
	
	private Notes notes;
	private String directory;
	
	public void contextDestroyed(ServletContextEvent arg0){
		
	}
	
	public void contextInitialized(ServletContextEvent arg0){
		
		try{
			ServletContext sc = arg0.getServletContext();
			directory = sc.getInitParameter("directory");
			notes = new Notes();
			notes.setDirectory(directory);
			sc.setAttribute("notes", notes);
			
		}catch(Exception ex){
			
			System.err.println("Error while starting notes application");
			System.err.println("Closing application...");
	
			try{	
				Service daemon = new Service(new File(directory));
				daemon.shutdown();
			}catch(Exception ex1){
				System.err.println(ex.getMessage());
				System.err.println(ex1.getClass().getName());
				System.exit(-1);
			}
		}
	}
}
