package com.laetienda.notes.bin;

import java.util.Scanner;
import com.laetienda.printer.Console;
import com.laetienda.notes.lib.Notes;

public class Command {
	
	private final String[] options = {
			"Stop application",
			"Status of application"
			};
	
	private Notes notes;
	private Console console;
	private boolean flag;
	private int option;
	Scanner scan;
	
	
	public Command() throws Exception{
		
		console = new Console();
		option = -1;
		scan = new Scanner(System.in);
		notes = new Notes();
	}
	
	public static void main(String[] args) throws Exception {
		
		Command cmd = new Command();
		cmd.notes.getTomcat().start();
		cmd.printHeader();
		
		do{
			cmd.printOptions();
			cmd.scanOption();
			cmd.processOption();
		}while(cmd.isRunning());
		
		cmd.console.sayGoodBye();
	}
	
	public void processOption(){
		
		switch (option){
			case 0:
				try{
					notes.getTomcat().stop();
				}catch(Exception ex){
					System.err.println("Error while closing tomcat");
				}finally{
					flag = false;
				}
				break;
				
			case 1:
				//TODO implement status option
				System.out.println("Option not implemented yet");
				break;
			
			default:
				wrongInput();
				break;
		}
	}
	
	private boolean isRunning(){
		return flag;
	}
	
	private void scanOption(){
		boolean flag = false;
		String text = scan.nextLine();
		
		try{
			option = Integer.parseInt(text);
			
				if(option > options.length){
				flag = true;
			}else{
				option = -1;
			}
		}catch(Exception ex){
			flag = false;
		}finally{
			if(!flag){
				wrongInput();
			}
		}
	}
	
	private void wrongInput(){
		System.err.println("You have selected a non valid option");
		System.err.println("Please hit enter to continue");
		scan.nextLine();
	}
	
	private void printHeader(){
		console.emptyLines(10);
		console.center("NOTES APPLICATION");
		console.center("=================");
		console.emptyLines(5);
	}
	
	private void printOptions(){
		console.center("Chose an option");
		console.center("---------------");
		console.emptyLines(1);
		console.options(options);
		console.emptyLines(1);
		System.out.print("Chose an option: ");
	}
}
