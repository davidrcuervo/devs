package com.laetienda.printer;

public class Console {
	
	private int width;
	private int height;
	
	public Console(){
		width = 120;
		height = 80;
	}
	
	public void emptyLines(int lines){
		for(int c=0; c < lines; c++)
			System.out.println();
	}
	
	public void center(String text){
		int margin = (width - text.length()) / 2;
		
		if(margin > 0)
			for(int c=0; c < margin; c++)
				System.out.print(" ");
		
		System.out.println(text);
	}
	
	public void fillLineWithChar(String text){
		int lenght = text.length();
		
		for(int c = 0; c < width; c += lenght){
			System.out.println(text);
		}
	}
	
	public void options(String[] options){
		for(int c = 0;  c < options.length; c++){
			System.out.println((c + 1) + ".\t" + options[c]);
		}
	}
	
	public void sayGoodBye(){
		emptyLines(2);
		
		center("Program has closed.");
		center("Hope to see you son");
		center("GOOD BYE!!!");
		emptyLines(10);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
