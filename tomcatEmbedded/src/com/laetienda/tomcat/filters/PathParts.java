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
		System.out.println("uri: " + uri);
		System.out.println("urlPattern: " + urlPattern);
		System.out.println("path: " + path);
		
		System.out.println("pathParts.length: " + pathParts.length);
		System.out.println("pathParts[0].length: " + pathParts[0].length());
		
		for(int c=0; c < pathParts.length; c++){
			System.out.println("pathParts[" + c + "]: " + pathParts[c]);
		}
		
		System.out.println("allpathParts.length: " + allpathParts.length);
		System.out.println("allpathParts[0].length: " + allpathParts[0].length());
		
		for(int c=0; c < allpathParts.length; c++){
			System.out.println("allpathParts[" + c + "]: " + allpathParts[c]);
		}
		*/
	}
	
	public void destroy(){
		
	}
}
