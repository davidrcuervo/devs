package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u")
})
public class User extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log4j = Logger.getLogger(User.class);

	@Column(name="\"uid\"", unique=true, nullable=false)
	private Integer uid;
	
	@Column(name="\"email\"", length=254, unique=true, nullable=false)
	private String email;
	
	public User() {
		
	}
	
	public User(Integer uid, String email) {
		setUid(uid);
		setEmail(email);
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
