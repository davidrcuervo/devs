package com.laetienda.install;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.laetienda.dap.DapException;

public class Instalador {
	
	private static final Options OPTIONS = new Options()
			.addOption(new Option("u", "user", true, "Admin user DN with priviledges to create domain settings."))
			.addOption(new Option("h", "help", false, "Help"))
			.addOption(new Option("p", "password", true, "Password of the admin user to connecto to Apacheds Directory"))
			.addOption(new Option("r", "rootPassword", true, "Password of the "));
	
	private File directory;
	private CommandLine line;
	
	public Instalador(File directory){
		this.directory = directory;
	}
	
	public void parseCommand(String[] args) throws InstallerException{
		
		try{
			CommandLineParser parser = new DefaultParser();
			line = parser.parse(OPTIONS, args);
			
			if(line.hasOption("help")){
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Install options", OPTIONS);
			}
		}catch(ParseException ex){
			throw new InstallerException("Error while parsing arguments", ex);
		}
	}
	
	public void dap() throws DapException{
	
		if(line.hasOption("user") && line.hasOption("password")){
			
			com.laetienda.dap.Installer installer = new com.laetienda.dap.Installer(directory);
			
			if(line.hasOption("rootPassword")){
				installer.setRootPassword(line.getOptionValue("rootPassword"));
			}
			
			installer.install(line.getOptionValue("user"), line.getOptionValue("password"));
		}else{
			System.err.println("User dn and password of a priviledged user must be provided");
		}	
	}
}
