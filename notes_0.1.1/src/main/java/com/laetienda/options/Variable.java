package com.laetienda.options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
public class Variable {
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String description;
	
	@XmlElement(name = "option")
	private List<Option> options;
	
	private Map<String, Option> opts;
	
	protected void buildOptionsMap(){
		if(opts == null){
			opts = new HashMap<String, Option>();
		}
		
		for(Option row : options){
			opts.put(row.getValue(), row);
		}
	}
	
	public Option findByValue(String value){
		return opts.get(value);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the options
	 */
	public List<Option> getOptions() {
		return options;
	}
}
