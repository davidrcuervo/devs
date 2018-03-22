package com.laetienda.dap;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DapFilter implements Filter {
	private final static Logger log = LogManager.getLogger(DapFilter.class);
	
	private DapManager dapManager;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		log.info("Running doFilter");
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		Dap dap = null;
		
		try {
			dap = dapManager.createDap();
			request.setAttribute("dap", dap);
			arg2.doFilter(arg0, arg1);
		} catch (DapException ex) {
			log.error("Failed to create dap object.", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			dapManager.closeConnection(dap);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		dapManager = (DapManager)arg0.getServletContext().getAttribute("dapManager");

	}

	public static void main(String[] args) {
		log.info("Hello " + DapFilter.class.getName() + " World!!");

	}

}
