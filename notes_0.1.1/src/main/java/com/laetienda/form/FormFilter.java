package com.laetienda.form;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.laetienda.acl.Acl;
import com.laetienda.db.Db;
import com.laetienda.db.DbManager;
import com.laetienda.entities.Form;

public class FormFilter implements Filter {
	private final Logger log4j2 = LogManager.getLogger();
	
	private DbManager dbManager;
	private String create;
	private String show;
	private String edit;
	private String delete;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		String create = arg0.getInitParameter("create");
		String show = arg0.getInitParameter("show");
		String edit = arg0.getInitParameter("edit");
		String delete = arg0.getInitParameter("delete");
		
		this.create = create == null ? "create" : create;
		this.show = show == null ? "show" : show;
		this.edit = edit == null ? "edit" : edit;
		this.delete = delete == null ? "delete" : delete;
		
		this.dbManager = (DbManager)arg0.getServletContext().getAttribute("dbManager");
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		String[] pathParts = (String[])request.getAttribute("pathParts");
		Acl acl = (Acl)request.getAttribute("acl");
		Form form = findForm(pathParts[0]);
		
		try {
			if(form == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			
			}else if(pathParts[1] != create &&  
					pathParts[1] != show && 
					pathParts[1] != edit &&
					pathParts[1] != delete) {
				
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				
			}else if(pathParts[1] == create && !acl.hasPermission(form.getCanCreateAcl())) {
				
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			
			}else {
				
				String action = create == pathParts[1] ? "create" :
					(show == pathParts[1] ? "show" :
						(edit == pathParts[1] ? "edit" : "delete"));
				
				Bean forma = new Bean(form, request);
				forma.setAction(action);
				request.setAttribute("forma", forma);
				arg2.doFilter(arg0, arg1);
			} 
				
		
		}catch(ArrayIndexOutOfBoundsException ex ) {
			log4j2.warn("Index out of path parts lenght", ex);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}catch (FormException e) {
			log4j2.warn("Failed to proces form servlet", e.getRootParent());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
		
	private Form findForm(String formName) {
		Form result = null;
			
		Db db = dbManager.createTransaction();
		
		try {
			result = db.getEm().createNamedQuery("Form.findByName", Form.class).setParameter("name", formName).getSingleResult();
		}catch(NoResultException | NonUniqueResultException ex) {
			log4j2.warn("Form required in URL does not exist");
		}finally {
			dbManager.closeTransaction(db);
		}
		
		return result;
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
