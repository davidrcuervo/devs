package com.laetienda.lang;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.db.DbManager;
import com.laetienda.db.Db;
import com.laetienda.logger.JavaLogger;

public class LangFilter implements Filter{
	
	private DbManager dbManager;
	private LangManager langManager;
	
	
	public void init(FilterConfig fConfig) throws ServletException{
		
		dbManager = (DbManager)fConfig.getServletContext().getAttribute("dbManager");
		langManager = (LangManager)fConfig.getServletContext().getAttribute("langManager");
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		HttpServletResponse httpResp = (HttpServletResponse)response;
		JavaLogger log = (JavaLogger)httpReq.getAttribute("logger");
		Db db = dbManager.createTransaction();
		
		try{
			Lang lang = new Lang(db, langManager, log);
			httpReq.setAttribute("lang", lang);
			//log.debug("Lang attribute has been set to this request");
		}catch(LangException ex){
			log.critical("It was not poossible to crete lang object");
			log.exception(ex);
			httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		chain.doFilter(request, response);
	}
	
	public void destroy(){

	}
}
