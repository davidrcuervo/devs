package com.laetienda.dap;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.app.AppException;
import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.entities.Option;
import com.laetienda.entities.User;
import com.laetienda.options.OptionsManager;

import org.apache.log4j.Logger;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger log4j = Logger.getLogger(Servlet.class);
	private String[] pathParts;
	private OptionsManager optManager;
	private DbManager dbManager;
	private Dap dap;
	private Db db;
	
	public Servlet(){
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException{
		
		dbManager = (DbManager)config.getServletContext().getAttribute("dbManager");
		DapManager dapManager = (DapManager)config.getServletContext().getAttribute("dapManager");
		db = dbManager.createTransaction();
		
		try{
			dap = dapManager.createDap();
		}catch(DapException ex){
			//TODO
		}
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathParts = (String[])request.getAttribute("pathParts");
		optManager = (OptionsManager)request.getServletContext().getAttribute("optManager");
	}
	
	@Override
	public void destroy(){
		dbManager.closeTransaction(db);
		try{
			dap.close();
		}catch(DapException ex){
			//TODO
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		switch(pathParts[0]){
			
			//https:///<server-name>:<port>/<context-path>
			case "":
				response.sendRedirect(request.getServletPath() + "/login");
				break;
		
			//https:///<server-name>:<port>/<context-path>/login
			case "signup":
			case "password":
			case "login":
				request.getRequestDispatcher("/WEB-INF/jsp/dap/container.jsp").forward(request, response);
				break;
		
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		switch (request.getParameter("submit")){
		
			case "signup":
				signup(request, response);
				break;
				
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
		}
	}
	
	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User user = new User();
		DapUser dapUser = new DapUser();
		
		try {
			Option status = db.findOption("user status", "registered");
			Option language = db.findOption("languages", request.getParameter("language"));
			Integer uid = db.getNextUid();
			dapUser.setUid(uid);
			dapUser.setCn(request.getParameter("cn"));
			dapUser.setSn(request.getParameter("sn")); 
			dapUser.setMail(request.getParameter("email"));
			dapUser.setPassword(request.getParameter("password"), request.getParameter("password_confirm"));

			user.setEmail(dapUser.getMail());
			user.setStatus(status);
			user.setLanguage(language);
			
			if(dapUser.getErrors().size() > 0 || user.getErrors().size() > 0) {
				db.insert(user);
				dap.insertUser(dapUser);
			}
			
		}catch(DbException ex) {
			log4j.error("Failed to persist user in database", ex.getRootParent());
			dapUser.addError("user", "Internal error. There was an error while saving into the database");
		} catch (DapException e) {
			try {
				db.remove(user);
			} catch (DbException e1) {
				log4j.fatal("Failed to remove user from DB that was not able to be saved in ldap directory", e1.getRootParent());
			}
		}finally {
			if(dapUser.getErrors().size() > 0 || user.getErrors().size() > 0) {
				log4j.info("FAILED to add user to the website");
				request.setAttribute("user", dapUser);
				doGet(request, response);
			}else {
				log4j.info("User has been added SUCCESFULLY");
			}
		}
		
		/*
		user.setStatus(optManager.findOption("User status", "registered"));
		user.setCn(request.getParameter("cn"));
		user.setSn(request.getParameter("sn"));
		user.setMail(request.getParameter("email"), dap);
		user.setPassword(request.getParameter("password"), request.getParameter("password_confirm"));
		
		try{
			db.insertNoCommit(user);
			dap.addEntry(user);
			db.commit();
		}catch(AppException ex){
			user.addError("user", "Internal error");
			log4j.error("Failed to process signup form", ex);
			dap.deleteEntry(user);
		}finally{
			if(user.getErrors().size() > 0){
				request.setAttribute("user", user);
			}else{
				request.setAttribute("UserSignupForm", "success");
			}
			doGet(request, response);
		}
		*/
	}
}
