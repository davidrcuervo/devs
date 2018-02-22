package com.laetienda.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="access_control_lists")
@NamedQueries({
	@NamedQuery(name="AccessList.findall", query="SELECT a FROM AccessList a")
})

public class AccessList extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(AccessList.class);
	
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
}
