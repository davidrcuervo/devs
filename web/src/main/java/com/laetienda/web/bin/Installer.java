package com.laetienda.web.bin;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.app.AppException;
//import com.laetienda.dap.DapException;
//import com.laetienda.db.DbException;
import com.laetienda.install.Instalador;

public class Installer {
	
	final static File DIRECTORY = new File(Installer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	
	static {
		System.setProperty("logFilePath", new File(DIRECTORY, "var" + File.separator + "log").getAbsolutePath());
	}

	public static void main(String[] args) {
		
		Logger log = LogManager.getLogger(Installer.class);
		
		Instalador installer = new Instalador(new File(DIRECTORY.getAbsolutePath()));
	
		log.info("Installing Apache Active directory");
		try{
			installer.parseCommand(args);
			installer.dap();
			log.info("Apache Active directory has been installed succesfully");
			/*
			log.info("Installing database structure");
			com.laetienda.db.Installer dbInstaler = new com.laetienda.db.Installer(new File(DIRECTORY.getAbsolutePath()));
			dbInstaler.run();
			log.info("Database structure has been installed succesfully");
			*/
		}catch(AppException ex){
			log.error(ex.getMessage(), ex);	
		}
	}
}
