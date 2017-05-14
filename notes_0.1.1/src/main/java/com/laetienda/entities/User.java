package com.laetienda.entities;

import javax.persistence.*;

import java.io.Serializable;
import com.laetienda.db.entities.EntityObject;
import com.laetienda.options.Option;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findall", query="SELECT u FROM User u")
})
public class User extends EntityObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
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
	
	public void setStatus(Option option){
		if(option == null){
			super.addError("user", "The status is null");
		}
		
		status = option.getValue();
	}
	
	public String getStatus(){
		return status;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
