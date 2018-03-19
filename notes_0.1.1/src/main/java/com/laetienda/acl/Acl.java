package com.laetienda.acl;

import java.util.List;

import com.laetienda.entities.AccessList;
import com.laetienda.entities.AclGroup;
import com.laetienda.entities.AclUser;
import com.laetienda.entities.Objeto;
import com.laetienda.entities.User;

public class Acl {
	
	private User user;
	
	public Acl(User user) {

	}
	
	public boolean canRead(Objeto objeto) {
		return compare(objeto.getRead());
	}
	
	public boolean canWrite(Objeto objeto) {
		return compare(objeto.getWrite());
	}
	
	public boolean canDelete(Objeto objeto) {
		return compare(objeto.getDelete());
	}
	
	private boolean compare(AccessList aList) {
		boolean result = false;
		
		List<AclUser> users = aList.getUsers();
		
		for(AclUser user : users) {
			if(user.getUser().getId() == this.user.getId()) {
				result = true;
				break;
			}
		}
		
		if(!result) {
			List<AclGroup> groups = aList.getGroups();
			
			for(AclGroup group : groups) {
				List<User> users2 = group.getGroup().getUsers();
				
				for(User user : users2) {
					if(user.getId() == this.user.getId()) {
						result = true;
						break;
					}
				}
			}
		}
		
		return result;
	}

	public User getUser() {
		return user;
	}



	public Acl setUser(User user) {
		this.user = user;
		return this;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
