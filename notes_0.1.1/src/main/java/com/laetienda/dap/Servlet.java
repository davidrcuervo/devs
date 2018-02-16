package com.laetienda.dap;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.AppException;
import com.laetienda.db.Db;
import com.laetienda.db.DbManager;
import com.laetienda.entities.User;
import com.laetienda.options.OptionsManager;

import org.apache.log4j.Logger;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger log4j = Logger.getLogger(Servlet.class);
	private String[] pathParts;
	private OptionsManager optManager;
	private DbManager dbManager;
	private DapBean dap = null;
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
			dap = new DapBean(dapManager.createConnection());
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
	}
}
