package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.log4j.Logger;

/**
 * @author I849921
 *
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u")
})
public class User extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = Logger.getLogger(User.class);

	@Column(name="\"uid\"", unique=true, nullable=false)
	private Integer uid;
	
	@Column(name="\"email\"", length=254, unique=true, nullable=false)
	private String email;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"status_option_id\"", nullable=false, unique=false)
	private Option status;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="\"language_option_id\"", nullable=false, unique=false)
	private Option language;
	
	@Transient
	private String cn;
	
	@Transient
	private String sn;

	@Transient
	private String password;

	@Transient
	private String description;
	
	public User() {
		
	}
	
	/**
	 * This constructor has been made for tomcat, the password will not be validated.
	 * @param uid
	 * @param password
	 */
	public User(Integer uid, String password) {
		this.uid = uid;
		this.password = password;
	}
	
	public User(Integer uid, String email, Option status, Option language) {
		setUid(uid);
		setEmail(email);
		setStatus(status);
		setLanguage(language);
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
		
		if(uid != null && uid > 100) {
			log4j.debug("uid has been correctly set. $uid: " + uid);
		}else {
			addError("user", "Internal error while saving user in the database");
			log4j.fatal("The uid for regestering user is not an integer");
		}
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

	public Option getLanguage() {
		return language;
	}

	public void setLanguage(Option language) {
		this.language = language;
		
		if(language == null || language.getName().isEmpty() || language.getName().equals("none")) {
			addError("language", "Select a valid language");
		}
	}
	
	public void setCn(String cn) {
		this.cn = cn;
		
		if(cn == null || cn.isEmpty()) {
			addError("cn", "First Name can't be empty");
		}else {
			if(cn.length() > 254) {
				addError("cn", "The name can't have more than 255 charcters");
			}
			
			//TODO validate that cn has only letters, no numbers or special characters
		}
	}

	public String getCn() {
		return cn;
	}
	
	public void setSn(String sn) {
		this.sn = sn;
		
		if(sn == null || sn.isEmpty()) {
			addError("sn", "Last name can't be empty");
		}else {
			if(sn.length() > 254) {
				addError("sn", "The last name can't have more than 255 charcters");
			}
			
			//TODO validate that sn has only letters, no numbers or special characters
		}
	}	
	
	public String getSn() {
		return sn;
	}

	public void setPassword(String password, String password2) {
		this.password = password;
		
		if(password == null || password.isEmpty()) {
			addError("password", "The password can't be empty");
		}else {
			if(!password.equals(password2)) {
				addError("password", "The password and confirmation should be identical");
			}
			
			if(password.length() > 255) {
				addError("password", "The password can't have more than 255 characters");
			}
		}
	}	
	
	public String getPassword() {
		return password;
	}

	public void setDescription(String description) {
		this.description = description;
		
		if(description == null || description.isEmpty()) {
			addError("description", "Description can't be empty");
		}else {
			if(description.length() > 254) {
				addError("description", "The description can't have more than 255 charcters");
			}
		}
	}	
	
	public String getDescription() {
		return description;
	}
	
}
