package com.laetienda.app;

import com.laetienda.entities.User;
import com.laetienda.db.DbException;
import com.laetienda.db.Dbase;
import com.laetienda.app.AppException;
import com.laetienda.app.GeneralException;
import com.laetienda.dap.DapException;
import com.laetienda.dap.Ldap;
import com.laetienda.dap.Dap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Usuario {
	private final static Logger log = LogManager.getLogger(Usuario.class);
	
	public Usuario() {
		
	}
	
	public User getUser(String username, String password, EntityManager em, Dap dap) throws GeneralException {
		User result = null;
		
		try {
			Entry dapUser = dap.getUser(username, password);
			result = em.createNamedQuery("User.findByUid", User.class).setParameter("uid", username).getSingleResult();			
			result.setCn(dapUser.get("cn").getString());
			result.setSn(dapUser.get("sn").getString());
			
		}catch (PersistenceException | DapException | LdapException ex) {
			log.error("Failed to find user. $Exception: " + ex.getMessage());
			throw new GeneralException(ex.getMessage(), ex);
		}
		
		return result;
	}
	
	public Usuario save(User user, EntityManager em, LdapConnection conn) throws AppException {
		
		if(user.getErrors().size() > 0) {
			throw new GeneralException("User has errors and can't be persisted");
		}
		
		Dbase dbase = new Dbase();
		Ldap ldap = new Ldap();
		
		try {
			dbase.insert(em, user);
			ldap.insertUser(user, conn);
		}catch (DbException e) {
			user.addError("user", "Internal error. Failed to add user");
			log.error("Failed to persist user. $excpetion: " + e.getMessage());
			throw e;
		}catch(DapException e) {
			user.addError("user", "Internal error. Failed to add user");
			log.error("Failed to save user in LDAP. $error: {}", e.getMessage());
			
			try {
				dbase.delete(user, em);
				throw e;
			}catch(DbException ex) {
				log.fatal("Failed to remove user from DB that was not able to be saved in ldap directory. $exception: " + e.getMessage());
				throw ex;
			}
		}
		
		return this;
	}
}
