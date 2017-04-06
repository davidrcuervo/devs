package com.laetienda.notes;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private NotesManager notesManager;
	private String pandoc;
	private File directory;
	private String[] pathParts;
	
	public Servlet(){
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
		
		Bean notes = new Bean(pandoc, path);
		request.setAttribute("notes", notes);
		
		String action = request.getParameter("action");
		
		if(notes.getFile().exists()){
			
			if(action != null && (action.equals("play") || action.equals("pdf"))){
				
				notes.setFormat(action);
				request.getRequestDispatcher("/WEB-INF/jsp/notes/play.jsp").forward(request, response);
			
			}else if(action != null && action.equals("edit") && notes.getFile().isFile()){
				
				request.getRequestDispatcher("/WEB-INF/jsp/notes/edit.jsp").forward(request, response);
				
			}else{
				request.getRequestDispatcher("/WEB-INF/jsp/notes/show.jsp").forward(request, response);
			}
			
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
	}
}
