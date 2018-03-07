package com.laetienda.tomcat;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class PathParts implements Filter{
	
	final static Logger log4j = Logger.getLogger(PathParts.class);
	
	public void init(FilterConfig fConfig) throws ServletException{
	
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		String uri = httpReq.getRequestURI();
		
		String urlPattern = httpReq.getServletPath();
		
		String path = new String();
		
		int index = uri.indexOf('&');
		
		if(index < 0){
			index = uri.length();
		}
		
		try{
			path = uri.substring(urlPattern.length() + 1, index);
		}catch (IndexOutOfBoundsException ex){
			
		}
		
		String[] pathParts = path.split("/");
		String[] allpathParts = uri.substring(1, index).split("/");
		
		httpReq.setAttribute("pathParts", pathParts);
		httpReq.setAttribute("allpathParts", allpathParts);
		chain.doFilter(request, response);
		
		/*
		 * DISABLE BLOCK BELOW, ONLY ENABLE IT TO TROUBLESHOOT PATH PARTS MODULE
		 */
		
		log4j.debug("uri: " + uri);
		log4j.debug("urlPattern: " + urlPattern);
		log4j.debug("path: " + path);
		
		log4j.debug("pathParts.length: " + pathParts.length);
		log4j.debug("pathParts[0].length: " + pathParts[0].length());
		
		for(int c=0; c < pathParts.length; c++){
			log4j.debug("pathParts[" + c + "]: " + pathParts[c]);
		}
		/*
		log4j.debug("allpathParts.length: " + allpathParts.length);
		log4j.debug("allpathParts[0].length: " + allpathParts[0].length());
		
		for(int c=0; c < allpathParts.length; c++){
			log4j.debug("allpathParts[" + c + "]: " + allpathParts[c]);
		}
		*/
	}
	
	public void destroy(){
		
	}
}
