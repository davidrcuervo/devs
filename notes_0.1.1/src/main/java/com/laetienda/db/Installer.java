package com.laetienda.db;

import java.io.File;
import javax.persistence.EntityManager;
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
	
	private void run() {
		Db db = dbManager.createTransaction();
		EntityManager em = db.getEm();
		
		Group sysadmins = new Group();
		Objeto objSysadmins = new Objeto();
		
		User sysadmin = new User();
		Objeto objSysadmin = new Objeto();
		
		AccessList acl = new AccessList();
		Objeto objAcl = new Objeto();
		
		sysadmins.setObjeto(objSysadmins);
		objSysadmins.setOwner(sysadmin);
		objSysadmins.setGroup(sysadmins);
		objSysadmins.setPermisions(acl, acl, acl);
		sysadmin.setObjeto(objSysadmin);
		objSysadmin.setOwner(sysadmin);
		objSysadmin.setGroup(sysadmins);
		objSysadmin.setPermisions(acl, acl, acl);
		acl.setObjeto(objAcl);
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");
		objAcl.setOwner(sysadmin);
		objAcl.setPermisions(acl, acl, acl);
		objAcl.setGroup(sysadmins);
		
		em.getTransaction().begin();
		em.persist(objSysadmins);
		em.persist(objSysadmin);
		em.persist(objAcl);
		em.persist(sysadmins);
		em.persist(sysadmin);
		em.persist(acl);
		em.getTransaction().commit();
		dbManager.closeTransaction(db);
	}
	
	public static void main(String[] args){
		
		File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		//File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			Installer installer = new Installer(directory);
			installer.run();
		} catch (DbException ex) {
			log4j.error("Failed to install database.", ex);
		}
	}
}
