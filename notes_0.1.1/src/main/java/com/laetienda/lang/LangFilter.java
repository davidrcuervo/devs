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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LangFilter implements Filter{
	private final static Logger log4j = LogManager.getLogger(LangFilter.class);
	
	private DbManager dbManager;
	private LangManager langManager;
	private Db db;
	
	public void init(FilterConfig fConfig) throws ServletException{
		
		dbManager = (DbManager)fConfig.getServletContext().getAttribute("dbManager");
		langManager = (LangManager)fConfig.getServletContext().getAttribute("langManager");
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		HttpServletResponse httpResp = (HttpServletResponse)response;
		db = dbManager.createTransaction();
		
		try{
			Lang lang = new Lang(db, langManager);
			httpReq.setAttribute("lang", lang);
			//log.debug("Lang attribute has been set to this request");
		}catch(LangException ex){
			log4j.fatal("It was not poossible to crete lang object", ex);
			httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		chain.doFilter(request, response);
	}
	
	public void destroy(){
		dbManager.closeTransaction(db);

	}
}
