package com.laetienda.web.bin;

import java.io.File;
import com.laetienda.tomcat.bin.Controller;

public class Run {
	
	public static final String DIRECTORY = "/home/myself/git/eclipse/Web.opt";
	private File directory;
	private boolean devMode;
	
	private Run(){
		directory = findDirectory();
		devMode = findDevMode();
	}
	
	public static void main(String[] args) {
		Run run = new Run();
		
		try{			
			if(run.devMode){
				run.directory = new File(DIRECTORY);
			}
			
			Controller tomcat  = new Controller(run.directory);
			tomcat.parseArguments(args);
			
		}catch (Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private File findDirectory(){
		
		String tempPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String temp = new File(tempPath).getParentFile().getAbsolutePath() + File.separator + "..";
		
		return new File(temp);
	}
	
	private boolean findDevMode(){
		
		boolean result = true;
		
		File confFile = new File(directory.getAbsolutePath() + 
				File.separator + "etc" +
				File.separator + "web" +
				File.separator + "conf.xml");
		
		if(confFile.exists() && !confFile.isDirectory()){
			result = false;
		}
		
		return result;
	}
}
