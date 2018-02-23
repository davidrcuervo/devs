package com.laetienda.options;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import com.laetienda.db.Db;
import com.laetienda.db.DbManager;

public class OptionFilter implements Filter{

	DbManager dbManager;
	Db db;
	
	public void init(FilterConfig fConfig) throws ServletException{
		
		dbManager = (DbManager)fConfig.getServletContext().getAttribute("dbManager");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)request;
		//HttpServletResponse httpResp = (HttpServletResponse)response;
		db = dbManager.createTransaction();
		
		Vars vars = new Vars(db);
		httpReq.setAttribute("vars", vars);
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		dbManager.closeTransaction(db);
	}
}
