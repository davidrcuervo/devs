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
		Objeto objManagers = new Objeto();
		Objeto objEmptyGroup = new Objeto();
		Objeto objSysadmin = new Objeto();
		Objeto objTomcat = new Objeto();
		Objeto objOwnerUSer = new Objeto();
		Objeto objGroupUser = new Objeto();
		Objeto objAllUser = new Objeto();
		Objeto objAdmUser = new Objeto();
		Objeto objAcl = new Objeto();
		Objeto objAclUser = new Objeto();
		Objeto objAclGroup = new Objeto();
		Objeto objAclAll = new Objeto();
		db.insert(objSysadmins);
		db.insert(objManagers);
		db.insert(objEmptyGroup);
		db.insert(objSysadmin);
		db.insert(objTomcat);
		db.insert(objOwnerUSer);
		db.insert(objGroupUser);
		db.insert(objAllUser);
		db.insert(objAdmUser);
		db.insert(objAcl);
		db.insert(objAclUser);
		db.insert(objAclGroup);
		db.insert(objAclAll);
		
		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		sysadmins.setObjeto(objSysadmins);
		db.insert(sysadmins);
		
		Group managers = new Group("managers", "Group contains manager users");
		managers.setObjeto(objManagers);
		db.insert(managers);
		
		Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
		empty.setObjeto(objEmptyGroup);
		db.insert(empty);
		
		User sysadmin = new User(1, "sysadmin@la-etienda.com");
		sysadmin.setObjeto(objSysadmin);
		db.insert(sysadmin);
		
		User tomcat = new User(2, "web@la-etienda.com");
		tomcat.setObjeto(objTomcat);
		db.insert(tomcat);
		
		User owner = new User(3, "user@mail.com");
		owner.setObjeto(objOwnerUSer);
		db.insert(owner);
		
		User groupUser = new User(4, "group@mail.com");
		groupUser.setObjeto(objGroupUser);
		db.insert(groupUser);
		
		User allUser = new User(5, "all@mail.com");
		allUser.setObjeto(objAllUser);
		db.insert(allUser);
		
		AccessList acl = new AccessList();
		acl.setObjeto(objAcl);
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");
		db.insert(acl);
		
		AccessList aclOwner = new AccessList("owner", "it includes only the owner of the object");
		aclOwner.setObjeto(objAclUser);
		aclOwner.addGroup(empty);
		aclOwner.addUser(owner);
		db.insert(aclOwner);
		
		AccessList aclGroup = new AccessList("group", "it includes all the members of the object group");
		aclGroup.setObjeto(objAclGroup);
		aclGroup.addGroup(empty);
		aclGroup.addUser(groupUser);
		db.insert(aclGroup);
		
		AccessList aclAll = new AccessList("all", "it includes all the users");
		aclAll.setObjeto(objAclAll);
		aclAll.addGroup(empty);
		aclAll.addUser(allUser);
		db.insert(aclAll);
		
		objSysadmins.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objSysadmin.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objTomcat.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objOwnerUSer.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objGroupUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objAllUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objAcl.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objAclUser.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objAclGroup.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		objAclAll.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, acl);
		
		db.update();
		
		dbManager.closeTransaction(db);
	}
	
	public static void main(String[] args){
		
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
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
