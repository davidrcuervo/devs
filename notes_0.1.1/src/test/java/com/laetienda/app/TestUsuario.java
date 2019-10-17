package com.laetienda.app;

import java.io.File;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.dap.Dap;
import com.laetienda.dap.DapManager;
//import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.entities.Option;
import com.laetienda.entities.User;

public class TestUsuario {
	private final static Logger log = LogManager.getFormatterLogger(TestUsuario.class);
	
	DapManager dapManager = null;
	DbManager dbManager = null;
	EntityManager em = null;
	
	public TestUsuario() throws AppException {
		
		File directory = new File("C:\\Users\\i849921\\git\\devs\\web\\target\\classes");
		dbManager = new DbManager(new File(directory.getAbsolutePath())).open();
		dapManager = new DapManager(new File(directory.getAbsolutePath()));
	}
	
	public void testSave() {
		log.info("Testing save user");
		
		Usuario usuario;
		
		User testUser = new User();
		testUser.setCn("First Name");
		testUser.setSn("Last Name");
		testUser.setPassword("p4ssw0rd", "p4ssw0rd");
		
		try {

			em = dbManager.createTransaction().getEm();
			Option userStatus = em.createNamedQuery("Option.findByOptionAndVariable", Option.class).setParameter("variable", "user status").setParameter("option", "registered").getSingleResult();
			Option english = em.createNamedQuery("Option.findByOptionAndVariable", Option.class).setParameter("variable", "languages").setParameter("option", "en").getSingleResult();
			testUser.setUid("test", em);
			testUser.setEmail("email@domain.com", em);
			testUser.setStatus(userStatus);
			testUser.setLanguage(english);
			log.debug("$userStatus: " + userStatus.getName());
			
			usuario = new Usuario(dbManager, dapManager);
			usuario.save(testUser);
			log.debug("User has been saved succesfully");
		} catch (AppException e) {
			log.error(e.getMessage(), e.getParent());			
		}finally {
			dbManager.closeEm(em);
			dbManager.close();	
		}
		
	}
	
	public void testFindUser() throws AppException {
		Usuario usuario = new Usuario(dbManager, dapManager);
		EntityManager em = dbManager.createTransaction().getEm();
		Dap dap = dapManager.createDap();		
		User user = usuario.getUser("test", "p4ssw0rd", em, dap);
		log.debug(user.getCn());
		log.debug(user.getSn());
		log.debug(user.getUid());
		log.debug(user.getEmail());
	}

	public static void main(String[] args) {
		
		TestUsuario test;
		try {
			test = new TestUsuario();
			test.testSave();
//			test.testFindUser();
		} catch (AppException e) {
			log.error(e.getMessage(), e.getParent());
		}
		log.info("closing application");
	}
}
