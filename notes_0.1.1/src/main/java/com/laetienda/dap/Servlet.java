package com.laetienda.dap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.entities.User;
import com.laetienda.options.OptionsManager;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String[] pathParts;
	private OptionsManager optManager;
	
	public Servlet(){
		super();
	}

	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathParts = (String[])request.getAttribute("pathParts");
		optManager = (OptionsManager)request.getServletContext().getAttribute("optManager");
	}
	
	public void destroy(){
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		switch(pathParts[0]){
			
			//https:///<server-name>:<port>/<context-path>
			case "":
				response.sendRedirect(request.getContextPath() + "/login");
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
	}
}
