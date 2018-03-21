package com.laetienda.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.laetienda.acl.Acl;
import com.laetienda.entities.EntityObject;
import com.laetienda.entities.Form;
import com.laetienda.entities.Input;
import com.laetienda.entities.Objeto;
import com.laetienda.entities.User;
import com.laetienda.lang.Lang;


public class Bean {
	private final static Logger log = LogManager.getLogger();
	
	private Form form;
//	private EntityObject entidad;
	private Objeto entidad;
	private HttpServletRequest request;
	private Lang lang;
	private String action;
	
	public Bean() {
		
	}
	
	public Bean(Form form, HttpServletRequest request) throws FormException {
		this.request = request;
		this.lang = (Lang)request.getAttribute("lang");
		this.form = form;
		this.entidad = setEntidad(form);
	}
	
	private Objeto setEntidad(Form form) throws FormException {
		Objeto result = null;
		
		Class<?> clazz;
		try {
			clazz = Class.forName(form.getClase());
		
			Object temp = clazz.newInstance();
			
			if(temp instanceof Objeto) {
				result = (Objeto)temp;
			}else {
				throw new FormException("Object Name in URL does not correspond to a valid object");
			}
		
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new FormException("Failed to find a valid class to process the form", e);
		}
		
		return result;
	}
	
	public String getPrint() {
		
		String name = form.getName();
		String submitText = lang.out(action.toUpperCase() + " " + name.toUpperCase());
		
		String result = "<form method=\"post\">";
		
		for(Input input : form.getInputs()) {
				
			String type = input.getType().toLowerCase();
				
			switch(type) {
				case "string":
					result += getHtmlStringInput(input);
					break;
				
				default:
					result += "<div>This type of input is not defined</div>";
					break;
			}
		}
		
		result += getPermissionsInputs();
		
		result += "<button type=\"submit\" name=\"" + name + "\" value=\"signup\" class=\"btn btn-primary btn-block\">" + submitText + "</button>";
		
		if(entidad.getErrors().containsKey(form.getName())) {
			result += "<div class=\"text-danger text-center\">\n";
			
			for(String error : entidad.getErrors().get(form.getName())) {
				result += "<small>" + lang.out(error) + "</small>";
			}
			
			result += "</div>\n";
		}
		
		result += "</form>";
			
		return result;
	}
	
	/*
	public String getPrint() {
		String result = "<form method=\"post\">";
		
		try {
			Method[] metodos = entidad.getClass().getMethods();
			
			
			for(Method metodo : metodos) {
				
				Parameter[] parameters = metodo.getParameters();
				
				if(parameters.length == 1) {
					
					String fieldName = metodo.getName().substring(3);
					
					
					if(parameters[0].getType().getName().equals(String.class.getName())) {
						log.debug("Metodo: " + fieldName + "\tclass: " + metodo.getReturnType().getName() + "\tNo. of paramenters: " + parameters.length + "\tparameter type: " + parameters[0].getType().getName());
						result += stringInput(fieldName);
						
					}else if(parameters[0].getType().getName().equals(AccessList.class.getName())){
						log.debug("Metodo: " + fieldName + "\tclass: " + metodo.getReturnType().getName() + "\tNo. of paramenters: " + parameters.length + "\tparameter type: " + parameters[0].getType().getName());
					}
				}
			}
			
			result += "</form>";
		}catch(Exception ex) {
			result = "<h3 class=\"text-ceter\">Failed to build form</h3>";
			log.error("Failed to create form", ex);
		}
		
		return result;
	}
	*/
	
	private String getHtmlStringInput(Input input) {
		String result = new String();
		String id = "id_form_" + form.getName() + "_input_" + input.getName();
		String value = input.isValue() ? getSafeValue(request.getParameter(input.getName())) : "";
		String placeholder = input.getPlaceholder() != null && !input.getPlaceholder().isEmpty() ? lang.out(input.getPlaceholder()) : "";
		String label = lang.out(input.getLabel());
		boolean isCorrect = entidad.getErrors().containsKey(input.getName()) ? false : true;
		String idErrorStatus = "id_form_" + form.getName() + "_inputError2Status_" + input.getName();
		
		result = "<div class=\"form-group" + (isCorrect ? "" : " has-error has-feedback")  + "\">\n"
				+ "<label class=\"control-label\" for=\"" + id + "\">" + label + "</label>\n";
		
		if(input.getGlyphicon() != null && !input.getGlyphicon().isEmpty()) {
			result += "<div class=\"input-group input-group-lg\">\n"
					+ "<span class=\"input-group-addon\">"
					+ "<span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span>\n"
					+ "</span>\n";
		}
		
		result += "<input type=\"text\" "
				+ "class=\"form-control\" "
				+ "id=\"" + id + "\" "
				+ "name=\"" + input.getName() + "\" "
				+ "placeholder=\"" + placeholder + "\" "
				+ (isCorrect ? "" : "aria-describedby=\"" + idErrorStatus + "\" ") 
				+ "value=\"" + value + "\"/>\n";
				
		result += isCorrect ? "" : "<span class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span>\n"
								+ "<span id=\"" + idErrorStatus + "\" class=\"sr-only\">(error)</span>\n";
		
		result += input.getGlyphicon() != null && !input.getGlyphicon().isEmpty() ? "</div>\n" : "";
		
		if(!isCorrect) {
			result += "<div class=\"text-danger text-center\">\n";
			
			for(String error : entidad.getErrors().get(input.getName())) {
				result += "<small>" + lang.out(error) + "</small>";
			}
			result += "</div>\n";
		}

		result += "</div>\n";

		return result;
	}
	
	private String getPermissionsInputs() {
		String result = new String();
		String id = "id_form_" + form.getName() + "_input_permissions_owner";
		String name = "owner";
		String label = lang.out("Set Owner") + ":";
		User user = (User)request.getSession().getAttribute("sessionUser");
		Acl acl = (Acl)request.getAttribute("acl");
		//Objeto objeto;// = action == "create" ? this.form : (action == "edit" ? (Objeto)this.entidad : null);
		List<User> editors = new ArrayList<User>();
		
		result = "<div class=\"form-group\">\n"
			+ "<label for=\"" + id + "\">" + label + "</label>\n"
			+ "<select id=\"" + id +  "\" name=\"" + name + "\" class=\"form-control\">\n";
		
		
		
		if(user == null) {
			log.debug("No session user found.");
			result += "<option value=\"none\" selected>" + lang.out("Select an option") + "</option>\n";
		}else if(action.equals("create")) {
			result += "<option value=\"none\">" + lang.out("Select an option") + "</option>\n";
			result += "<option value=\"" + user.getId() + "\" selected>" + user.getFullName() + "</option>\n";
			//result += "<option value=\"" + form.getOwner().getId() + "\">" + form.getOwner().getFullName() + "</option>\n";
			editors = acl.findUsersInAcl(form.getCanCreateAcl());
		}else if(action.equals("edit")){
			result += "<option value=\"" + entidad.getOwner().getId() + "\" selected>" + entidad.getOwner().getFullName() + "</option>\n";
			result += "<option value=\"" + user.getId() + "\">" + user.getFullName() + "</option>\n";
			editors = acl.findUsersInAcl(entidad.getWrite());
		}
		
		
		for(User usuario : editors) {
			/*if(usuario.getId() == user.getId() || usuario.getId() == objeto.getOwner().getId()) {
				//Nothing to do. It does want to print the option twice
			}else {*/
				result += "<option value=\"" + usuario.getId() + "\">" + usuario.getFullName() + "</option>\n";
			//}
		}
		
		
		result += "</select>\n";
		result += "</div>\n";
		return result;
	}
	
	private String getSafeValue(String value) {
		
		String result = new String();
		
		if(value != null && !value.isEmpty()) {
			result = value.replaceAll("<", "&lt;");
			result = value.replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("'", "&#039;").replaceAll("\"", "&#034;");
		}
		return result;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public static void main(String[] args) {
		log.info("Hello Form Bean World!!!");
	}
}
