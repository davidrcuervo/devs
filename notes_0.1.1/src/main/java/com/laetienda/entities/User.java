package com.laetienda.entities;

import javax.persistence.*;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;

import java.io.Serializable;
import java.util.List;

import com.laetienda.dap.DapBean;
import com.laetienda.dap.DapEntry;
import com.laetienda.dap.DapException;
import com.laetienda.options.Option;
import com.laetienda.dap.Ldif;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u")
})
public class User extends EntityObject implements DapEntry, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Transient
	public static final String ID_NAME = "user_id_counter";
	
	@Transient
	public static final int FIRST_ID = 101;
	
	@Id
	@Column(name="\"id\"", updatable=false, nullable=false, unique=true)
	private Integer id;
	
	@Column(name="\"status\"", nullable=false, unique=false, length=254)
	private String status;
	
	@Transient
	private String cn;
	
	@Transient
	private String sn;
	
	@Transient 
	private String mail;
	
	@Transient
	private String password;
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail, DapBean dap) {
		this.mail = mail;
		if(mail == null || mail.isEmpty()){
			
		}else{
			
			List<DapEntry> temp = dap.searchEntry("ou=people", String.format("(mail=%s)", mail), User.class);
			if(temp.size() > 0){
				addError("email", "This email address has been already registered");
			}
		}
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
		
		if(sn == null || sn.isEmpty()){
			addError("sn", "Last Name must not be empty");
		}else{
			if(sn.length() < 255){
				//sn is ok to be persisted
			}else{
				addError("sn", "Last Name must have maximum 254 characters");
			}
		}
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
		
		if(sn == null || sn.isEmpty()){
			addError("cn", "First Name must not be empty");
		}else{
			if(sn.length() < 255){
				//cn is ok to be persisted
			}else{
				addError("cn", "First Name must have maximum 254 characters");
			}
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password, String confirm_password) {
		
		if(password == null || password.isEmpty()){
			addError("password", "Password should not be empty");
		}else{
			if(password.equals(confirm_password)){
				this.password = password;
			}else{
				addError("password", "Password and confirm password does not match");
			}
		}
	}

	public void setStatus(Option option){
		if(option == null){
			super.addError("user", "The status is null");
		}
		
		status = option.getValue();
	}
	
	public String getStatus(){
		return status;
	}

	@Override
	public String getIdentifierName() {
		return ID_NAME;
	}
	
	@Override
	public User setIdentifierValue(Integer id){
			
		this.id = id;		
		return this;
	}
	
	@Override
	public Entry getEntry() throws DapException{
		
		Entry result = null;
		try{
			Dn dn = new Dn("uid=" + id,"ou=People", Ldif.getDomain());
			result = new DefaultEntry(dn)
					.add("objectclass", "person")
					.add("objectclass", "inetOrgPerson")
					.add("objectclass", "organizationalPerson")
					.add("objectclass", "top")
					.add("ou", "People")
					.add("uid", id.toString())
					.add("cn", cn)
					.add("sn", sn)
					.add("mail", mail)
					.add("userpassword", password)
					.add("description", "User added by the web application");
			
		}catch(LdapInvalidDnException ex){
			throw new DapException("Unable to build user entry", ex);
		}catch(LdapException ex){
			throw new DapException("Unable to buidl user entry", ex);
		}
		
		return result;
	}
	
	@Override
	public void setDapEntry(Entry entry){
		id = Integer.parseInt(entry.get("uid").get().getString());
		cn = entry.get("cn").get().getString();
		sn = entry.get("sn").get().getString();
		mail = entry.get("mail").get().getString();
	}
}
