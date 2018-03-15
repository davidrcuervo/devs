package com.laetienda.web.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Root extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger log4j = LogManager.getLogger(Root.class);

	public void init(ServletConfig config) throws ServletException {
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] pathParts = (String[])request.getAttribute("pathParts");
		
		if(pathParts == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}else {
			log4j.debug("PathParts object has been found. $pathParts.length: " + Integer.toString(pathParts.length));
			request.getRequestDispatcher("/WEB-INF/jsp/home/home.jsp").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
