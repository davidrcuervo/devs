package com.laetienda.web.bin;

import java.io.File;

import com.laetienda.app.AppException;
import com.laetienda.install.Instalador;

public class Installer {

	public static void main(String[] args) {
		
		File directory = new File("");
		
		Instalador installer = new Instalador(directory);
	
		try{
			installer.parseCommand(args);
			installer.dap();
		}catch(AppException ex){
			if(ex.getParent() != null){
				System.err.println(ex.getMessage());
				ex.getParent().printStackTrace();
			}else{
				ex.printStackTrace();
			}
		}
	}
}
