package com.laetienda.tomcat.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class PathParts implements Filter{
	
	public void init(FilterConfig fConfig) throws ServletException{
	
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		String uri = httpReq.getRequestURI();
		
		String urlPattern = httpReq.getServletPath();
		
		String path = new String();
		
		try{
			path = uri.substring(urlPattern.length() + 1);
		}catch (IndexOutOfBoundsException ex){
			
		}
		
		String[] pathParts = path.split("/");
		
		httpReq.setAttribute("pathParts", pathParts);
		chain.doFilter(request, response);
		
		/*
		System.out.println("uri: " + uri);
		System.out.println("urlPattern: " + urlPattern);
		System.out.println("path: " + path);
		
		System.out.println("pathParts.length: " + pathParts.length);
		System.out.println("pathParts[0].length: " + pathParts[0].length());
		
		for(int c=0; c < pathParts.length; c++){
			System.out.println("pathParts[" + c + "]: " + pathParts[c]);
		}
		*/
	}
	
	public void destroy(){
		
	}
}
