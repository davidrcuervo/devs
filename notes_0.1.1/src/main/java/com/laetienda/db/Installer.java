package com.laetienda.db;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;

import com.laetienda.app.AppException;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.entities.*;


public class Installer {
	static final Logger log4j = LogManager.getLogger(Installer.class);
	
//	DbManager dbManager;
//	DapManager dapManager;
//	File directory;
	
	/*
	public Installer(File directory) throws DbException, DapException {
		dbManager = new DbManager(directory);
		dapManager = new DapManager(directory); 
		this.directory = directory;
	}
	*/
	
	public Installer() {
		
	}
	
	public void run(LdapConnection conn, Db db) throws AppException {
//		dbManager.setCreateDatabaseVariable();
//		dbManager.open();
//		Db db = dbManager.createTransaction();
		log4j.info("Database should be created at this point, now it will add application rows");
		
		List<Variable> vars = db.getEm().createNamedQuery("Variable.findall", Variable.class).getResultList();
		if(vars.size() > 0) {
			throw new DbException("Variables table is not empty");
		}
		
		List<Option> opts = db.getEm().createNamedQuery("Option.findall", Option.class).getResultList();
		if(opts.size() > 0) {
			throw new DbException("Options table is not empty");
		}
		
		List<Group> gps = db.getEm().createNamedQuery("Option.findall", Group.class).getResultList();
		if(gps.size() > 0) {
			throw new DbException("Groups table is not empty");
		}
		
		List<User> users = db.getEm().createNamedQuery("Option.findall", User.class).getResultList();
		if(users.size() > 0) {
			throw new DbException("Users table is not empty");
		}
		
		//TODO create test for other tables: Access_control_list, forms, inputs, objects
		
		
		//Start installing components 
		Option status = new Option("operative system", "This user belongs to the operative system of the website");
		Option userActiveStatus = new Option("user status", "active");
		Option language = new Option("en", "English");
		
		Variable userStatus = new Variable("user status", "Different options of status of the user withing the website");
		userStatus.addOption(userActiveStatus);
		userStatus.addOption("blocked", "User has been blocked to use the application");
		userStatus.addOption("deleted", "User has removed himself from the website");
		userStatus.addOption("registered", "User has been registered but password has not been confirmed");
		userStatus.addOption(status);
		
		/*
		Variable userUid = new Variable("user uid", "This variable keeps the counting of uid. Starting at 101");
		userUid.addOption("uid", "101");
		*/
		
		Variable languages = new Variable("languages", "Languages availables in the system");
		languages.addOption("none", "Select a language");
		languages.addOption(language);
		languages.addOption("es", "Espanol");
		languages.addOption("fr", "Francais");

//		Option status = db.findOption("user status", "operative system");
//		Option userActiveStatus = db.findOption("user status", "active");
//		Option language = db.findOption("languages", "en");
		
		Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
		Group managers = new Group("managers", "Group contains manager users");	
		Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
				
//		SYSADMIN
//		Usuario usuario = new Usuario();
//		User sysadmin = new User("sysadmin", "sysadmin@la-etienda.com", status, language, db);
//		db.insert(sysadmin);
		
//		User tomcat = new User("tomcat", "tomcat@la-etienda.com", status, language, db);
//		db.insert(tomcat);

		User sysadmin = new User("uid=sysadmin", "sysadmin@la-etienda.com", status, language, conn);
		User tomcat = new User("uid=tomcat", "tomcat@la-etienda.com", status, language, conn);
		User owner = new User("uid=owner", "owner@mail.com", status, language, conn);
		User groupUser = new User("uid=group", "group@mail.com", status, language, conn);
		User allUser = new User("uid=all", "todos@mail.com", status, language, conn);

		User manager = new User("uid=manager", "manager@mail.com", userActiveStatus, language, conn);
		manager.setPassword("Welcome@1", "Welcome@1");
		manager.setCn("Manager");
		manager.setSn("Snless"); 
		
		AccessList acl = new AccessList();
		acl.addGroup(sysadmins);
		acl.addUser(sysadmin);
		acl.setName("sysadmins");
		acl.setDescription("Includes sysadmin user and sysadmin group");

		AccessList aclManagers = new AccessList("managers", "It includes the managers of the site");
		aclManagers.addUser(manager);
		aclManagers.addGroup(managers);
		
		AccessList aclOwner = new AccessList("owner", "it includes only the owner of the object");
		aclOwner.addGroup(empty);
		aclOwner.addUser(owner);

		AccessList aclGroup = new AccessList("group", "it includes all the members of the object group");
		aclGroup.addGroup(empty);
		aclGroup.addUser(groupUser);

		AccessList aclAll = new AccessList("all", "it includes all the users");
		aclAll.addGroup(empty);
		aclAll.addUser(allUser);

		Form form = new Form("group", "com.laetienda.entities.Group", "/WEB-INF/jsp/email/signup.jsp", "/WEB-INF/jsp/thankyou/signup.jsp", aclManagers);
		form.addInput(new Input(form, "name", "Group Name", "string", "Insert the group name", "glyphicon-user", true));
		form.addInput(new Input(form, "description", "Description", "string", true));
				
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
		
		/*
		//VARIABLES
		db.insert(userStatus);
		db.insert(languages);
		
		//GROUPS
		db.insert(sysadmins);
		db.insert(managers);
		db.insert(empty);
		
		//ACLs
		db.insert(acl);
		db.insert(aclManagers);
		db.insert(aclOwner);
		db.insert(aclGroup);
		db.insert(aclAll);
		db.insert(form);
		
		//USERS
		usuario.save(sysadmin, db, conn);	
		usuario.save(tomcat, db, conn);
		usuario.save(groupUser, db, conn);		
		usuario.save(allUser, db, conn);
		usuario.save(manager, db, conn);
		*/
//		db.update();	
		
		/*
		Dap dap = dapManager.createDap();
		
		try {
			dap.insertUser(manager);
		}catch(DapException ex) {
			log4j.error("Error while adding \"Owner\" user to LDAP", ex);
			//db.remove(manager);
		}finally {
			dapManager.closeConnection(dap);
			dbManager.closeTransaction(db);
			dbManager.close();
		}
		*/
	}
	
	public static void main(String[] args){
		
//		File directory = new File(Installer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		//File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		log4j.info("DATABASE IS BEING INSTALLED");
		File directory = new File("C:/Users/i849921/git/devs/web/target/classes"); //SAP lenovo
		log4j.debug("Application Directori is {}", directory.getAbsolutePath());
		
		DapManager dapManager = null;
		DbManager dbManager = null;		
		LdapConnection conn = null;
		Db db = null;
		
		try {
			dapManager = new DapManager(directory);
			conn = dapManager.createLdap();
			dbManager = new DbManager(directory);
			dbManager.open();
			db = dbManager.createTransaction();
			
			Installer installer = new Installer();
			installer.run(conn,db);
			log4j.info("Database has installed succesfully");
		} catch (AppException ex) {
			log4j.error(ex.getMessage(), ex.getRootParent());
		} finally {
			dbManager.closeTransaction(db);
			dbManager.close();
			
			try {
				dapManager.closeConnection(conn);
			} catch (DapException e) {
				log4j.fatal(e.getMessage(), e.getRootParent());
			}
		}
	}
}
