package com.laetienda.db;

import java.io.File;
import org.apache.log4j.Logger;

import com.laetienda.entities.*;

public class Installer {
	static final Logger log4j = Logger.getLogger(Installer.class);
	
	DbManager dbManager;
	File directory;
	
	private Installer(File directory) throws DbException {
		dbManager = new DbManager(directory);
		this.directory = directory;
	}
	
	private void run() throws DbException {
		Db db = dbManager.createTransaction();
				
		Objeto objSysadmins = new Objeto();
		Objeto objSysadmin = new Objeto();
		Objeto objAcl = new Objeto();
		db.insert(objSysadmins);
		db.insert(objSysadmin);
		db.insert(objAcl);
		
		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		sysadmins.setObjeto(objSysadmins);
		db.insert(sysadmins);
		
		User sysadmin = new User(1, "sysadmin@la-etienda.com");
		sysadmin.setObjeto(objSysadmin);
		db.insert(sysadmin);
		
		AccessList acl = new AccessList();
		acl.setObjeto(objAcl);
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");
		db.insert(acl);
				
		objSysadmins.setOwner(sysadmin);
		objSysadmins.setGroup(sysadmins);
		objSysadmins.setPermisions(acl, acl, acl);
		
		objSysadmin.setOwner(sysadmin);
		objSysadmin.setGroup(sysadmins);
		objSysadmin.setPermisions(acl, acl, acl);
		
		objAcl.setOwner(sysadmin);
		objAcl.setPermisions(acl, acl, acl);
		objAcl.setGroup(sysadmins);
		
		db.update();
		
		dbManager.closeTransaction(db);
	}
	
	public static void main(String[] args){
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			Installer installer = new Installer(directory);
			installer.run();
		} catch (DbException ex) {
			log4j.error("Failed to install database.",	ex.getRootParent());
		}
	}
}
