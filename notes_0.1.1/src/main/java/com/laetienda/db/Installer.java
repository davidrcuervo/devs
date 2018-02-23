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
		
		Variable userStatus = new Variable("user status", "Different options of status of the user withing the website");
		userStatus.addOption("active", "User is active. It has been registered and password has been confirmed");
		userStatus.addOption("blocked", "User has been blocked to use the application");
		userStatus.addOption("deleted", "User has removed himself from the website");
		userStatus.addOption("registered", "User has been registered but password has not been confirmed");
		userStatus.addOption("operative system", "This user belongs to the operative system of the website");
		db.insert(userStatus);
		
		Variable userUid = new Variable("user uid", "This variable keeps the counting of uid. Starting at 101");
		userUid.addOption("uid", "101");
		db.insert(userUid);
		
		Variable languages = new Variable("languages", "Languages availables in the system");
		languages.addOption("none", "Select a language");
		languages.addOption("en", "English");
		languages.addOption("es", "Español");
		languages.addOption("fr", "Francais");
		db.insert(languages);

		Option status = db.findOption("user status", "operative system");
		Option language = db.findOption("languages", "en");
		
		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		db.insert(sysadmins);
		
		Group managers = new Group("managers", "Group contains manager users");
		db.insert(managers);
		
		Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
		db.insert(empty);
		
		User sysadmin = new User(1, "sysadmin@la-etienda.com");
		sysadmin.setLanguage(language);
		sysadmin.setStatus(status);
		db.insert(sysadmin);
		
		User tomcat = new User(2, "web@la-etienda.com");
		tomcat.setLanguage(language);
		tomcat.setStatus(status);
		db.insert(tomcat);
		
		User owner = new User(3, "owner@mail.com");
		owner.setLanguage(language);
		owner.setStatus(status);
		db.insert(owner);
		
		User groupUser = new User(4, "group@mail.com");
		groupUser.setLanguage(language);
		groupUser.setStatus(status);
		db.insert(groupUser);
		
		User allUser = new User(5, "all@mail.com");
		allUser.setLanguage(language);
		allUser.setStatus(status);
		db.insert(allUser);
		
		User manager = new User(6, "manager@mail.com");
		manager.setLanguage(language);
		manager.setStatus(status);
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
		
		userStatus.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, aclAll);
		userUid.setOwner(tomcat, empty).setPermisions(acl, aclOwner, aclAll);
		languages.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, aclAll);
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
