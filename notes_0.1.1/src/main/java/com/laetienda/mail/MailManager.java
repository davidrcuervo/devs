package com.laetienda.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.laetienda.app.Aes;


public class MailManager {
	private final static Logger log4j = Logger.getLogger(MailManager.class);
	
	private Properties settings;
	private String password;
	
	public MailManager(File directory) throws MailException {
		settings = loadSettings(directory);
		password = getPassword();
	}
	
	public synchronized Email createEmail() throws MailException {
		return new Email(settings, password);
	}
	
	private Properties loadSettings(File directory) throws MailException {
		Properties defaults = new Properties();
		defaults.put("mail.smtp.auth", "true");
		defaults.put("mail.smtp.starttls.enable", "true");
		
		Properties result = new Properties(defaults);
		String path = directory.getAbsolutePath() + File.separator + "etc" + File.separator + "mail.conf.xml";
		
		try {
			FileInputStream conf = new FileInputStream(new File(path));
			result.loadFromXML(conf);
		} catch (IOException ex) {
			throw new MailException("Failed to get settings from mail", ex);
		}
		
		return result;
	}

	public String getPassword() throws MailException {
		Aes aes = new Aes();
		String result = new String();
		try {
			result = aes.decrypt(settings.getProperty("password"), settings.getProperty("username"));
		} catch (Exception ex) {
			throw new MailException("Failed to get find password", ex);
		}
		log4j.debug("$password: " + result);
		return result;
	}
	
	public static void main(String[] args){
		
		File directory = new File("/Users/davidrcuervo/git/devs/web"); //mac
		//File directory = new File("C:/Users/i849921/git/devs/web"); //SAP lenovo
		
		try {
			MailManager mailManager = new MailManager(directory);
			Email email = mailManager.createEmail();
			email.setText("Email testing").send("davidrcuervo@outlook.com", "Hello JavaMail world!");
			log4j.info("Email has been sent succesfully");
		} catch (MailException ex) {
			log4j.error("Failed to send email", ex.getRootParent());
		}
		
		
	}
}
