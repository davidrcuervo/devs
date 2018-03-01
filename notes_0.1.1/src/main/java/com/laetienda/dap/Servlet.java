package com.laetienda.dap;

import java.io.IOException;

//import javax.persistence.NoResultException;
//import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.db.Db;
import com.laetienda.db.DbException;
import com.laetienda.db.DbManager;
import com.laetienda.entities.Option;
import com.laetienda.entities.User;
import com.laetienda.mail.Email;
import com.laetienda.mail.MailException;
import com.laetienda.mail.MailManager;
import com.laetienda.tomcat.Page;

import org.apache.log4j.Logger;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger log4j = Logger.getLogger(Servlet.class);
	private String[] pathParts;
	private DbManager dbManager;
	private DapManager dapManager;
	private MailManager mailManager;
	
	public Servlet(){
		super();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException{
		log4j.debug("Initializing DAP servlet");
		
		dbManager = (DbManager)config.getServletContext().getAttribute("dbManager");
		dapManager = (DapManager)config.getServletContext().getAttribute("dapManager");
		mailManager = (MailManager)config.getServletContext().getAttribute("mailManager");
	}
	
	private void build(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		pathParts = (String[])request.getAttribute("pathParts");

	}
	
	@Override
	public void destroy(){
		log4j.debug("Closing/destroying DAP Servlet");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		switch(pathParts[0]){
			
			//https:///<server-name>:<port>/<context-path>
			case "":
				response.sendRedirect(request.getServletPath() + "/login");
				break;
		
			//https:///<server-name>:<port>/<context-path>/login
			case "signup":
			case "password":
			case "login":
				request.getRequestDispatcher("/WEB-INF/jsp/dap/container.jsp").forward(request, response);
				break;
		
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		switch (request.getParameter("submit")){
		
			case "signup":
				signup(request, response);
				break;
				
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
		}
	}
	
	private void signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Db db = null;
		Dap dap = null;
		Email email = null;
		User user = new User();
		request.setAttribute("user", user);
		Page page = (Page)request.getAttribute("page");
		
		try {
			db = dbManager.createTransaction();
			Option status = db.findOption("user status", "registered");
			Option language = db.findOption("languages", request.getParameter("language"));
			Integer uid = db.getNextUid();
			user.setUid(uid);
			user.setCn(request.getParameter("cn"));
			user.setSn(request.getParameter("sn")); 
			user.setEmail(request.getParameter("email"), db);
			user.setPassword(request.getParameter("password"), request.getParameter("password_confirm"));
			user.setStatus(status);
			user.setLanguage(language);
			
			if(user.getErrors().size() > 0) {
				log4j.info("Parameters included in the form are not valid");
			}else {
				db.insert(user);
				dap = dapManager.createDap();
				dap.insertUser(user);
				email = mailManager.createEmail();
				email.setText("/WEB-INF/jsp/email/signup.jsp", request, response);
				email.send(user.getEmail(), "La-eTienda email and password validation");
			}
			
		}catch(DbException ex) {
			log4j.error("Failed to persist user in database", ex.getRootParent());
			user.addError("user", "Internal error. There was an error while saving into the database");
		} catch (DapException e) {
			user.addError("user", "Internal error. Failed to add user");
			log4j.error("Failed to add user to ldap directory", e.getRootParent());
			try {
				//User temp = db.getEm().createNamedQuery("User.findByEmail", User.class).setParameter("email", user.getEmail()).getSingleResult();
				db.remove(user);
			/*}catch (NoResultException | NonUniqueResultException e1) {
				log4j.fatal("User didn't save in ldap directory and it is still in database", e1);*/
			} catch (DbException e2) {
				log4j.fatal("Failed to remove user from DB that was not able to be saved in ldap directory", e2.getRootParent());
			}
		} catch(MailException ex) {
			user.addError("user", "Internal error. Failed to add user");
			log4j.error("Failed to send confirmation email", ex.getRootParent());			
		}finally {
			dapManager.closeConnection(dap);
			dbManager.closeTransaction(db);
			
			if(user.getErrors().size() > 0) {
				log4j.info("FAILED to add user to the website");
				doGet(request, response);
			}else {
				log4j.info("User has been added SUCCESFULLY");
				request.removeAttribute("user");
				request.getSession().setAttribute("ThankyouToken", "signup");
				response.sendRedirect(page.getRootUrl() + "/thankyou/signup");	
			}
		}
	}
}
