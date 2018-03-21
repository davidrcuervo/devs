package com.laetienda.db;

import java.io.File;
import org.apache.logging.log4j.Logger;
import org.python.jline.internal.Log;
import org.apache.logging.log4j.LogManager;

import com.laetienda.dap.Dap;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.entities.*;

public class Installer {
	static final Logger log4j = LogManager.getLogger(Installer.class);
	
	DbManager dbManager;
	DapManager dapManager;
	File directory;
	
	private Installer(File directory) throws DbException, DapException {
		dbManager = new DbManager(directory);
		dapManager = new DapManager(directory); 
		this.directory = directory;
	}
	
	private void run() throws DbException, DapException {
		Db db = dbManager.createTransaction();
		
		
		Variable userStatus = new Variable("user status", "Different options of status of the user withing the website");
		userStatus.addOption("active", "User is active. It has been registered and password has been confirmed");
		userStatus.addOption("blocked", "User has been blocked to use the application");
		userStatus.addOption("deleted", "User has removed himself from the website");
		userStatus.addOption("registered", "User has been registered but password has not been confirmed");
		userStatus.addOption("operative system", "This user belongs to the operative system of the website");
		db.insert(userStatus);
		
		/*
		Variable userUid = new Variable("user uid", "This variable keeps the counting of uid. Starting at 101");
		userUid.addOption("uid", "101");
		db.insert(userUid);
		*/
		
		Variable languages = new Variable("languages", "Languages availables in the system");
		languages.addOption("none", "Select a language");
		languages.addOption("en", "English");
		languages.addOption("es", "Español");
		languages.addOption("fr", "Francais");
		db.insert(languages);

		Option status = db.findOption("user status", "operative system");
		Option userActiveStatus = db.findOption("user status", "active");
		Option language = db.findOption("languages", "en");
		
		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		db.insert(sysadmins);
		
		Group managers = new Group("managers", "Group contains manager users");
		db.insert(managers);
		
		Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
		db.insert(empty);
		
		User sysadmin = new User("sysadmin", "sysadmin@la-etienda.com", status, language, db);
		db.insert(sysadmin);
		
		User tomcat = new User("tomcat", "tomcat@la-etienda.com", status, language, db);
		db.insert(tomcat);
		
		User owner = new User("owner", "owner@mail.com", status, language, db);
		db.insert(owner);
		
		User groupUser = new User("group", "group@mail.com", status, language, db);
		db.insert(groupUser);
		
		User allUser = new User("all", "todos@mail.com", status, language, db);
		db.insert(allUser);
		
		User manager = new User("manager", "manager@mail.com", userActiveStatus, language, db);
		manager.setPassword("Welcome@1", "Welcome@1");
		manager.setCn("Manager");
		manager.setSn("Snless"); 
		db.insert(manager);
		
		
		
		AccessList acl = new AccessList();
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");
		db.insert(acl);
		
		AccessList aclManagers = new AccessList("managers", "It includes the managers of the site");
		aclManagers.addUser(manager);
		aclManagers.addGroup(managers);
		db.insert(aclManagers);
		
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
		
		Form form = new Form("group", "com.laetienda.entities.Group", "/WEB-INF/jsp/email/signup.jsp", "/WEB-INF/jsp/thankyou/signup.jsp", aclManagers);
		form.addInput(new Input(form, "name", "Group Name", "string", "Insert the group name", "glyphicon-user", true));
		form.addInput(new Input(form, "description", "Description", "string", true));
		db.insert(form);
		
		userStatus.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, aclAll);
		//userUid.setOwner(tomcat, empty).setPermisions(acl, aclOwner, aclAll);
		languages.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, aclAll);
		sysadmins.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		managers.addUser(manager).setOwner(manager, managers).setPermisions(acl, aclGroup, aclGroup);
		empty.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		sysadmin.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		tomcat.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		owner.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		manager.setOwner(manager, managers).setPermisions(acl, aclOwner, aclGroup);
		groupUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		allUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		acl.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclManagers.setOwner(manager, managers).setPermisions(acl, acl, acl);
		aclOwner.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclGroup.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		aclAll.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		form.setOwner(manager, managers).setPermisions(aclGroup, aclGroup, aclGroup);
		
		db.update();	
		
		
		Dap dap = dapManager.createDap();
		
		try {
			dap.insertUser(manager);
		}catch(DapException ex) {
			Log.error("Error while adding \"Owner\" user to LDAP", ex.getRootParent());
			//db.remove(manager);
		}finally {
			dapManager.closeConnection(dap);
			dbManager.closeTransaction(db);
			dbManager.close();
		}
	}
	
	public static void main(String[] args){
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			log4j.info("DATABASE IS BEING INSTALLED");
			Installer installer = new Installer(directory);
			installer.run();
			log4j.info("Database has installed succesfully");
		} catch (DbException | DapException ex) {
			log4j.error("Failed to install database.",	ex.getRootParent());
		}
	}
}
