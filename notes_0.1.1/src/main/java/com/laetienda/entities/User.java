package com.laetienda.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.logging.log4j.Logger;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.logging.log4j.LogManager;

import com.laetienda.dap.DapException;
import com.laetienda.dap.Ldap;

/**
 * @author I849921
 *
 */
/**
 * @author myself
 *
 */
@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u"),
	@NamedQuery(name="User.findByUid", query="SELECT u FROM User u WHERE u.uid = :uid"),
//	@NamedQuery(name="User.findByEmail", query="SELECT u FROM User u WHERE u.email = :email"),
//	@NamedQuery(name="User.findByUidOrEmail", query="SELECT u FROM User u WHERE u.email = :input OR u.uid = :input")
})
public class User extends Objeto implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = LogManager.getLogger(User.class);

	@Column(name="\"uid\"", length=254, unique=true, nullable=false)
	private String uid;
	
	@OneToOne(cascade= {CascadeType.REFRESH})
	@JoinColumn(name="\"status_option_id\"", nullable=false, unique=false)
	private Option status;
	
	@OneToOne(cascade= {CascadeType.REFRESH})
	@JoinColumn(name="\"language_option_id\"", nullable=false, unique=false)
	private Option language;
	
	@Transient
	private Entry ldapEntry;

	@Transient
	private Ldap ldap;

	public User() {
		ldap = new Ldap();
	}
	
	/**
	 * This constructor has been made for tomcat, the password will not be validated.
	 * @param uid
	 * @throws DapException 
	 */
	public User(String uid) throws DapException {
		ldap = new Ldap();
		this.uid = uid;
		
	}
	
	/**
	 * 
	 * @param username Must be unique in ldap. It will be used to build uid.
	 * @param name 
	 * @param lastname
	 * @param email
	 * @param status
	 * @param language
	 * @param conn
	 * @throws DapException
	 */
	public User(String username, String name, String lastname, String email, Option status, Option language, LdapConnection conn) throws DapException {
		ldap = new Ldap();
		
		String tempUid = "uid=" + username;		
		setUid(tempUid, conn);
		
		ldapEntry = setLdapEntry(conn);
		setCn(name);
		setSn(lastname);
		setEmail(email, conn);
		setStatus(status);
		setLanguage(language);
	}

	private Entry setLdapEntry(LdapConnection conn) throws DapException {
		
		Entry result = ldap.searchDn(uid, conn);
		
		try {
			if(result == null) {
				Dn dn = new Dn(uid, "ou=People", ldap.getDomainDn().getName());
				result = new DefaultEntry(dn);
				result.add("objectclass", "person")
					.add("objectclass", "inetOrgPerson")
					.add("uid", dn.getRdn(0).getValue())
					.add("ou", "People");
			}
		} catch (LdapException e) {
			throw new DapException(e);
		} 
		
		return result;
	}

	public String getUid() {
		return uid;
	}
	
	/*
	public void setUid(String uid, Db db) {
		setUid(uid, db.getEm());
	}
	*/
	public void setUid(String uid, LdapConnection conn) throws DapException {
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
			
			if(ldap.searchDn(uid, conn) != null) {
				addError("uid", "Username already exists");
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
	
	public void setEmail(String email, LdapConnection conn) throws DapException {
		
		try {
			ldapEntry.add("mail", email);
			if(email == null || email.isEmpty()) {
				addError("email", "The email can't be empty");
			}else if(email.length() > 254) {
				addError("email", "The mail can't have more than 255 charcters");
			}else {	
				EntryCursor search = conn.search(ldap.getPeopleLdapEntry(conn).getDn(), "(mail=" + email + ")", SearchScope.ONELEVEL);
			
				if(search.iterator().hasNext()) {
					addError("email", "This email address has already been registered");
				}
			}
		} catch (DapException | LdapException e) {
			addError("email", "The application were not able to find out if email has been registered");
			throw new DapException(e);
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
	/**
	 * 
	 * @param cn CN DAP Direcotory entry, common used for the First Name
	 * @throws DapException 
	 */
	public void setCn(String cn) {
		
		try {
			ldapEntry.add("cn", cn);
	
			if(cn == null || cn.isEmpty()) {
				addError("cn", "First Name can't be empty");
			}else {
				if(cn.length() > 254) {
					addError("cn", "The name can't have more than 255 charcters");
				}
				
				//TODO validate that cn has only letters, no numbers or special characters
			}
		} catch (LdapException e) {
			addError("cn", "The name is not valid");
		}
	}

	public String getCn() throws DapException {
		String result = null;
		try {
			result = ldapEntry.get("cn").getString();
		} catch (LdapInvalidAttributeValueException e) {
			throw new DapException(e);
		}
		return result;
	}
	
	/**
	 * 
	 * @param sn SN (SureName) LDAP entry, common used for last name
	 */
	public void setSn(String sn) {
		
		try {
			ldapEntry.add("sn", sn);
			
			if(sn == null || sn.isEmpty()) {
				addError("sn", "Last name can't be empty");
			}else {
				if(sn.length() > 254) {
					addError("sn", "The last name can't have more than 255 charcters");
				}
				
				//TODO validate that sn has only letters, no numbers or special characters
			}
		} catch (LdapException e) {
			addError("sn", "The last name is not valid");
			log4j.debug("Exception adding sn to ldap entry." + e.getMessage());
		}
	}	
	
	public String getSn() throws DapException {
		String result = new String();
		String sn;
		
		try {
			sn = ldapEntry.get("sn").getString();
		
			if(sn == null || sn.equals("Snless")) {
				log4j.debug("sn is null or Snless. $sn: " + sn);
			}else {
				result = sn;
			}
		
		} catch (LdapInvalidAttributeValueException e) {
			throw new DapException(e);
		}
		return result;
	}

	public void setPassword(String password, String password2) {
		
		try {
			ldapEntry.add("userPassword", password);

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
		} catch (LdapException e) {
			addError("password", "Password is not valid");
			log4j.debug("Exception adding description to ldap entry." + e.getMessage());
		}
	}	
	
	public void setDescription(String description) {
		
		try {
			ldapEntry.add("description", description);
			if(description == null || description.isEmpty()) {
				addError("description", "Description can't be empty");
			}else {
				if(description.length() > 254) {
					addError("description", "The description can't have more than 255 charcters");
				}
			}
		} catch (LdapException e) {
			addError("description", "This not a valid description");
			log4j.debug(e.getMessage());
		}
	}	
	
	public String getName() {
		String result = null;
		
		try {
			result = ldapEntry.get("cn").getString() + " " + ldapEntry.get("sn").getString();
		} catch (LdapInvalidAttributeValueException e) {
			result = new String();
			log4j.debug("EXCEPTION: while getting name from person ldap attribute. " + e.getMessage());
		}
		
		return result;
	}
	
	public String getDescription() {
		String result = new String();
		try {
			result = ldapEntry.get("description").getString();
		} catch (LdapInvalidAttributeValueException e) {
			result = new String();
			log4j.debug("EXCEPTION: while getting description from person ldap attribute. " + e.getMessage());
		}
		return result;
	}
	
	public Entry getLdapEntry() {
		return ldapEntry;
	}
}
