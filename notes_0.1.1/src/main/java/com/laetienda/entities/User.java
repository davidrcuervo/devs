package com.laetienda.entities;

import java.io.Serializable;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import org.apache.logging.log4j.Logger;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;

import com.laetienda.dap.Dap;
import com.laetienda.dap.DapException;
import com.laetienda.dap.DapManager;
import com.laetienda.dap.Ldap;
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
	private static final Logger log4j = LogManager.getLogger(User.class);

	@Column(name="\"uid\"", length=254, unique=true, nullable=false)
	private String uid;
	
	/*
	@Column(name="\"email\"", length=254, unique=true, nullable=false)
	private String email;
	*/
	@OneToOne(cascade= {CascadeType.REFRESH})
	@JoinColumn(name="\"status_option_id\"", nullable=false, unique=false)
	private Option status;
	
	@OneToOne(cascade= {CascadeType.REFRESH})
	@JoinColumn(name="\"language_option_id\"", nullable=false, unique=false)
	private Option language;
	
	@Transient
	private Entry ldapEntry;
	/*
	@Transient
	private String cn;
	
	@Transient
	private String sn;

	@Transient
	private String password;

	@Transient
	private String description;
	*/
	public User() {
		
	}
	
	/**
	 * This constructor has been made for tomcat, the password will not be validated.
	 * @param uid
	 * @throws DapException 
	 */
	public User(String uid/*, String password*/) throws DapException {
		this.uid = uid;
		
	}
	
	public User(String uid, String email, Option status, Option language, DapManager dapManager) throws DapException {
		ldapEntry = new DefaultEntry();
		setUid(uid, dapManager);
		setEmail(email, dapManager);
		setStatus(status);
		setLanguage(language);
	}

	public String getUid() {
		return uid;
	}
	
	/*
	public void setUid(String uid, Db db) {
		setUid(uid, db.getEm());
	}
	*/
	public void setUid(String uid, DapManager dapManager) {
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
			
			
			
		}
	}
	
	public String getEmail() throws DapException {
		try {
			return ldapEntry.get("email").getString();
		} catch (LdapInvalidAttributeValueException e) {
			throw new DapException(e);
		}
	}
	
	/*
	public void setEmail(String email, Db db) {
		setEmail(email, db.getEm());
	}
	*/
	public void setEmail(String email, DapManager dapManager) throws DapException {
		
		LdapConnection ldap = null;
		try {
			ldapEntry.add("email", email);
			if(email == null || email.isEmpty()) {
				addError("email", "The email can't be empty");
			}else if(email.length() > 254) {
					addError("email", "The mail can't have more than 255 charcters");
			}else {	
			
				ldap = dapManager.createLdap();
				EntryCursor search = ldap.search(dapManager.getPeople().getDn(), "(mail=" + email + ")", SearchScope.ONELEVEL);
			
				if(search.next()) {
					addError("email", "This email address has already been registered");
				}
			}
		} catch (DapException | LdapException | CursorException e) {
			addError("email", "The application were not able to find out if email has been registered");
			throw new DapException(e);
		}finally {
			dapManager.closeConnection(ldap);
		}
	}

	/*
	public void setEmail(String email, EntityManager em) {
		//this.email = email;
		
		if(email == null || email.isEmpty()) {
			addError("email", "The email can't be empty");
		}else {
			if(email.length() > 254) {
				addError("email", "The mail can't have more than 255 charcters");
			}
			
			List<User> test = em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
			
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
	*/
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
	/**
	 * 
	 * @param cn CN DAP Direcotory entry, common used for the First Name
	 */
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
	
	/**
	 * 
	 * @param sn SN (SureName) LDAP entry, common used for last name
	 */
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
		String result = new String();
		
		if(sn == null || sn.equals("Snless")) {
			log4j.debug("sn is null or Snless. $sn: " + sn);
		}else {
			result = sn;
		}
		
		return result;
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
	
	public String getName() {
		return getCn() + " " + getSn();
	}
	
	public String getFullName(Dap dap) {
		
		User temp = this;
		
		
			try {
				temp = dap.userSyncDbAndLdap(temp);
				
			} catch (NullPointerException | DapException e) {
				log4j.error("Failed to get First Name and Last Name from LDAP", e);
			}
		
		
		return temp.getCn() + " " + temp.getSn();
	}
	/*
	public String getFullName(Object dap) {
		String result = new String();
		if(dap instanceof Dap) {
			result = getFullName((Dap)dap);
		}
		return result;
	}
	*/
	public String getDescription() {
		return description;
	}
	
}
