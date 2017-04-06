package com.laetienda.log;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LogFilter implements Filter{
	
	LoggerManager logManager;
	JavaLogger logger;
	
	public void init(FilterConfig fConfig) throws ServletException{
		logManager = (LoggerManager)fConfig.getServletContext().getAttribute("logManager");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)request;
		
		synchronized(this){
			logger = logManager.createJavaLogger();
		}
		
		httpReq.setAttribute("logger", logger);
		
		chain.doFilter(request, response);
	}
	
	public void destroy(){
		logManager.closeJavaLogger(logger);
	}

}
