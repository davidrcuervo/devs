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
		String path = httpReq.getRequestURI();
		
		String[] pathParts = path.split("/");
		
		httpReq.setAttribute("pathParts", pathParts);
		chain.doFilter(request, response);
	}
	
	public void destroy(){
		
	}
}
