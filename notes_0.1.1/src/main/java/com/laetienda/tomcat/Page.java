package com.laetienda.tomcat;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class Page {
	
	private String url;
	private String rootUrl;
	private String urlWithPattern;
	private String[] allpathParts;
	private List<String> styles;
	private List<String> scripts;
	private String variables;
	
	public Page(HttpServletRequest request){
		allpathParts = (String[])request.getAttribute("allpathParts");
		rootUrl = buildRootUrl(request);
		url = buildUrl(request);
		urlWithPattern = buildUrlWithPatter(request);
		variables = new String();
	}
	
	private String buildRootUrl(HttpServletRequest request){
		
		int port = request.getServerPort();
		
		String result = request.getHeader("x-forwarded-proto");
		
		if(result == null){
			result = request.getScheme();
		}
		
		result += "://" + request.getServerName();
		
		if(!(port == 80 || port == 443)){
			result += ":" + Integer.toString(port);
		}
		
		return result;
	}
	
	private String buildUrl(HttpServletRequest request){
		
		String result = rootUrl;
		
		if(allpathParts != null)
			for(int c=0; c < allpathParts.length; c++){
				result += "/" + allpathParts[c];
			}
		
		return result;
	}
	
	private String buildUrlWithPatter(HttpServletRequest request){
		
		return rootUrl + request.getServletPath();
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
	
	public String getUrlWithPattern(){
		return urlWithPattern;
	}
	
	/**
	 * It takes the parameter and encode the string antxyto be used on the url of a web
	 * @param url String that will be encoded
	 * @exception IOException url can not be empty or null
	 * @return String returns the string encoded
	 */
	public String getEncodeUrl(String url) throws IOException{
		
		if(url == null || url.isEmpty()){
			throw new IOException("url parameter is null or empty");
		}
		
		return URLEncoder.encode(url, "utf-8");
	}
	
	/**
	 * It decodes the encoded String 
	 * @param encodedUrl String that will be dencoded
	 * @exception IOException encodedUrl can not be empty or null
	 * @return String returns the string dencoded
	 */
	public String getDecoedeUrl(String encodedUrl) throws IOException{
		
		if(encodedUrl == null || encodedUrl.isEmpty()){
			throw new IOException("encodedUrl parameter is null or empty");
		}
		
		return URLDecoder.decode(encodedUrl, "utf-8");
	}
	
	public String addVar(String key, String value){
		String result = "?";
		
		if(variables.indexOf('?') > -1){
			result = "&";
		}
		
		result += key + "=" + value;
		
		return result;
	}
	
}
