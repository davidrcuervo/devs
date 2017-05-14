package com.laetienda.tomcat;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class PageFilter implements Filter{
	
	public void init(FilterConfig fConfig) throws ServletException{
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		Page page = new Page(httpReq);
		
		httpReq.setAttribute("page", page);
		
		chain.doFilter(request, response);
	}
	
	public void destroy(){
		
	}
}
