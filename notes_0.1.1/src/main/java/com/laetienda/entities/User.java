package com.laetienda.entities;

import java.io.Serializable;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import org.apache.log4j.Logger;

import com.laetienda.db.Db;

/**
 * @author I849921
 *
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u"),
	@NamedQuery(name="User.findByUid", query="SELECT u FROM User u WHERE u.uid = :uid"),
	@NamedQuery(name="User.findByEmail", query="SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name="User.findByUidOrEmail", query="SELECT u FROM User u WHERE u.email = :input OR u.uid = :input")
})
public class User extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = Logger.getLogger(User.class);

	@Column(name="\"uid\"", length=254, unique=true, nullable=false)
	private String uid;
	
	@Column(name="\"email\"", length=254, unique=true, nullable=false)
	private String email;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="\"status_option_id\"", nullable=false, unique=false)
	private Option status;
	
	@OneToOne(cascade=CascadeType.PERSIST)
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
	public User(String uid, String password) {
		this.uid = uid;
		this.password = password;
	}
	
	public User(String uid, String email, Option status, Option language, Db db) {
		setUid(uid, db);
		setEmail(email, db);
		setStatus(status);
		setLanguage(language);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid, Db db) {
		this.uid = uid;
		
		if(uid == null || uid.isEmpty()) {
			addError("uid", "Username can't be empty");
		}else {
			if(uid.length() < 4) {
				addError("uid", "Username must have at least 4 characters");
			}
			
			if(uid.length() > 64) {
				addError("uid", "Username can't have more than 64 characters");
			}
			
			List<User> test = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", uid).getResultList();
			
			if(test != null && test.size() > 0) {
				addError("uid", "This username has already been registered");
			}
			
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email, Db db) {
		this.email = email;
		
		if(email == null || email.isEmpty()) {
			addError("email", "The email can't be empty");
		}else {
			if(email.length() > 254) {
				addError("email", "The mail can't have more than 255 charcters");
			}
			
			List<User> test = db.getEm().createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
			
			if(test != null && test.size() > 0) {
				addError("email", "This email address has already been registered");
			}
			
			try {
				InternetAddress address = new InternetAddress(email);
				address.validate();
			} catch (AddressException ex) {
				addError("email", "Please make sure you have typed a valid address");
				log4j.debug("Email address not valid.", ex);
			}
		}
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
			
			if(password.length() < 8) {
				addError("password", "The password must have at least 8 characters");
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
