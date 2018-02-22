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

		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		db.insert(sysadmins);
		
		Group managers = new Group("managers", "Group contains manager users");
		db.insert(managers);
		
		Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
		db.insert(empty);
		
		User sysadmin = new User(1, "sysadmin@la-etienda.com");
		db.insert(sysadmin);
		
		User tomcat = new User(2, "web@la-etienda.com");
		db.insert(tomcat);
		
		User owner = new User(3, "owner@mail.com");
		db.insert(owner);
		
		User groupUser = new User(4, "group@mail.com");
		db.insert(groupUser);
		
		User allUser = new User(5, "all@mail.com");
		db.insert(allUser);
		
		User manager = new User(6, "manager@mail.com");
		db.insert(manager);
		
		AccessList acl = new AccessList();
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");
		db.insert(acl);
		
		AccessList aclOwner = new AccessList("owner", "it includes only the owner of the object");
		aclOwner.addGroup(empty);
		aclOwner.addUser(owner);
		db.insert(aclOwner);
		
		AccessList aclGroup = new AccessList("group", "it includes all the members of the object group");
		aclGroup.addGroup(empty);
		aclGroup.addUser(groupUser);
		db.insert(aclGroup);
		
		AccessList aclAll = new AccessList("all", "it includes all the users");
		aclAll.addGroup(empty);
		aclAll.addUser(allUser);
		db.insert(aclAll);
		
		sysadmins.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		managers.setOwner(manager, managers).setPermisions(acl, aclGroup, aclGroup);
		empty.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		sysadmin.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		tomcat.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		owner.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		manager.setOwner(manager, managers).setPermisions(acl, aclOwner, aclGroup);
		groupUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		allUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		acl.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclOwner.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclGroup.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclAll.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
	
		db.update();	
		dbManager.closeTransaction(db);
	}
	
	public static void main(String[] args){
		
		File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		//File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			log4j.info("DATABASE IS BEING INSTALLED");
			Installer installer = new Installer(directory);
			installer.run();
			log4j.info("Database has installed succesfully");
		} catch (DbException ex) {
			log4j.error("Failed to install database.",	ex.getRootParent());
		}
	}
}
