package com.laetienda.notes.bin;

import java.io.IOException;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.laetienda.notes.lib.Notes;

public class Controller {
	
	
	Options options;
	CommandLineParser parser;
	CommandLine line;
	Notes notes;
	
	Controller() throws Exception{
		options = setOptions();
		parser = new DefaultParser();
		notes = new Notes();
	}
	
	public static void main(String[] args) throws Exception {
				
		Controller controller = new Controller();
		controller.parseArguments(args);
	}
	
	private Options setOptions(){
		return new Options()
				.addOption(new Option("h", "help", false, "Show help."))
				.addOption(new Option("start", false, "Start notes Service"))
				.addOption(new Option("stop", false, "Stop notes service"))
				.addOption(new Option("status", false, "Status of notes service"));
	}
	
	private void parseArguments(String[] args) throws Exception{
		
		line = parser.parse(options, args);
		
		if(line.hasOption("help")){
			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Logger", options);
			
		}else if(line.hasOption("status")){
			
		}else if(line.hasOption("stop")){
			//TODO make the client http to stop the service
			
		}else if(line.hasOption("start")){
			notes.getTomcat().start();
		}else{
			throw new IOException("You have chose wrong option");
		}
		
	}
	
	
}
