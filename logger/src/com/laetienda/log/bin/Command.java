package com.laetienda.log.bin;

import javax.persistence.EntityManager;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import com.laetienda.log.Logger;
import com.laetienda.log.entities.Log;
import com.laetienda.db.Connection;

public class Command {
	
	Logger logger;
	CommandLineParser parser;
	CommandLine line;
	
	public Command(){
		logger = new Logger();
		parser = new DefaultParser();
	}

	public static void main(String[] args) {
		
		Command cmd = new Command();
		
		try{
			cmd.parseArguments(args);
			cmd.run();
		}catch (Exception ex){
			System.err.println(ex.getMessage());
			System.err.println(ex.getClass().getName());
			ex.printStackTrace();
			System.exit(1);
		}finally{
			
		}
	}
	
	public void parseArguments(String[] args) throws ParseException, IOException{
		
		line = parser.parse(logger.getOptions(), args);
			
		if(line.hasOption("help")){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Logger", logger.getOptions());
		
		}else if(line.hasOption("directory")){
			
			logger.loadConfFile(line.getOptionValue("directory"));
			
			for(Option opt : line.getOptions()){
				
				if(/*!opt.getLongOpt().equals("directory") &&*/ !opt.getLongOpt().equals("help")){
					logger.setSetting(opt.getLongOpt(), opt.getValue());
				}
			}
			
		}else{
			throw new IOException("FATAL: directory of application has not been detected");
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void run() throws Exception {
		
		Log logEntity = logger.getEntity();
		logger.setMessage(logger.getSetting("message"));
		
		if(line.hasOption("file"))
			logger.setSetting("saveTO", "file");
		
		if(logger.getSetting("saveTO").equals("file")){
		
			logger.setFile(logger.getSetting("file"));
			logger.setSetting("saveTO", "file");
		
		}else if(logger.getSetting("saveTO").equals("database")){
			
			logger.setDb(setDbConnection());
			
		}
		
		logEntity.setUser(logger.getSetting("user"));
		logEntity.setProgram(logger.getSetting("program"));
		logEntity.setLevel(logger.getSetting("level"));
		logEntity.setMethod(logger.getSetting("method"));
		logEntity.setLine(Integer.parseInt(logger.getSetting("line")));
		
		logger.print();
	}
	
	private EntityManager setDbConnection() throws Exception{
		
		Connection dbConnection = new Connection();
		dbConnection.loadConfFile(logger.getSetting("directory"));
		dbConnection.open();
		
		return dbConnection.getEm();
	}
}
