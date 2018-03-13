package com.laetienda.dap;

import java.io.IOException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final static Logger log4j = LogManager.getLogger(Servlet.class);
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
		Page page = (Page)request.getAttribute("page");
		
		switch(pathParts[0]){
			
			//https:///<server-name>:<port>/<context-path>
			case "":
				response.sendRedirect(request.getServletPath() + "/login");
				break;
			
			//https://<server-name>:<port>/<context-path>/validate/:enryptedUsername
			case "validate":
				doGetValidate(request, response);
				break;
		
			//https:///<server-name>:<port>/<context-path>/login
			case "signup":
			case "password":
			case "login":
				request.getRequestDispatcher("/WEB-INF/jsp/dap/container.jsp").forward(request, response);
				break;
			
			//https:///<server-name>:<port>/<context-path>/logout	
			case "logout":
				if(request.getSession().getAttribute("sessionUser") == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}else {
					request.getSession().removeAttribute("sessionUser");
					request.getSession().setMaxInactiveInterval(30 * 60);
					response.sendRedirect(page.getRootUrl() + "/home");
				}
				
				break;
			
			//https://<server-name>:<port>/<context-path>/recoverpassword/:enryptedUsernameAndEmail
			//enryptedUsername = user.getUid() + ":" + user.getEmail()
			case "recoverpassword":
				doGetPasswordRecover(request, response);
				break;
		
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		build(request, response);
		
		if(request.getSession().getAttribute("sessionUser") == null) {
			switch (request.getParameter("submit")){
			
				case "signup":
					signup(request, response);
					break;
					
				case "validate":
					doPostValidate(request, response);
					break;
					
				case "login":
					login(request, response);
					break;
					
				case "password":
					password(request, response);
					break;
					
				case "passwordrecover":
					doGetPasswordRecover(request, response);
					break;
					
				default:
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					break;
			}
		}else {
			log4j.fatal("SECURITY WARNING. Someone is trying to do something nasty.");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
			//Integer uid = db.getNextUid();
			user.setUid(request.getParameter("uid"), db);
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
				email.send(user.getEmail(), (String)request.getAttribute("emailSubject"));
			}
			
		}catch(DbException ex) {
			log4j.error("Failed to persist user in database", ex.getRootParent());
			user.addError("user", "Internal error. There was an error while saving into the database");
		} catch (DapException e) {
			user.addError("user", "Internal error. Failed to add user");
			log4j.error("Failed to add user to ldap directory", e.getRootParent());
			
			try {
				db.remove(user);
			} catch (DbException e2) {
				log4j.fatal("Failed to remove user from DB that was not able to be saved in ldap directory", e2.getRootParent());
			}
		} catch(MailException ex) {
			user.addError("user", "Internal error. Failed to add user");
			log4j.error("Failed to send confirmation email", ex.getRootParent());	
			
			try {
				db.remove(user);
				dap.deleteUser(user);
			} catch (DbException e) {
				log4j.fatal("Failed to remove user from DB that was not able to be saved in ldap directory", e.getRootParent());
			}
			
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
	
	//https://<server-name>:<port>/<context-path>/validate/:enryptedUsername
	private void doGetValidate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Db db = dbManager.createTransaction();
		Page page = (Page)request.getAttribute("page");
		User user;
		
		log4j.debug("$pathParts[1]: " + pathParts[1]);
		log4j.debug("Username: " + page.simpleDecrypt(pathParts[1]));
		
		try {
			 
			if(request.getAttribute("user") == null) {
				
				user = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", page.simpleDecrypt(pathParts[1])).getSingleResult();
				
				if(user != null && user.getStatus().getName().equals(db.findOption("user status", "registered").getName())) {
					log4j.debug("User has been found from encoded url. $username: " + user.getUid());
				}else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			}else {
				user = (User)request.getAttribute("user");
			}
			
			request.setAttribute("user", user);
			request.getRequestDispatcher("/WEB-INF/jsp/dap/validate.jsp").forward(request, response);
			
		}catch(DbException | NoResultException | NonUniqueResultException ex) {
			log4j.debug("Failed to serve validate page", ex);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}finally {
			dbManager.closeTransaction(db);
		}
	}
	
	private void doPostValidate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Db db = dbManager.createTransaction();
		Dap dap = null;
		User user = new User();
		String username = request.getParameter("uid");
		Page page = (Page)request.getAttribute("page");
		
		try {
			user = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", username).getSingleResult();
			dap = dapManager.createDap();
			
			if(username.equals(page.simpleDecrypt(pathParts[1])) && username.equals(user.getUid())) {
					
				if(dap.checkPassword(username, request.getParameter("password"))) {
					Option option = db.findOption("user status", "active");
					user.setStatus(option);
					db.update();
				}else {
					user.addError("user", "Wrong password");
				}
			}else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			
		} catch(NoResultException | NonUniqueResultException ex) {
			user.addError("user", "The username does not exist.");
		} catch (DapException | DbException ex) {
			user.addError("user", "Internal error while verifying password");
			log4j.error("Failed to validate password", ex.getRootParent());
			
		}finally {
			dapManager.closeConnection(dap);
			dbManager.closeTransaction(db);
			
			if(user.getErrors().size() > 0) {
				log4j.info("Failed to validate password of user \"" + username + "\"");
				request.setAttribute("user", user);
				doGet(request, response);
			}else {
				log4j.info("Password of user \"" + username + "\" has been validated succesfully");
				request.getSession().setAttribute("ThankyouToken", "validate");
				response.sendRedirect(page.getRootUrl() + "/thankyou/validate");
			}	
		}	
	}
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Db db = dbManager.createTransaction();
		String input = request.getParameter("username");
		User user = new User();
		user.setUid(input, db);
		Dap dap = null;
		Page page = (Page)request.getAttribute("page");
				
		try {
			dap = dapManager.createDap();
			user = db.getEm().createNamedQuery("User.findByUidOrEmail", User.class).setParameter("input", input).getSingleResult();
			Option active = db.findOption("user status", "active");
			
			if(!user.getStatus().getName().equals(active.getName())) {
				user.addError("user", "User has not verifed her/his email");
			}else if(!dap.checkPassword(user.getUid(), request.getParameter("password"))){
				user.addError("password", "Wrong password");
			}
			
		}catch(NoResultException | NonUniqueResultException ex) {
			user.addError("uid", "This username or email address has not been registered yet.");
		}catch(DapException | DbException ex) {
			user.addError("user", "Internal error while logining user");
			log4j.error("Failed to login user", ex.getRootParent());
		}finally {
			dbManager.closeTransaction(db);
			dapManager.closeConnection(dap);
			
			if(user.getErrors().size() > 0) {
				log4j.info("User \"" + input + "\" failed to login");
				request.setAttribute("user", user);
				doGet(request, response);
			}else {
				log4j.info("User \"" + input + "\" has logged in successfully");
				request.getSession().setAttribute("sessionUser", user);
				request.getSession().setMaxInactiveInterval(-1);
				//TODO check session previous page then redirect to it. So far, it will redirect to home page
				response.sendRedirect(page.getRootUrl() + "/home");
			}
		}
	}
	
	private void password(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Db db = dbManager.createTransaction();
		String email = request.getParameter("email");
		User user = new User();
		user.setUid(email, db);
		Page page = (Page)request.getAttribute("page");
				
		try {
			user = db.getEm().createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();
			request.setAttribute("user", user);
			Email mailer = mailManager.createEmail();
			mailer.setText("/WEB-INF/jsp/email/passwordRecovery.jsp", request, response);
			mailer.send(user.getEmail(), (String)request.getAttribute("emailSubject"));
			
		}catch(NoResultException | NonUniqueResultException ex) {
			user.addError("email", "This email address has not been registered yet.");
		}catch(MailException ex) {
			user.addError("user", "Internal error while reseting password $email: " + email);
			log4j.error("Failed to reset password", ex.getRootParent());
		}finally {
			dbManager.closeTransaction(db);
			
			if(user.getErrors().size() > 0) {
				log4j.info("Failed to process password recovery form: $email" + email);
				doGet(request, response);
			}else {
				request.removeAttribute("user");
				request.getSession().setAttribute("ThankyouToken", "password");
				response.sendRedirect(page.getLinkFromRootUrl("/thankyou/password"));
			}
		}
	}

	//https://<server-name>:<port>/<context-path>/recoverpassword/:enryptedUsernameAndEmail
	//enryptedUsername = user.getUid() + ":" + user.getEmail()
	private void doGetPasswordRecover(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Page page = (Page)request.getAttribute("page");
		String session = request.getParameter("submit");
		String[] usernameAndEmail = page.simpleDecrypt(pathParts[1]).split(":");
		Db db = dbManager.createTransaction();
		
		log4j.debug("$pathParts[1]: " + pathParts[1]);
		log4j.debug("$usernameAndEmail: " + page.simpleDecrypt(pathParts[1]));
		log4j.debug("$Username: " + usernameAndEmail[0]);
		log4j.debug("$eMail: " + usernameAndEmail[1]);
		
		try {
			User user = db.getEm().createNamedQuery("User.findByUid", User.class).setParameter("uid", usernameAndEmail[0]).getSingleResult();
			
			if(user.getEmail().equals(usernameAndEmail[1])) {
				
				if(session != null && session.equals("passwordrecover")) {
					doPostPasswordRecover(db, user, request, response);
					
				}else {
					request.getRequestDispatcher("/WEB-INF/jsp/dap/passwordRecover.jsp").forward(request, response);
				}
				
			}else {
				log4j.info("Invalid url recovery link. User email in database is not the same that email in link");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			
		}catch(NoResultException | NonUniqueResultException ex) {
			log4j.info("Invalid url recovery link. Username does not exist in database");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}finally {
			dbManager.closeTransaction(db);
		}
	}
	
	private void doPostPasswordRecover(Db db, User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Page page = (Page)request.getAttribute("page");
		user.setPassword(request.getParameter("password"), request.getParameter("password_confirm"));
		Dap dap = null;
		Email email = null;
		
		try {
			if(user.getErrors().size() > 0) {
				log4j.debug("Password is not valid then it will not saved in LDAP");
			}else {
				dap = dapManager.createDap();
				dap.changeUserPassword(user);
				email = mailManager.createEmail();
				email.setText("/WEB-INF/jsp/email/passwordReset.jsp", request, response);
				email.send(user.getEmail(), (String)request.getAttribute("emailSubject"));
			}
			
		}catch(DapException | MailException ex) {
			log4j.error("Failed to update password in LDAP", ex.getRootParent());
			user.addError("user", "Internal error. There was an error while saving into the database");
		}finally {
			dapManager.closeConnection(dap);
			
			if(user.getErrors().size() > 0) {
				request.setAttribute("user", user);
				request.getRequestDispatcher("/WEB-INF/jsp/dap/passwordRecover.jsp").forward(request, response);
			}else {
				request.getSession().setAttribute("ThankyouToken", "recoverpassword");
				response.sendRedirect(page.getRootUrl() + "/thankyou/recoverpassword");
			}
		}
	}
}
