package com.laetienda.notes.bin;

public class Controller {

	public static void main(String[] args) throws Exception {
				
		com.laetienda.tomcat.bin.Controller controller = new com.laetienda.tomcat.bin.Controller();
		controller.parseArguments(args);
	}
}
