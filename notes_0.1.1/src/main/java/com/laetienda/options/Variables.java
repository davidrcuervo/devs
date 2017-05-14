package com.laetienda.options;


import java.util.List;

//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Variables {

	@XmlElement(name = "variable")
	private List<Variable> variables;
	
	public List<Variable> getVariables(){
		return variables;
	}
}
