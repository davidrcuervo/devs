package com.laetienda.notes.bin;

import java.io.File;

public class Controller {
	
	public static final String DIRECTORY = "/home/myself/git/eclipse/Web.opt";

	public static void main(String[] args) throws Exception {
				
		com.laetienda.tomcat.bin.Controller controller = new com.laetienda.tomcat.bin.Controller(new File(DIRECTORY));
		controller.parseArguments(args);
	}
}
