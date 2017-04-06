package com.laetienda.notes;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Slides extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private NotesManager notesManager;
	private String pandoc;
	private File directory;
	private String[] pathParts;
	
	public Slides(){
		super();
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		notesManager = (NotesManager)request.getSession().getServletContext().getAttribute("notesManager");
		pathParts = (String[])request.getAttribute("pathParts");
		pandoc = notesManager.getSetting("pandoc");
		directory = new File(notesManager.getSetting("pathToNotes"));
	}
	
	public void destroy(){
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		build(request, response);
		
		String path = directory.getAbsolutePath();
		
		for(int c=0; c < pathParts.length; c++){
			path += File.separator + URLDecoder.decode(pathParts[c], "utf-8");
		}
		
		Bean slides = new Bean(pandoc, path);
		File file = slides.getFile();
		request.setAttribute("slides", slides);
		System.out.println("file: " + file.getAbsolutePath());
		if(file.exists() && file.isFile()){
			request.getRequestDispatcher("/WEB-INF/jsp/notes/slides.jsp").forward(request, response);
			
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
	}
}