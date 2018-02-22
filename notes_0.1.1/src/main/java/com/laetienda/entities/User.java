package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u")
})
public class User extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column(name="\"uid\"", unique=true, nullable=false)
	private Integer uid;
	
	@Column(name="\"email\"", length=254, unique=true, nullable=false)
	private String email;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"option_id\"", nullable=false, unique=false)
	private Option status;
	
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

	public Option getStatus() {
		return status;
	}

	public void setStatus(Option status) {
		this.status = status;
	}
	
}
