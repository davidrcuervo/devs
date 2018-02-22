package com.laetienda.dap;

public class DapUser {
	
	private Integer uid;
	private String cn;
	private String sn;
	private String password;

	public DapUser() {
		
	}
	
	public DapUser(Integer uid, String password) {
		setUid(uid);
		this.password = password;
	}
	
	public DapUser(Integer uid, String cn, String sn, String password, String password2) {
		setUid(uid);
		setCn(cn);
		setSn(sn);
		setPassword(password, password2);
	}
	
	public String getDn() {
		//TODO implement an algorithm to build the DN
		return null;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
