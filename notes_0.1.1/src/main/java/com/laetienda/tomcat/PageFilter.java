package com.laetienda.tomcat;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageFilter implements Filter{
	
	public final Logger log = LogManager.getLogger(PageFilter.class);
	
	public void init(FilterConfig fConfig) throws ServletException{
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("Running doFilter from " + PageFilter.class.getName());
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		HttpServletResponse httpRes = (HttpServletResponse)response;
		Page page = new Page(httpReq, httpRes);
		
		httpReq.setAttribute("page", page);
		
		chain.doFilter(request, response);
	}
	
	public void destroy(){
		
	}
}
