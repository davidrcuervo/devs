package com.laetienda.entities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.apache.logging.log4j.Logger;

import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;

import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="access_control_lists")
@NamedQueries({
	@NamedQuery(name="AccessList.findall", query="SELECT acl FROM AccessList acl"),
	@NamedQuery(name="AccessList.findByName", query="SELECT acl FROM AccessList acl WHERE acl.name = :name"),
	@NamedQuery(
			name="AccessList.findUsers", 
			query="SELECT u1 FROM AccessList acl "
					+ "JOIN acl.users aclu "
					+ "JOIN aclu.user u1 "
					+ "WHERE acl = :acl "
					+ "UNION "
					+ "SELECT u2 FROM AccessList acl "
					+ "JOIN acl.groups aclg "
					+ "JOIN aclg.group g "
					+ "JOIN g.users u2 "
					+ "WHERE acl = :acl"
				),
	@NamedQuery(
			name="AccessList.findUserInAcl", 
			query="SELECT acl FROM AccessList acl "
					+ "JOIN acl.groups aclg "
					+ "JOIN aclg.group g "
					+ "JOIN g.users u2 "
					+ "JOIN acl.users aclu  "
					+ "JOIN aclu.user u1 "
					+ "WHERE acl = :acl AND (u1 = :user OR u2 = :user)"
			),
	@NamedQuery(
			name="AccessList.findAclsByUser", 
			query="SELECT acl FROM AccessList acl "
					+ "JOIN acl.groups aclg "
					+ "JOIN aclg.group g "
					+ "JOIN g.users u2 "
					+ "JOIN acl.users aclu "
					+ "JOIN aclu.user u1 "
					+ "WHERE u1 = :user OR u2 = :user"
			)
})

public class AccessList extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(AccessList.class);
	
	@Column(name="\"name\"", unique=true, nullable=false, length=254)
	private String name;
	
	@Column(name="\"description\"", unique=true, nullable=false, length=254)
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="acl", orphanRemoval=true)
	private List<AclUser> users = new ArrayList<AclUser>();
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="acl", orphanRemoval=true)
	private List<AclGroup> groups = new ArrayList<AclGroup>();
	
	public AccessList() {
		
	}
	
	public AccessList(String name, String description) {
		setName(name);
		setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<AclUser> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		AclUser aclUser = new AclUser();
		aclUser.setUser(user);
		aclUser.setAcl(this);
		
		//TODO check if user has already added to the acl
		users.add(aclUser);
	}

	public List<AclGroup> getGroups() {
		return groups;
	}
	
	public void addGroup(Group group) {
		AclGroup aclGroup = new AclGroup();
		aclGroup.setAcl(this);
		aclGroup.setGroup(group);
		
		//TODO check if group had added before to the acl
		groups.add(aclGroup);
	}
	
	public static void main(String[] args) {
		log.info("Hello " + AccessList.class.getName() + "!!!.");
		
		File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		//File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		DbManager dbManager = null;
		Db db = null;
		AccessList result = null;
		List<User> result2 = new ArrayList<User>();
		
		try {
			dbManager = new DbManager(directory);
			db = dbManager.createTransaction();
			AccessList acl = db.getEm().createNamedQuery("AccessList.findByName", AccessList.class).setParameter("name", "managers").getSingleResult();
			User user = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", "manager").getSingleResult();
			result = db.getEm().createNamedQuery("AccessList.findUserInAcl", AccessList.class).setParameter("user", user).setParameter("acl", acl).getSingleResult();
			result2 = db.getEm().createNamedQuery("AccessList.findUsers", User.class).setParameter("acl", acl).getResultList();
		} catch (DbException e) {
			log.error("Failed to find if permisions is true or not", e.getRootParent());
		}catch( NoResultException | NonUniqueResultException e) {
			log.info("No result found", e);
		}finally{
			if(dbManager != null) {
				if(db != null) {
					dbManager.closeTransaction(db);
				}
				dbManager.close();
			}
		}
		
		log.info("RESULT1: " + (result != null ? result.getName() : "No access"));
		
		log.info("RESULT2: ");
		for(User usuario : result2) {
			log.info(usuario.getUid() + " \t " + usuario.getFullName(null));
		}
		
		log.info("GAME OVER!!!");
	}
}
