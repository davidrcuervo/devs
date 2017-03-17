package com.laetienda.lang;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.laetienda.db.Connection;
import com.laetienda.db.Transaction;
import com.laetienda.db.exceptions.*;
import com.laetienda.lang.entities.Language;
import com.laetienda.log.JavaLogger;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final int PAGE_SIZE = 20;
	
	private String[] pathParts;
	Connection dbManager;
	private Transaction db;
	private JavaLogger log;
	private Language langEntity;
	
	public Servlet(){
		super();
	}
	
	public void init(ServletConfig config) throws ServletException {
		dbManager = (Connection)config.getServletContext().getAttribute("dbManager");
		db = dbManager.createTransaction();
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathParts = (String[])request.getAttribute("pathParts");
		log = (JavaLogger)request.getAttribute("logger");
		
	}
	
	public void destroy(){
		dbManager.closeTransaction(db);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		int page = findPage(request.getParameter("page"));
		int rows = ((Number)db.getEm().createNamedQuery("Language.findAllCount").getSingleResult()).intValue();
		Integer pageAmount = (rows % PAGE_SIZE) == 0 ? (rows / PAGE_SIZE) : ((rows / PAGE_SIZE) + 1);
				
		List<Language> langList = db.getEm().createNamedQuery("Language.findAll", Language.class)
				.setFirstResult((page - 1) * PAGE_SIZE)
				.setMaxResults(PAGE_SIZE)
				.getResultList();
		
		request.setAttribute("langList", langList);
		request.setAttribute("pageNumber", page);
		request.setAttribute("pageAmount", pageAmount);
		
		if(request.getSession().getAttribute("langAddFormSuccess") != null){
			request.setAttribute("langAddFormSuccess", request.getSession().getAttribute("langAddFormSuccess"));
			request.getSession().removeAttribute("langAddFormSuccess");
		}
		
		if(request.getSession().getAttribute("langEditFormSuccess") != null){
			request.setAttribute("langEditFormSuccess", request.getSession().getAttribute("langEditFormSuccess"));
			request.getSession().removeAttribute("langEditFormSuccess");
		}
		
		if(request.getSession().getAttribute("lang_delete_success") != null){
			request.setAttribute("lang_delete_success", request.getSession().getAttribute("lang_delete_success"));
			request.getSession().removeAttribute("lang_delete_success");
		}
		
		if(pathParts.length == 1 && pathParts[0].length() == 0){
			request.getRequestDispatcher("/WEB-INF/jsp/lang/show.jsp").forward(request, response);
		}else if(pathParts.length == 2 && pathParts[0].equals("edit") && findLanguage(pathParts[1]) != null){
			log.debug("doGet requested to edit lang row. $id" + pathParts[1]);
			request.setAttribute("langEntity", langEntity);
			request.getRequestDispatcher("/WEB-INF/jsp/lang/show.jsp").forward(request, response);
		}else if(pathParts.length == 2 && pathParts[0].equals("delete") && findLanguage(pathParts[1]) != null){
			//request.setAttribute("langEntity", langEntity);
			request.getRequestDispatcher("/WEB-INF/jsp/lang/delete.jsp").forward(request, response);
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		String action = request.getParameter("submit");
		
		if(action.equals("lang_add")){
			save(request, response);
			
		}else if (action.equals("lang_delete")){
			delete(request, response);
		}else{
			response.sendRedirect(request.getContextPath());
		}
	}
	
	private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Saving language entry in database");
		
		Language language = null;
			
		try{
			language = (Language)db.find(Language.class, request.getParameter("id"));
			/*
			int id;
			
			try{
				id = Integer.parseInt(request.getParameter("id"));
			}catch(NumberFormatException ex){
				id = 0;
			}
			
			language = db.getEm().find(Language.class, id);
			*/
			if(language == null){
				language = new Language();
			}
			
			language.setIdentifier(request.getParameter("identifier"), db)
				.setEnglish(request.getParameter("english"))
				.setFrench(request.getParameter("french"))
				.setSpanish(request.getParameter("spanish"));
			
			if(language.getErrors().size() > 0){
				
				if(language.getId() ==null || language.getId() == 0){
					request.setAttribute("langAddInstance", language);
				}else{
					request.setAttribute("langEditInstance", language);
				}
				doGet(request, response);
				
			}else{
				
				if(language.getId() == null || language.getId() == 0){
					db.insert(db.getEm(), language);
					request.getSession().setAttribute("langAddFormSuccess", "language entity success: The language line has been added succesfully");
					
				}else{
					db.update(db.getEm());
					request.getSession().setAttribute("langEditFormSuccess", "language entity success: The language line has been edited succesfully");
				}
				
				response.sendRedirect(request.getRequestURI());
			}
			
		}catch (DbException ex){
			log.exception(ex);
			language.addError("form", "language entity error: Internal error while persisting in the database");
		}finally{
			db.getEm().clear();
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Deleting language entry from the database");
		
		Language language = null;
		
		try{
			language = (Language)db.find(Language.class, request.getParameter("id"));
			db.remove(language);
			
			request.getSession().setAttribute("lang_delete_success", "The entry from languges has beed deleted successfully");
			response.sendRedirect(request.getServletPath());
			
		}catch(DbException ex){
			log.exception(ex);
			if(language == null) language = new Language();
			language.addError("lang_delete", "Internal error while removing the entry");
			request.setAttribute("langInstance", language);
			doGet(request, response);
		}finally{
			
		}
	}
	
	private Language findLanguage(String idString){
		log.info("Finding language entity. $id" + pathParts[1]);
		
		Language result = null;
		
		try{
			int id = Integer.parseInt(idString);
			result = db.getEm().find(Language.class, id);
			
		}catch (NumberFormatException ex){
		
		}
		
		if(result != null){
			langEntity = result;
		}
		return result;
	}
	
	private int findPage(String page){
		int result = 1;
		
		try{
			result = Integer.parseInt(page);
		}catch (NumberFormatException ex){
			log.warning("The pagination for Languages is not a valid number \n EXCEPTION: " + ex.getMessage());
		}
		
		return result;
	}
}
