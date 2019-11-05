package com.laetienda.web.bin;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.app.AppException;
//import com.laetienda.dap.DapException;
//import com.laetienda.db.DbException;
import com.laetienda.install.Instalador;

public class Installer {
	
	 
	private final static File DIRECTORY = new File(Installer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	static {
		System.setProperty("logFilePath", new File(DIRECTORY, "var" + File.separator + "log").getAbsolutePath());
	}
	private final static Logger log = LogManager.getLogger(Installer.class);
	
	public static void main(String[] args) {
		
		log.debug("Application Root Directory is: {}", DIRECTORY.getAbsolutePath());
		Instalador installer = new Instalador(new File(DIRECTORY.getAbsolutePath()));
	
		try{
			installer.parseCommand(args); 
			installer.dap();
			installer.database();			
		}catch(AppException ex){
			log.error(ex.getMessage(), ex);	
		}
	}
}
