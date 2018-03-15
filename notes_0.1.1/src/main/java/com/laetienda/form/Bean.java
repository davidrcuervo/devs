package com.laetienda.form;

import org.apache.logging.log4j.Logger;

import com.laetienda.entities.AccessList;
import com.laetienda.entities.EntityObject;
import com.laetienda.entities.Group;
import com.laetienda.entities.Language;
import com.laetienda.lang.Lang;
import com.laetienda.mail.MailException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.logging.log4j.LogManager;

public class Bean {
	private final static Logger log = LogManager.getLogger();
	
	private EntityObject entidad;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Lang lang;
	
	public Bean() {
		
	}
	
	public Bean(EntityObject entidad, HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.lang = (Lang)request.getAttribute("lang");
		setEntidad(entidad);
	}

	public EntityObject getEntidad() {
		return entidad;
	}

	public void setEntidad(EntityObject entidad) {
		this.entidad = entidad;
	}
	
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
	
	private String stringInput(String field) {
		String result = new String();
		
		try {
		
		result = "<div class=\"form-group\">\n"
				+ "<label for=\"" + field + "\">" + lang.out(field) + "</label>\n"
				+ "<div class=\"input-group input-group-lg\">\n"
				+ "<span class=\"input-group-addon\">"
				+ "<span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span>\n"
				+ "</span>\n"
				+ "<input type=\"text\" class=\"form-control\" id=\"" + field + "\" name=\"" + field + "\" placeholder=\"" + lang.out(field) + "\" value=\"" + entidad.getClass().getMethod("get" + field.substring(0,1).toUpperCase() + field.substring(1)).invoke(entidad) + "\"/>\n"
				+ "</div>\n";
		
		if(entidad.getErrors().containsKey(field)) {
			result += "<div class=\"text-danger text-center\">\n";
			
			for(String error : entidad.getErrors().get(field)) {
				result += "<small>" + lang.out(error) + "</small>";
			}
			result += "</div>\n";
		}

		result += "</div>\n";
		}catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			
		}
		return result;
	}
	
	private String getJsp(String jsp) throws FormException {
		String result = new String();
		
		try {
			HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
	            private final StringWriter sw = new StringWriter();
	
	            @Override
	            public PrintWriter getWriter() throws IOException {
	                return new PrintWriter(sw);
	            }
	
	            @Override
	            public String toString() {
	                return sw.toString();
	            }
	        };
	        
			request.getRequestDispatcher(jsp).include(request, responseWrapper);
		
	        result = responseWrapper.toString();
	        
		}catch(IOException ex) {
			throw new FormException("Failed to get text from jsp file", ex);
		}catch (ServletException ex) {
			throw new FormException("Failed to get text from jsp file", ex);
		}
		
		return result;
	}
	
	public String getSmallTest() {
		return new String("Small test");
	}
	
	public static void main(String[] args) {
		Bean bean = new Bean();
		Group group = new Group();
		bean.setEntidad(group);
		System.out.println(bean.getPrint());
		
	}
}
