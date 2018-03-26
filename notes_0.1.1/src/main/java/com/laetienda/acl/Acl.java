package com.laetienda.acl;

import java.util.ArrayList;
import java.util.List;

//import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.laetienda.db.DbManager;
import com.laetienda.db.Db;
import com.laetienda.entities.AccessList;
import com.laetienda.entities.Group;
//import com.laetienda.entities.AclGroup;
//import com.laetienda.entities.AclUser;
import com.laetienda.entities.Objeto;
import com.laetienda.entities.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Acl {
	private static final Logger log = LogManager.getLogger(Acl.class);
	
	private User user;
	private DbManager dbManager;
	
	public Acl(DbManager dbManager, User user) {
		this.setUser(user);
		this.dbManager = dbManager;
	}
	
	public boolean canRead(Objeto objeto) {
		return hasPermission(objeto.getRead());
	}
	
	public boolean canWrite(Objeto objeto) {
		return hasPermission(objeto.getWrite());
	}
	
	public boolean canDelete(Objeto objeto) {
		return hasPermission(objeto.getDelete());
	}
	
	public boolean isPartOf(String aclName) {
		log.debug("Validating if user has access to $aclName: " + aclName);
		Db db = dbManager.createTransaction();
		AccessList temp = null;
		
		try {
			temp = db.getEm().createNamedQuery("AccessList.findByName", AccessList.class).setParameter("name", aclName).getSingleResult();
		}catch( NoResultException | NonUniqueResultException e) {
			log.debug("No result found. \"" + aclName + "\" does not exist", e);
		}finally {
			dbManager.closeTransaction(db);
		}
		
		return hasPermission(temp);
	}
	
	public boolean hasPermission(AccessList aList) {
		
		AccessList result = null;
		
		Db db = dbManager.createTransaction();
		
		try {
			//User tempUser = db.getEm().find(User.class, user.getId());
			//log.debug("$tempUser: " + tempUser.getUid());
			//AccessList tempAcl = db.getEm().find(AccessList.class, aList.getId());
			//log.debug("$tempAcl: " + tempAcl.getName());
			result = db.getEm().createNamedQuery("AccessList.findUserInAcl", AccessList.class).setParameter("user", user).setParameter("acl", aList).getSingleResult();
		}catch( NoResultException | NonUniqueResultException e) {
			log.debug("No result find, then user does not have enough privileges", e);
		}finally {
			dbManager.closeTransaction(db);
		}
		
		return result == null ? false : true; 
		
		/*
		List<AclUser> users = aList.getUsers();
		
		for(AclUser user : users) {
			if(user.getUser().getId() == this.user.getId()) {
				result = true;
				break;
			}
		}
		
		if(!result) {
			List<AclGroup> groups = aList.getGroups();
			
			for(AclGroup group : groups) {
				List<User> users2 = group.getGroup().getUsers();
				
				for(User user : users2) {
					if(user.getId() == this.user.getId()) {
						result = true;
						break;
					}
				}
			}
		}
		*/
	}
	
	public List<User> findUsersInAcl(AccessList acl){
		Db db = dbManager.createTransaction();
		List<User> result = db.getEm().createNamedQuery("AccessList.findUsers", User.class).setParameter("acl", acl).getResultList();
		dbManager.closeTransaction(db);
		
		return result != null ? result : new ArrayList<User>();
	}
	
	public List<Group> findGroupsByUser(User u){
		Db db = dbManager.createTransaction();
		List<Group> result = db.getEm().createNamedQuery("Group.findByUser", Group.class).setParameter("user", u).getResultList();
		dbManager.closeTransaction(db);
		
		return result;
	}
	
	public List<AccessList> findAclsByUser(){
		Db db = dbManager.createTransaction();
		List<AccessList> result = db.getEm().createNamedQuery("AccessList.findAclsByUser", AccessList.class).setParameter("user", user).getResultList();
		dbManager.closeTransaction(db);
		
		return result;
	}


	public User getUser() {
		return user;
	}



	public Acl setUser(User user) {
		this.user = user;
		return this;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
