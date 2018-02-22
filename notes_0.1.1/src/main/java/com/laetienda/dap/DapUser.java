package com.laetienda.dap;

import com.laetienda.entities.EntityObject;

public class DapUser extends EntityObject{
	
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
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password, String password2) {
		this.password = password;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
