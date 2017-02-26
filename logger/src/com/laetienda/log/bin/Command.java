package com.laetienda.log.bin;

import javax.persistence.EntityManager;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.io.IOException;
import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import com.laetienda.log.Logger;
import com.laetienda.log.entities.Log;
import com.laetienda.db.Connection;

public class Command {
	
	public static final String DIRECTORY = "/home/myself/git/eclipse/Web.opt";
	
	private Logger logger;
	private CommandLineParser parser;
	private CommandLine line;
	private File directory;
	
	public Command() throws Exception{
		directory = findDirectory();
		logger = new Logger(directory);
		parser = new DefaultParser();
	}

	public static void main(String[] args) {
		
		try{
			Command cmd = new Command();
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
			
			//logger.loadConfFile(line.getOptionValue("directory"));
			
			for(Option opt : line.getOptions()){
				
				if(/*!opt.getLongOpt().equals("directory") &&*/ !opt.getLongOpt().equals("help")){
					logger.setSetting(opt.getLongOpt(), opt.getValue());
				}
			}
			
		}else{
			throw new IOException("FATAL: directory of application has not been detected");
		}
	}
	
	private File findDirectory(){
		
		File result;
		
		String tempPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String temp = new File(tempPath).getParentFile().getAbsolutePath() + File.separator + "..";
		
		File confFile = new File(temp + 
				File.separator + "etc" +
				File.separator + "database" +
				File.separator + "conf.xml");
		
		if(confFile.exists() && !confFile.isDirectory()){
			result = new File(temp);
		}else{
			result = new File(DIRECTORY);
		}
		
		return result;
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
		
		Connection dbConnection = new Connection(directory);
				
		return dbConnection.getEm();
	}
}
