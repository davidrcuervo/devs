package com.laetienda.options;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.laetienda.removed.Variables;

public class OptionsManager {
	
	private List<Variable> variables;
	private Map<String, Variable> vars;
	
	public OptionsManager(File directory) throws OptionException{
		
		String pathToFile = directory.getAbsolutePath() + File.separator + "etc" + File.separator + "variables.xml";
		File file = new File(pathToFile);
		vars = new HashMap<String, Variable>();
		System.out.println("file: " + file.getAbsolutePath());
		
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(Variables.class);
			Unmarshaller jaxbUnmasrshaller = jaxbContext.createUnmarshaller();
			
			Variables tempVars = (Variables)jaxbUnmasrshaller.unmarshal(file);
			variables = tempVars.getVariables();
			
			for(Variable row : variables){
				row.buildOptionsMap();
				vars.put(row.getName(), row);
			}
			
		}catch(JAXBException ex){
			throw new OptionException("Failed to build ", ex);
		}
	}
	
	public Map<String, Variable> getVariablesMap(){
		return vars;
	}
	
	public Option findOption(String variable, String option){
		Option result = null;
		
		Variable temp = vars.get(variable);
		
		if(temp != null){
			result = temp.findByValue(option);
		}
		
		return result;
	}
	
	public List<Variable> getVariables(){
		return variables;
	}
	
	public static void main(String args[]){
		
		try{
			File directory = new File("");
			OptionsManager opts = new OptionsManager(directory);
			
			List<Variable> variables = opts.getVariables();
	
			for(Variable variable : variables){
				
				System.out.println("Name: " + variable.getName());
				System.out.println("Description: " + variable.getDescription());
				
				System.out.println("OPTIONS:");
				System.out.println("VALUE \t DESCRIPTION");
				for(Option option : variable.getOptions()){
					System.out.println(option.getValue() + "\t" + option.getDescription());
				}
			}
			
			System.out.println();
			
			Option test = opts.findOption("User status", "deleted");
			System.out.println("Value: " + test.getValue() + "\t Description: " + test.getDescription());
		}catch(OptionException ex){
			ex.getParent().printStackTrace();
		}
	}
}
