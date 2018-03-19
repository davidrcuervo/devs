package com.laetienda.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="grupos")
@NamedQueries({
	@NamedQuery(name="Group.findall", query="SELECT g FROM Group g")
})
public class Group extends Objeto implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(Group.class);
	
	@Column(name="\"name\"", nullable=false, unique=true, length=254)
	private String name;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=false)
	private List<User> users = new ArrayList<User>();

	public Group() {
		
	}
	
	public Group (String name, String description) {
		setName(name);
		setDescription(description);
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public Group addUser(User user) {
		
		boolean flag = true;
		
		for(User usuario : users) {
			if(user.getId().equals(usuario.getId())) {
				flag = false;
				break;
			}
		}
		
		if(flag) {
			users.add(user);
		}
		
		return this;
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
	
	public static void main(String[] args) {
		log.info("Hello " + Group.class.getName() + "!!!.");
	}
	
}
