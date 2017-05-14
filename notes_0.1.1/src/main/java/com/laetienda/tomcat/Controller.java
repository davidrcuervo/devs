package com.laetienda.tomcat;

import java.io.IOException;
import java.io.File;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import com.laetienda.tomcat.Service;


public class Controller {
	
	private Options options;
	private CommandLineParser parser;
	private CommandLine line;
	private Service daemon;
	
	public Controller(File directory) throws Exception{
		options = setOptions();
		parser = new DefaultParser();
		daemon = new Service(directory);
	}

	private Options setOptions(){
		return new Options()
			.addOption(new Option("h", "help", false, "Show help."))
			.addOption(new Option("start", false, "Start notes Service"))
			.addOption(new Option("stop", false, "Stop notes service"));
	}
	
	public void parseArguments(String[] args) throws Exception{
		
		line = parser.parse(options, args);
		
		if(line.hasOption("help")){			
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Notes service", options);
			
		}else if(line.hasOption("stop")){
			daemon.shutdown();
			
		}else if(line.hasOption("start")){
			daemon.start();
			daemon.await();
			daemon.stop();
		}else{
			throw new IOException("You have chose wrong option");
		}
	}
}
