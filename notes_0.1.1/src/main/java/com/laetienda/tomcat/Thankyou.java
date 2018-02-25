package com.laetienda.tomcat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Thankyou extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathParts = (String[])request.getAttribute("pathParts");
		
		String token = (String)request.getSession().getAttribute("ThankyouToken");
		request.getSession().removeAttribute("ThankyouToken");
		
		if(pathParts[0] != null && !pathParts[0].isEmpty() && token != null && token.equals(pathParts[0])) {
			request.getRequestDispatcher("/WEB-INF/jsp/thankyou/" + pathParts[0] + ".jsp").forward(request, response);
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}	
}
