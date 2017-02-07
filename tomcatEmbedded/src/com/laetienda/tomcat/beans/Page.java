package com.laetienda.tomcat.beans;

import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class Page {
	
	private String url;
	private String rootUrl;
	private String[] pathParts;
	private List<String> styles;
	private List<String> scripts;
	
	public Page(HttpServletRequest request){
		pathParts = findPathParts(request);
		rootUrl = buildRootUrl(request);
		url = buildUrl(request);
	}
	
	private String[] findPathParts(HttpServletRequest request){
		
		String[] result = (String[])request.getAttribute("pathParts");
		
		System.out.println(request.getRequestURI());
		
		return result;
	}
	
	private String buildRootUrl(HttpServletRequest request){
		
		int port = request.getServerPort();
		String result = request.getScheme() + "://" + request.getServerName();
		
		if(!(port == 80 || port == 443)){
			result += ":" + Integer.toString(port);
		}
		
		return result;
	}
	
	private String buildUrl(HttpServletRequest request){
		
		String result = rootUrl;
		
		if(pathParts != null)
			for(int c=1; c < pathParts.length; c++){
				result += "/" + pathParts[c];
			}
		
		System.out.println(result);
		
		return result;
	}
	
	public void addStyle(String style){
		if(styles == null)
			styles = new ArrayList<String>();
		
		if(!styles.contains(style))
			styles.add(style);
	}
	
	public void addScript(String script){
		
		if(scripts == null)
			scripts = new ArrayList<String>();
		
		if(!scripts.contains(script))
			scripts.add(script);
	}
	
	public String encodePhrase(String string){
		String result = "";
		
		return result;
	}

	public String getUrl() {
		return url;
	}

	public List<String> getStyles() {
		return styles;
	}

	public List<String> getScripts() {
		return scripts;
	}

	/**
	 * @return the rootUrl
	 */
	public String getRootUrl() {
		return rootUrl;
	}
}
