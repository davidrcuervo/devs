package com.laetienda.app;

import com.laetienda.entities.User;
import com.laetienda.db.Db;
import com.laetienda.db.DbException;
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
	
//	User user;
//	User tomcat;
//	DbManager dbManager;
//	DapManager dapManager;
//	Dn baseDn;
	
	public Usuario() {
		
	}
	
//	public Usuario(DbManager dbManager, DapManager dapManager) throws AppException {	
//		try {
//			setUser(user);
//			setDbManager(dbManager);
//			setDapManager(dapManager);
//			this.tomcat = dapManager.getTomcat();
//			this.baseDn = new Dn(Ldif.getDomain());
//		} catch (LdapInvalidDnException e) {
//			log.error("Failed to create Usuario object. $exception: " + e.getMessage());
//			throw new DapException(e);
//		}
//	}
	
//	public Usuario setUser(User user) {
//		this.user = user;
//		return this;
//	}
	
//	public Usuario setDbManager(DbManager dbManager) {
//		this.dbManager = dbManager;
//		return this;
//	}

//	public Usuario setDapManager(DapManager dapManager) {
//		this.dapManager = dapManager;
//		return this;
//	}
	
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
	
	public Usuario save(User user, Db db, LdapConnection conn) throws AppException {
		if(user.getErrors().size() > 0) {
			throw new GeneralException("User has errors and can't be persisted");
		}
		
//		Db db = dbManager.createTransaction();
//		Dap dap = null;
		Ldap ldap = new Ldap();
		
		try {
			db.insert(user);
//			dap = dapManager.createDap();
//			dap.insertUser(user);
			ldap.insertUser(user, conn);
		}catch (DbException e) {
			user.addError("user", "Internal error. Failed to add user");
			log.error("Failed to persist user. $excpetion: " + e.getMessage());
			throw e;
		}catch(DapException e) {
			user.addError("user", "Internal error. Failed to add user");
			
			try {
				db.remove(user);
				throw e;
			}catch(DbException ex) {
				log.fatal("Failed to remove user from DB that was not able to be saved in ldap directory. $exception: " + e.getMessage());
				throw ex;
			}
		}finally {
//			dbManager.closeTransaction(db);
//			dapManager.closeConnection(dap);
		}
		return this;
	}
}
