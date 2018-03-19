package com.laetienda.acl;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.laetienda.db.Db;
import com.laetienda.db.DbManager;
import com.laetienda.entities.User;

public class AclFilter implements Filter {
	
	private DbManager dbManager;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		
		User user = (User)request.getSession().getAttribute("sessionUser");
				
		if(user != null) {
			Db db = dbManager.createTransaction();
			user = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", "tomcat").getSingleResult();
			dbManager.closeTransaction(db);
		}
		
		Acl acl = new Acl(user);
		request.setAttribute("acl", acl);
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		dbManager = (DbManager)arg0.getServletContext().getAttribute("dbManager");
	}
}
