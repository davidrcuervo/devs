package com.laetienda.mail;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class Email {
	private final static Logger log4j = Logger.getLogger(Email.class);
	
	private InternetAddress from;
	private InternetAddress to;
	private String subject;
	private String text;
	private Session session;
	
	public Email(Session session, InternetAddress from) throws MailException {
		this.session = session;
		this.from = from;
	}
	
	public void send(String to, String subject) throws MailException {
		setTo(to);
		setSubject(subject);
		send();
	}
	
	public void send() throws MailException {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(from);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setContent(text, "text/html; charset=utf-8");
			Transport.send(message);
			log4j.debug("Email message from " + from.toString() + " to " + to.toString() + " has been sent succesfully.");

		} catch (MessagingException ex) {
			throw new MailException("Failed to send message", ex);
		}
	}
	
	public InternetAddress getFrom() {
		return from;
	}
	
	public void setFrom(InternetAddress from) {
		this.from = from;
	}

	public InternetAddress getTo() {
		return to;
	}
	
	public void setTo(String to) throws MailException {
		try {
			setTo(new InternetAddress(to));
		} catch (AddressException ex) {
			throw new MailException("Failed to set \"to\" mail address", ex);
		}
	}

	public void setTo(InternetAddress to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public Email setText(String jsp, HttpServletRequest request, HttpServletResponse response) throws MailException {
		
		try {
			HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
	            private final StringWriter sw = new StringWriter();
	
	            @Override
	            public PrintWriter getWriter() throws IOException {
	                return new PrintWriter(sw);
	            }
	
	            @Override
	            public String toString() {
	                return sw.toString();
	            }
	        };
	        
			request.getRequestDispatcher(jsp).include(request, responseWrapper);
			
	        this.text = responseWrapper.toString();
	        
		}catch(IOException ex) {
			throw new MailException("Failed to get text from jsp file", ex);
		}catch (ServletException ex) {
			throw new MailException("Failed to get text from jsp file", ex);
		}
		return this;
	}
	
	//For testing propuses
	protected Email setText(String text) {
		this.text = text;
		return this;
	}

	public static void main(String[] args) {
		
	}

}
