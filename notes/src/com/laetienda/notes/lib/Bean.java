package com.laetienda.notes.lib;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;

public class Bean {
	
	private String directory;
	private String pandoc;
	private File file;
	
	public Bean(String pandoc, String directory){
		setDirectory(directory);
		this.pandoc = pandoc;
	}
	
	public String getPrint(){
		String result = "";
		String s = null;
		
		try{
			Process p = Runtime.getRuntime().exec(pandoc + " -f markdown -t html " + file.getAbsolutePath());
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			while((s = stdInput.readLine()) != null){
				result += s;
			}
			
		}catch(IOException ex){
			System.err.println(ex.getMessage());
			System.err.println(ex.getClass().getName());
		}
		
		return result;
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
							return name.toLowerCase().endsWith(".md");
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
