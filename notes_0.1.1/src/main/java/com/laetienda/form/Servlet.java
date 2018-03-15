package com.laetienda.form;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;

import com.laetienda.db.Db;
import com.laetienda.db.DbManager;
import com.laetienda.entities.*;

import org.apache.logging.log4j.LogManager;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log4j2 = LogManager.getLogger(Servlet.class);
	
	DbManager dbManager;
	
	@Override
	public void init(ServletConfig config) throws ServletException{
		log4j2.debug("Initializing FORM servlet");
		
		dbManager = (DbManager)config.getServletContext().getAttribute("dbManager");
	}
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String servletName = request.getServletPath();
		
		String[] pathParts = (String[])request.getAttribute("pathParts");
				
		//https://<server-name>:<port>/<context-path>/:ObjectName
		//https://<server-name>:<port>/<context-path>/:ObjectName/create
		//https://<server-name>:<port>/<context-path>/:ObjectName/show/:idUrlEncrypted
		//https://<server-name>:<port>/<context-path>/:ObjectName/edit/:idUrlEncrypted
		//https://<server-name>:<port>/<context-path>/:ObjectName/delete
		
		try {
			EntityObject entidad = findEntidad(pathParts[0]);
			if(entidad != null) {
				switch (pathParts[1]) {
				
				case "create":
				case "show":
				case "edit":
				case "delete":
					Bean forma = new Bean(entidad, request, response);
					request.setAttribute("forma", forma);
					request.getRequestDispatcher("/WEB-INF/jsp" + servletName + "/crud.jsp").forward(request, response);
					break;
				default:
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					break;
				}
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			log4j2.warn("Index out of pathParts length", ex);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}catch(FormException ex) {
			log4j2.warn("Failed to process form servlet", ex.getRootParent());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}finally {
			
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] pathParts = (String[])request.getAttribute("pathParts");
		EntityObject entidad;
		
		try {
			entidad = findEntidad(pathParts[0]);
			
			if(entidad != null) {
				
			}
			
		} catch (FormException ex) {
			log4j2.warn("Failed to process form", ex.getRootParent());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@Override
	public void destroy(){
		log4j2.debug("Closing/destroying From (CRUD) Servlet");
	}
	
	public static void main(String[] args) {
		File directory = new File("test.jsp");
		
		log4j2.info("$directory: " + directory.getAbsolutePath());
	}
	
	private EntityObject findEntidad(String entidad) throws FormException  {
				
		EntityObject result = null;
			
		try {
		
			if(entidad == null || entidad.isEmpty()) {
				throw new FormException("Invalid object name in URL");
			}else {
				Class<?> clazz = Class.forName("com.laetienda.entities." + entidad);
				Object temp = clazz.newInstance();
				
				if(temp instanceof Objeto) {
					result = (Objeto)temp;
				}else {
					throw new FormException("Object Name in URL does not correspond to a valid object");
				}
			}
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			throw new FormException("Failed to find a valid object for url string");
		
		}
		
		return result;
	}
	
}
