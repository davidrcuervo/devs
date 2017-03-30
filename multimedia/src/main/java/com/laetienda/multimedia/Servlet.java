package com.laetienda.multimedia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.laetienda.db.Connection;
import com.laetienda.db.Transaction;
import com.laetienda.db.exceptions.DbException;
import com.laetienda.log.JavaLogger;

public class Servlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	private String[] pathParts;
	private JavaLogger log;
	private MediaManager mediaManager;
	private Connection dbManager;
	private Transaction db;
	private Map<String, String> uploadProgress;
	
	public Servlet(){
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = request.getSession().getServletContext();
			
		mediaManager = (MediaManager)context.getAttribute("mediaManager");
		dbManager = (Connection)context.getAttribute("dbManager");
		db = dbManager.createTransaction();
		pathParts = (String[])request.getAttribute("pathParts");
		log = (JavaLogger)request.getAttribute("logger");
		
		log.debug("video_folder: " + mediaManager.getSetting("videos_folder"));
	}
	
	public void destroy(){
		dbManager.closeTransaction(db);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		log.info("running com.laetienda.multimedia servlet. doGet method");
		
		Bean bean = new Bean(db);
		request.setAttribute("multimedia", bean);
		
		String temp = (String)request.getSession().getAttribute("successMessage");
		
		if(temp != null){
			request.setAttribute("successMessage", temp);
			request.getSession().removeAttribute("successMessage");
		}
		
		//http://<server_name>/<context_path>
		if(pathParts.length == 1 && pathParts[0].length() == 0){
			
			request.getRequestDispatcher("/WEB-INF/jsp/media/show.jsp").forward(request, response);
		
		//http://<server_name>/<context_path>/video
		}else if(pathParts[0].equals("video") && pathParts.length == 1){
			
			request.getRequestDispatcher("/WEB-INF/jsp/media/video/show.jsp").forward(request, response);
			
		//http://<server_name>/<context_path>/video/video_name
		}else if(pathParts[0].equals("video") && pathParts.length == 2 && findVideoByUrl(pathParts[1]) != null){
			
			com.laetienda.multimedia.entities.Video video = findVideoByUrl(pathParts[1]);
			request.setAttribute("video", video);
			request.getRequestDispatcher("/WEB-INF/jsp/media/video/video.jsp").forward(request, response);
				
		//http://<server_name>/<context_path>/image/image.png
		}else if(pathParts[0].equals("image")){
				
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
		
		//http://<server_name>/<context_path>/src/video.mp4
		}else if(pathParts[0].equals("src") && pathParts.length == 2){
			
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
		
		/** 
		 * Idea taken from the web below:
		 * 	http://stackoverflow.com/questions/4112686/how-to-use-servlets-and-ajax
		 */
		//http://<server_name>/<context_path>/ajax/1234
		}else if(pathParts.length == 2 && pathParts[0].equals("ajax")){
			log.debug("ajax requested. progressId: " + pathParts[1]);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			@SuppressWarnings("unchecked")
			Map<String, String> progress = (Map<String, String>)request.getSession().getAttribute(pathParts[1]);
			
			if(progress == null){
				progress = new HashMap<String, String>();
				progress.put("porcentage", "0");
			}
			
			String json = new Gson().toJson(progress);
			response.getWriter().write(json);
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		final int THRESHOLD_SIZE = 1024 * 1024 * 10;		// 10MB
		//final int MAX_FILE_SIZE = 1024 * 1024 * 100;		// 100MB
		//final int MAX_REQUEST_SIZE = 1024 * 1024 * 150;		// 150MB
		
		String submit = null;
		File storeFile = null;
		Map<String, String> inputs = new HashMap<String, String>();
		
		if(ServletFileUpload.isMultipartContent(request)){
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(THRESHOLD_SIZE);
			factory.setRepository(new File(mediaManager.getSetting("temp_folder")));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			//	upload.setFileSizeMax(MAX_FILE_SIZE);
			// upload.setSizeMax(MAX_REQUEST_SIZE);
			
			ProgressListener fileUploadProgress = new ProgressListener(){
				
				public void update(long pBytesRead, long pContentLength, int pItems){
					
					int porcentage = (int)(100.00 * pBytesRead / pContentLength);
					
					log.debug("pBytesRead: " + Long.toString(pBytesRead));
					log.debug("pContentLength: " + Long.toString(pContentLength));
					log.debug("pItems: " + Integer.toString(pItems));
					log.debug("porcentage: " + Integer.toString(porcentage));
					
					@SuppressWarnings("unchecked")
					Map<String, String> uploadFileProgress = (Map<String, String>)request.getSession().getAttribute("uploadFileProgress");
					
					if(uploadFileProgress == null){
						uploadFileProgress = new HashMap<String, String>();
						request.getSession().setAttribute("uploadFileProgress", uploadFileProgress);
					}
					
					uploadFileProgress.put("porcentage", Integer.toString(porcentage));
				}				
			};
			
			upload.setProgressListener(fileUploadProgress);
			
			String uploadPath = mediaManager.getSetting("temp_folder");
			
			try{
				List<FileItem> formItems = upload.parseRequest(request);
				Iterator<FileItem> iter = formItems.iterator();
				
				while(iter.hasNext()){
					FileItem item = iter.next();
					
					if(item.isFormField()){
						inputs.put(item.getFieldName(), item.getString());
					
					}else{
						String fileName = new File(item.getName()).getName();
						
						if(fileName != null && !fileName.isEmpty()){
							String filePath = uploadPath + File.separator + fileName;
							inputs.put("filePath", filePath);
							storeFile = new File(filePath);
							item.write(storeFile);
						}
					}
				}
				
				submit = inputs.get("submit");
				
			}catch (Exception ex){
				log.exception(ex);
				inputs.put("error", "Internal error while uploading file");
			}
			
		}else{	
			submit = request.getParameter("submit");
		}
		
		switch(submit){
			
			case "video":
				uploadVideo(request, response, inputs);
				break;
			
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
		}
	}
	
	private void uploadVideo(HttpServletRequest request, HttpServletResponse response, Map<String, String> inputs) throws ServletException, IOException{
		
		com.laetienda.multimedia.entities.Video video = new com.laetienda.multimedia.entities.Video();
		File originalVideo = null;
		String fileName;
		//log.debug("videos_folder: " + mediaManager.getSetting("videos_folder"));
		
		try{
			File videoPath = new File(mediaManager.getSetting("videos_folder"));
			
			if(inputs.get("filePath") == null || inputs.get("filePath").isEmpty()){
				video.addError("video", "Make sure that you have selected a valid video file");
			}
			
			if(inputs.containsKey("error")){
				video.addError("video", inputs.get("error"));
			}
			
			if(video.getErrors().size() <= 0){
				uploadProgress = new HashMap<String,String>();
				uploadProgress.put("porcentage", "0");
				request.getSession().setAttribute(inputs.get("progressId"), uploadProgress);
				
				originalVideo = new File(inputs.get("filePath"));
				
				int extIndex = originalVideo.getName().lastIndexOf('.');
				
				if(extIndex == -1){
					fileName = originalVideo.getName();
				}else{
					fileName = originalVideo.getName().substring(0, extIndex);
				}
				
				video.setUrl(fileName, db);
				video.setName(inputs.get("name"));
				video.setDescription(inputs.get("description"));
			
				String mp4 = videoPath.getAbsolutePath() + File.separator + video.getUrl() + ".mp4";
				String webm = videoPath.getAbsolutePath() + File.separator + video.getUrl() + ".webm";
				video.setMp4(mp4);
				video.setWebm(webm);
				
				/*
				log.debug("videoPath: " + videoPath.getAbsolutePath());	
				log.debug("fileName: " + fileName);
				log.debug("mp4: " + mp4);
				log.debug("webm: " + webm);
				*/ 
				
				List<String> encodeToMp4 = Arrays.asList("ffmpeg", 
						"-i", originalVideo.getAbsolutePath(),
						"-vcodec", "libx264",
						"-acodec", "aac",
						"-strict", "-2",
						mp4);
			
				List<String> encodeToWebm = Arrays.asList("ffmpeg",
						"-i", originalVideo.getAbsolutePath(),
						"-vcodec", "libvpx",
						"-qmin", "0",
						"-qmax", "50",
						"-crf", "10",
						"-b:v", "1M",
						"-acodec", "libvorbis",
						webm);
			
				if(video.getErrors().size() <= 0 && encode(encodeToMp4) == 0 && encode(encodeToWebm) == 0 ){
					db.insert(video);
				}else{
					video.addError("video", "The uploaded file is not soported by the media server");
				}
			}
		}catch(MultimediaException ex){
			video.addError("video", "The file is not supported for the application encoder");
			log.exception(ex);
		}catch(DbException ex){
			video.addError("video", "Ther was an internal error while persisting info in the database");
			log.exception(ex);
		}finally{
			
			request.getSession().removeAttribute(inputs.get("progressId"));
			
			if(originalVideo !=  null){
				originalVideo.delete();
			}
			
			if(video.getErrors().size() > 0){
				request.setAttribute("video", video);
				doGet(request, response);
			}else{
				request.getSession().setAttribute("successMessage", "The video has been uploaded successfully!!");
				response.sendRedirect(request.getRequestURI());
			}
		}
	}
	
	/**
	 * Algorithm copied from the following link:
	 * <a href="http://stackoverflow.com/questions/10927718/how-to-read-ffmpeg-response-from-java-and-use-it-to-create-a-progress-bar">link</a>
	 * 
	 * @param cmd List<String>
	 * @return
	 * @throws MultimediaException
	 */
	
	private int encode(List<String> cmd) throws MultimediaException{
		int result = -1;
		
		ProcessBuilder pb = new ProcessBuilder(cmd);
		
		try{
			Process p = pb.start();
			final InputStream status = p.getErrorStream();
			
			
			new Thread(new Runnable(){
				
				public void run(){
				
					InputStreamReader reader = new InputStreamReader(status);
					Scanner scan = new Scanner(reader);
					
					Pattern duPattern = Pattern.compile("(?<=Duration: )[^.]*");
					Pattern timePattern = Pattern.compile("(?<=time=)[^.]*"); 
					
					String duration = scan.findWithinHorizon(duPattern, 0);				
					String match; 
					double processedSecs;
					double totalSecs = -1;
					double progress;
					String[] hms;
					
					if(duration != null){
						hms = duration.split(":");
						
						totalSecs = Integer.parseInt(hms[0]) * 3600 + Integer.parseInt(hms[1]) * 60 + Integer.parseInt(hms[2]);
						log.debug("Total duration: " + totalSecs + " seconds");
					}
					
					while(null != (match = scan.findWithinHorizon(timePattern, 0))){
						hms = match.split(":");
						processedSecs = Integer.parseInt(hms[0])*3600 + Integer.parseInt(hms[1])*60 + Integer.parseInt(hms[2]);
						progress = processedSecs / totalSecs;
						uploadProgress.put("porcentage", Integer.toString((int)(progress * 100)));
						log.debug(String.format("Progress: %.2f%%%n", progress * 100));
					}

					scan.close();
				}
			}).start();
			result = p.waitFor();
			
		}catch(IOException ex){
			ex.printStackTrace();
			throw new MultimediaException("" ,ex);
		}catch(InterruptedException ex){
			ex.printStackTrace();
			throw new MultimediaException("", ex);
		}
			
		return result;
	}

	private com.laetienda.multimedia.entities.Video findVideoByUrl(String url){
		com.laetienda.multimedia.entities.Video result = null;
		
		try{
			result = db.getEm().createNamedQuery("Video.findByUrl", com.laetienda.multimedia.entities.Video.class).setParameter("url", pathParts[1]).getSingleResult();
		}catch(NoResultException ex) {
			log.debug(ex.getMessage());
		}catch(NonUniqueResultException ex){
			log.debug(ex.getMessage());
		}
		
		return result;
	}
}
