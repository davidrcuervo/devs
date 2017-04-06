package com.laetienda.notes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bean {
	
	private String directory;
	private String pandoc;
	private File file;
	private List<String> command;
	private String format;
	
	public Bean(String pandoc, String pathToFile){
		file = new File(pathToFile);
		
		this.directory = pathToFile;
		this.pandoc = pandoc;
		setFormat(null);
	}
	
	public String getPrint(){
		
		String result = new String("<div class=\"alert alert-warning\" role=\"alert\">Not valide file</div>");
		int index = file.getName().lastIndexOf('.');
		
		if(index > 0){
			String temp = file.getName().substring(index + 1);
			
			if(temp.equalsIgnoreCase("md")){
				result = getWithPandoc();
			}else if(temp.equals("html")){
				result = getRead();
			}else{
				
			}
		}
		
		return result;
	}
	
	public String getWithPandoc(){
		String result = "";
		String s = null;
		
		/*
		for(String temp : command){
			System.out.print(temp + " ");
		}
		System.out.println();
		*/
		
		try{	
			Process p = new ProcessBuilder(command).start();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
					
			while((s = stdInput.readLine()) != null){
				result += s + "\n";
			}
			
		}catch(IOException ex){
			result = "<textarea>" + ex.getMessage() + "</textarea>";
			System.err.println(ex.getMessage());
			System.err.println(ex.getClass().getName());
		}
		
		return result;
	}
	
	public File getFile() {
		return file;
	}
	
	public List<String> getFolders(){
		
		List<String> result = new ArrayList<String>();
		
		for(String temp : file.list()){
			File f = new File(directory + File.separator + temp);
			
			if(f.isDirectory()){
				
				result.add(temp);
			}
		}
		return result;
	}
	
	public List<String> getFiles(){
		List<String> result = new ArrayList<String>();
		
		String[] list = file.list(
					new FilenameFilter(){
						
						public boolean accept(File file, String name){
							return name.toLowerCase().endsWith(".md") || name.toLowerCase().endsWith(".html");
						}
					}
				);
		
		for(String temp : list){
			File f = new File(directory + File.separator + temp);
			
			if(f.isFile() && !f.isHidden() && !temp.startsWith(".")){
				result.add(temp);
			}
		}
		
		return result;
	}
	
	public void setFormat(String format){
		
		format = format == null ? "plain" : format;
		this.format = format;
		
		switch(format){
			case "play":
				command = Arrays.asList(pandoc,
						//"-s",
						"-f", "markdown",
						"-t", "revealjs",
						//"-V", revealjs,
						"--variable", "theme=beige",
						file.getAbsolutePath());
				break;
				
			case "pdf":
				
			case "plain":
				
			default:
				command = Arrays.asList(pandoc, 
						"-f", "markdown", 
						"--wrap=preserve", 
						"--table-of-contents", 
						"-t", "html5",
						file.getAbsolutePath());
				break;
		}
	}
	
	public String getFormat(){
		return this.format;
	}
	
	public String getRead(){
		String result = null;
		
		try{
			byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			result = new String(encoded, "UTF-8");
		}catch (IOException ex){
			ex.printStackTrace();
		}
		
		return result;
	}
	
	public String getUrl(){
		return "/notes";
	}
	
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		
		file = new File(directory);
		this.directory = directory;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
