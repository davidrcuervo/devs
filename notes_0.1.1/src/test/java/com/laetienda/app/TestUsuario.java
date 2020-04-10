package com.laetienda.app;

import java.io.File;

import javax.persistence.EntityManager;

import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.dap.DapManager;
import com.laetienda.db.DbManager;
import com.laetienda.db.Dbase;
import com.laetienda.entities.Option;
import com.laetienda.entities.User;

public class TestUsuario {
	private final static Logger log = LogManager.getFormatterLogger(TestUsuario.class);
	
	DapManager dapManager = null;
	DbManager dbManager = null;
	
	public TestUsuario() throws AppException {
		
		File directory = new File("C:\\Users\\i849921\\git\\devs\\web\\target\\classes");
		dbManager = new DbManager(new File(directory.getAbsolutePath())).open();
		dapManager = new DapManager(new File(directory.getAbsolutePath()));
	}
	
	public void testSave() throws AppException {
		log.info("Testing save user");
			
		LdapConnection conn = null;
		EntityManager em = null;
		Usuario usuario = new Usuario();
		
		try {
			em = dbManager.createTransaction().getEm();
			conn = dapManager.createLdap();
			
			Option userStatus = em.createNamedQuery("Option.findByOptionAndVariable", Option.class).setParameter("variable", "user status").setParameter("option", "registered").getSingleResult();
			Option english = em.createNamedQuery("Option.findByOptionAndVariable", Option.class).setParameter("variable", "languages").setParameter("option", "en").getSingleResult();
			User testUser = new User("test", "First Name", "Last Name", "testuser@mail.com", userStatus, english, conn, em);
			testUser.setPassword("p4ssw0rd", "p4ssw0rd");
			log.debug("$userStatus: " + userStatus.getName());
			
			usuario.save(testUser, em, conn);
			log.debug("User has been saved succesfully");
		} catch (AppException e) {
			log.error(e.getMessage(), e.getParent());			
		}finally {
			dapManager.closeConnection(conn);
			dbManager.closeEm(em);
			dbManager.close();
			
		}
		
	}
	
	public void testFindUser() throws AppException {
		
		Usuario usuario = new Usuario();
		EntityManager em = dbManager.createTransaction().getEm();
		
		LdapConnection conn = null;

		try{
			conn = dapManager.createLdap();	
			User user = usuario.getUser("tomcat", em, conn);
			log.debug(user.getCn());
			log.debug(user.getSn());
			log.debug(user.getUid());
			log.debug(user.getEmail());
		}catch(AppException e) {
			log.error("Failed to find user. $error: {}", e.getMessage());
			throw e;
		}finally {
			dbManager.closeEm(em);
			dapManager.closeConnection(conn);
		}
	}
	
	public void testModifyUser() throws AppException {
		
		LdapConnection conn = null;
		Dbase dbase = new Dbase();
		Usuario usuario = new Usuario();
		EntityManager em = dbManager.createTransaction().getEm();
		
		try {
			conn = dapManager.createLdap();
			User user = usuario.getUser("test", em, conn);
			dbase.begin(em);
			user.setEmail("testuser2@mail.com", conn);
			usuario.update(user, em, conn);
		} catch (AppException e) {
			log.error("Failed to find users. $error: {}", e.getMessage());
			throw e;
		}finally {
			dapManager.closeConnection(conn);
		}
	}

	public static void main(String[] args) {
		
		TestUsuario test;
		try {
			test = new TestUsuario();
			test.testSave();
//			test.testFindUser();
			test.testModifyUser();
		} catch (AppException e) {
			log.error(e.getMessage(), e);
		}
		log.info("closing application");
	}
}
