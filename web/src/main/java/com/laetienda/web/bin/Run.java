package com.laetienda.web.bin;

import java.io.File;
import com.laetienda.tomcat.Controller;
import com.laetienda.tomcat.TomcatException;

public class Run {
	
	public static void main(String[] args) {
		System.out.println("Starting website");
		
		File directory = new File("");
		
		try{
			Controller tomcat  = new Controller(directory);
			tomcat.parseArguments(args);
			
			//System.out.println(directory.getAbsolutePath() + File.separator + "WebContent");
		}catch(TomcatException ex){
			if(ex.getParent() != null){
				System.err.println(ex.getMessage());
				ex.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
			
		}
	}
}
