package com.laetienda.notes.servlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.notes.lib.*;

public class Notas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathToNotes;
	private String[] pathParts;
	private String path;
	private String pandoc;
	
	public Notas(){
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		
		Notes notes = (Notes)config.getServletContext().getAttribute("notes");
		pathToNotes = notes.getPathToNotes();
		pandoc = notes.getPandoc();
		System.out.println("pathToNotes: " + pathToNotes);
		System.out.println("pandoc: " + pandoc);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		pathParts = (String[])request.getAttribute("pathParts");
		path = pathToNotes;
		
		for(int c=2; c < pathParts.length; c++){
			path = path + File.separator + pathParts[c];
		}
		
		Bean notes = new Bean(pandoc, path);
		request.setAttribute("notes", notes);
		System.out.println(notes.getFile().getAbsolutePath());
		if(notes.getFile().exists()){
			request.getRequestDispatcher("/WEB-INF/jsp/pages/home.jsp").forward(request, response);
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
