package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="grupos")
@NamedQueries({
	@NamedQuery(name="Group.findall", query="SELECT g FROM Group g")
})
public class Group extends Objeto implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(Group.class);
	
	@Column(name="\"name\"", nullable=false, unique=true, length=254)
	private String name;
	
	@Column(name="\"description\"", nullable=true, unique=false, length=254)
	private String description;

	public Group() {
		
	}
	
	public Group (String name, String description) {
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
}
