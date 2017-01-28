package com.laetienda.notes.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.notes.lib.Notes;

public class Root extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String pathToNotes;
	
	public Root(){
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		Notes notes = (Notes)config.getServletContext().getAttribute("notes");
		pathToNotes = notes.getPathToNotes();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}