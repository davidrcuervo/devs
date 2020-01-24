package com.laetienda.db;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.Logger;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;

import com.laetienda.app.AppException;
import com.laetienda.app.Usuario;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.entities.*;


public class Installer {
	static final Logger log4j = LogManager.getLogger(Installer.class);
	
	public Installer() {
		
	}
	
	public void run(LdapConnection conn, EntityManager em, String tomcatPass) throws AppException {

		Dbase dbase = new Dbase();
		log4j.info("Database should be created at this point, now it will add application rows");
		
		
		try {
			
			em.getTransaction().begin();
			
			List<Variable> vars = em.createNamedQuery("Variable.findall", Variable.class).getResultList();
			if(vars.size() > 0) {
				throw new DbException("Variables table is not empty");
			}
			
		
			List<Option> opts = em.createNamedQuery("Option.findall", Option.class).getResultList();
			if(opts.size() > 0) {
				throw new DbException("Options table is not empty");
			}
			
			List<Group> gps = em.createNamedQuery("Option.findall", Group.class).getResultList();
			if(gps.size() > 0) {
				throw new DbException("Groups table is not empty");
			}
			
			List<User> users = em.createNamedQuery("Option.findall", User.class).getResultList();
			if(users.size() > 0) {
				throw new DbException("Users table is not empty");
			}
		
			//TODO create test for other tables: Access_control_list, forms, inputs, objects
			
			//Start installing components 	
			Variable userStatus = new Variable("user status", "Different options of status of the user withing the website");
			Option userActiveStatus = new Option("active", "The user has completed the activation process");
			userStatus.addOption(userActiveStatus);
			Option status = new Option("operative system", "This user belongs to the operative system of the website");
			userStatus.addOption(status);
			userStatus.addOption("blocked", "User has been blocked to use the application");
			userStatus.addOption("deleted", "User has removed himself from the website");
			userStatus.addOption("registered", "User has been registered but password has not been confirmed");
			em.persist(userActiveStatus);
			em.persist(status);
			em.persist(userStatus);
		
			Variable languages = new Variable("languages", "Languages availables in the system");
			Option language = new Option("en", "English");
			languages.addOption(language);
			languages.addOption("none", "Select a language");
			languages.addOption("es", "Espanol");
			languages.addOption("fr", "Francais");
			em.persist(languages);

			Group sysadmins = new Group("sysadmins", "This group contains all the sysadmin users.");
			em.persist(sysadmins);
			Group managers = new Group("managers", "Group contains manager users");	
			em.persist(managers);
			Group empty = new Group("empty", "This group will not have any users. if it does is a bug");
			em.persist(empty);

			dbase.commit(em);	
			Usuario usuario = new Usuario();

			User sysadmin = new User("sysadmin", "SysAdmin", "SnLess", "sysadmin@la-etienda.com", status, language, conn, em);
			usuario.save(sysadmin, em, conn);
			User tomcat = new User("tomcat", "Tomcat", "SnLess", "tomcat@la-etienda.com", status, language, conn, em);
			tomcat.setPassword(tomcatPass, tomcatPass);
			usuario.save(tomcat, em, conn);
			User owner = new User("owner", "Owner", "SnLess","owner@mail.com", status, language, conn, em);
			usuario.save(owner, em, conn);
			User groupUser = new User("group", "Group", "SnLess", "group@mail.com", status, language, conn, em);
			usuario.save(groupUser, em, conn);
			User allUser = new User("allusers", "AllUsers", "SnLess", "todos@mail.com", status, language, conn, em);
			usuario.save(allUser, em, conn);
			
			User manager = new User("manager", "Manager", "SnLess", "manager@mail.com", userActiveStatus, language, conn, em);
			//TODO cipher password
			manager.setPassword("Welcome@1", "Welcome@1");
			usuario.save(manager, em, conn);
			
			AccessList acl = new AccessList();
			acl.addGroup(sysadmins);
			acl.addUser(sysadmin);
			acl.setName("sysadmins");
			acl.setDescription("Includes sysadmin user and sysadmin group");
			em.persist(acl);

			AccessList aclManagers = new AccessList("managers", "It includes the managers of the site");
			aclManagers.addUser(manager);
			aclManagers.addGroup(managers);
			em.persist(aclManagers);
			
			AccessList aclOwner = new AccessList("owner", "it includes only the owner of the object");
			aclOwner.addGroup(empty);
			aclOwner.addUser(owner);
			em.persist(aclOwner);
	
			AccessList aclGroup = new AccessList("group", "it includes all the members of the object group");
			aclGroup.addGroup(empty);
			aclGroup.addUser(groupUser);
			em.persist(aclGroup);
	
			AccessList aclAll = new AccessList("all", "it includes all the users");
			aclAll.addGroup(empty);
			aclAll.addUser(allUser);
			em.persist(aclAll);
			
			Form form = new Form("group", "com.laetienda.entities.Group", "/WEB-INF/jsp/email/signup.jsp", "/WEB-INF/jsp/thankyou/signup.jsp", aclManagers);
			form.addInput(new Input(form, "name", "Group Name", "string", "Insert the group name", "glyphicon-user", true));
			form.addInput(new Input(form, "description", "Description", "string", true));
			em.persist(form);
				
			userStatus.setOwner(sysadmin, sysadmins).setPermisions(acl, acl, aclAll);
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
			
			dbase.commit(em);
		
		}catch(DapException | DbException e) {
			dbase.rollback(em);
			throw e;
		}catch(IllegalArgumentException e) {
			dbase.rollback(em);
			throw new DbException(e);
		}
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
			installer.run(conn,db.getEm(), dapManager.getSetting("tomcatpassword"));
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
