package com.laetienda.multimedia;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;


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
		
		//http://<server_name>/<context_path>/image/image.png
		if(pathParts[0].equals("image")){
				
			try{
				
				Image image = mediaManager.createImage(pathParts);
				response.setContentType(image.getMimeType());
				
				ImageIO.write(	image.get(request.getParameter("width"), request.getParameter("height")), 
								image.getExtension(), 
								response.getOutputStream());
			}catch(MultimediaException ex){
				log.exception(ex);
				
				if(ex.getParent() != null)
					log.exception(ex.getParent());
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		
		//http://<server_name>/<context_path>/video/video.mp4
		}else if(pathParts[0].equals("video") && pathParts.length == 2){
			
			try{
				Video video = mediaManager.createVideo(pathParts);
				video.setRange(request.getHeader("Range"));
				
				response.reset();
				response.setBufferSize(video.getBufferLength());
				response.setHeader("Content-Disposition", String.format("inline;filename=\"%s\"", video.getFileName()));
				response.setHeader("Accept-Ranges", "bytes");
				response.setDateHeader("Last-Modified", video.getLastModifiedTime());
				response.setDateHeader("Expires", video.getExpireTime());
				response.setContentType(video.getContentType());
				response.setHeader("Content-Range", String.format("bytes %s-%s/%s", video.getStart(), video.getEnd(), video.getLength()));
				response.setHeader("Content-Length", String.format("%s", video.getContentLenght()));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				
				int bytesRead;
				int bytesLeft = video.getContentLenght();
				ByteBuffer buffer = ByteBuffer.allocate(video.getBufferLength());
				
				SeekableByteChannel input = video.getInput();
				OutputStream output = response.getOutputStream();
				input.position(video.getStart());
				
				while((bytesRead = input.read(buffer)) != -1 && bytesLeft > 0){
					buffer.clear();
					output.write(buffer.array(), 0, bytesLeft < bytesRead ? bytesLeft : bytesRead);
					bytesLeft -= bytesRead;
				}
			}catch(MultimediaException ex){
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		
		//http://<server_name>/<context_path>/show/video_name
		}else if(pathParts[0].equals("show") && pathParts.length == 2){
			
			request.getRequestDispatcher("/WEB-INF/jsp/media/show.jsp").forward(request, response);
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
	}
	
	public void destroy(){
		
	}
}
