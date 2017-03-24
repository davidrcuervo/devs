package com.laetienda.multimedia;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.log.JavaLogger;

public class Servlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private String[] pathParts;
	private JavaLogger log;
	private MediaManager mediaManager;
	
	public Servlet(){
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		mediaManager = (MediaManager)config.getServletContext().getAttribute("mediaManager");
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathParts = (String[])request.getAttribute("pathParts");
		log = (JavaLogger)request.getAttribute("logger");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		log.info("running com.laetienda.multimedia servlet. doGet method");
		
		try{
			
			if(pathParts[0].equals("image")){
				
				Image image = mediaManager.createImage(pathParts);
				response.setContentType(image.getMimeType());
				
				ImageIO.write(	image.get(request.getParameter("width"), request.getParameter("height")), 
								image.getExtension(), 
								response.getOutputStream());
				
			}else if(pathParts[0].equals("video")){
				
			}else{
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}catch(MultimediaException ex){
			log.exception(ex);
			log.exception(ex.getParent());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
	}
	
	public void destroy(){
		
	}
}
