package com.laetienda.tomcat;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class Page {
	private final String strKey = new String("vS8WkS8uZcHZufyWENUWPgX8FPugDBH8");
	private final static Logger log4j = Logger.getLogger(Page.class);
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
	 * From: http://www.adeveloperdiary.com/java/how-to-easily-encrypt-and-decrypt-text-in-java/
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
	 * From: http://www.adeveloperdiary.com/java/how-to-easily-encrypt-and-decrypt-text-in-java/
	 * @param encodedUrl String that will be decoded
	 * @exception IOException encodedUrl can not be empty or null
	 * @return String returns the string decoded
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
	
	/**
	 * 
	 * @param strClearText
	 * @param strKey
	 * @return encrypted text and encoded with Base64.encodeBase64URLSafeString, so it can be used in URLs
	 * @throws IOException
	 */
	public String simpleEncrypt(String strClearText){
		String result = new String();
		
		SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes(), "Blowfish");
		
		try {
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(strClearText.getBytes());
			result = Base64.encodeBase64URLSafeString(encrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			log4j.error("Failed to encode phrase for URL $phrase: " + strClearText ,ex);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param strEncrypted
	 * @param strKey
	 * @return
	 * @throws IOException
	 */
	public String simpleDecrypt(String strEncrypted){
		String result = new String();
		
		SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
		Cipher cipher;
		
		try {
			cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			
			byte[] decrypted=cipher.doFinal(Base64.decodeBase64(strEncrypted));
			result = new String(decrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			log4j.error("Failed to decode phrase from URL" ,ex);
		}
		
		return result;
	}
}
