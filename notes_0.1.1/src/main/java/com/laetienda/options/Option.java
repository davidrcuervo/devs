package com.laetienda.options;

//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Option {
	
	@XmlElement
	private String value;
	
	@XmlElement
	private String description;
	
	public String getValue(){
		return value;
	}
	
	public String getDescription(){
		return description;
	}
}
