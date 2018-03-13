package com.laetienda.dap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.laetienda.entities.EntityObject;

@Deprecated
public class DapUser extends EntityObject{
	private final static Logger log4j = LogManager.getLogger(DapUser.class);	
	
	private Integer uid;
	private String cn;
	private String sn;
	private String password;
	private String mail;
	private String description;

	public DapUser() {

	}
	
	public DapUser(Integer uid, String password) {
		setUid(uid);
		this.password = password;
	}
	
	public DapUser(Integer uid, String cn, String sn, String email, String password, String password2) {
		setUid(uid);
		setCn(cn);
		setSn(sn);
		setMail(email);
		setPassword(password, password2);
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

	public String getCn() {
		return cn;
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

	public String getSn() {
		return sn;
		
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

	public String getPassword() {
		return password;
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
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
		
		if(mail == null || mail.isEmpty()) {
			addError("mail", "The email can't be empty");
		}else {
			if(mail.length() > 254) {
				addError("mail", "The mail can't have more than 255 charcters");
			}
			
			//TODO validate that mail has a proper structure. tip: use regular expresions
		}
		
	}

	public String getDescription() {
		return description;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
